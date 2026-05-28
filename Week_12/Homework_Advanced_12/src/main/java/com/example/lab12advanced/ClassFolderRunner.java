package com.example.lab12advanced;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ClassFolderRunner {
    private static final int MOCK_INTEGER_VALUE = 42;

    public static void main(String[] args) {
        Path inputFolder = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("target/classes").toAbsolutePath().normalize();

        try {
            new ClassFolderRunner().run(inputFolder);
        } catch (Exception e) {
            System.out.println("Analysis failed: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private void run(Path inputFolder) throws Exception {
        if (!Files.isDirectory(inputFolder)) {
            throw new IllegalArgumentException("Input must be a folder: " + inputFolder);
        }

        Path compiledSources = Files.createTempDirectory("lab12-compiled-sources");
        Path instrumentedClasses = Files.createTempDirectory("lab12-instrumented-classes");

        compileJavaSources(inputFolder, compiledSources);

        List<Path> roots = new ArrayList<>();
        roots.add(inputFolder);
        roots.add(compiledSources);

        List<ClassEntry> entries = findClassEntries(roots);
        if (entries.isEmpty()) {
            System.out.println("No .class files were found.");
            return;
        }

        Set<String> annotationNames = findAnnotationTypes(roots, entries);
        printAnnotationTypes(annotationNames);

        instrumentAnnotatedMethods(roots, entries, annotationNames, instrumentedClasses);

        List<Path> runtimeRoots = new ArrayList<>();
        runtimeRoots.add(instrumentedClasses);
        runtimeRoots.addAll(roots);

        try (URLClassLoader loader = createChildFirstClassLoader(runtimeRoots)) {
            analyzePublicClasses(loader, entries, annotationNames);
        }
    }

    private void compileJavaSources(Path inputFolder, Path outputFolder) throws IOException {
        List<File> sources;
        try (Stream<Path> stream = Files.walk(inputFolder)) {
            sources = stream
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .toList();
        }

        if (sources.isEmpty()) {
            return;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("A JDK is required because the input contains .java files.");
        }

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            List<String> options = List.of(
                    "-d", outputFolder.toString(),
                    "-classpath", System.getProperty("java.class.path") + File.pathSeparator + inputFolder
            );
            boolean success = compiler.getTask(
                    null,
                    fileManager,
                    null,
                    options,
                    null,
                    fileManager.getJavaFileObjectsFromFiles(sources)
            ).call();

            if (!success) {
                throw new IllegalStateException("Compilation of source files failed.");
            }
            System.out.println("Compiled " + sources.size() + " source file(s) to " + outputFolder);
        }
    }

    private List<ClassEntry> findClassEntries(List<Path> roots) throws IOException {
        List<ClassEntry> entries = new ArrayList<>();
        for (Path root : roots) {
            try (Stream<Path> stream = Files.walk(root)) {
                stream
                        .filter(path -> path.toString().endsWith(".class"))
                        .filter(path -> !path.getFileName().toString().contains("$"))
                        .forEach(path -> entries.add(new ClassEntry(root, path, toClassName(root, path))));
            }
        }
        entries.sort(Comparator.comparing(ClassEntry::className));
        return entries;
    }

    private String toClassName(Path root, Path classFile) {
        String relative = root.relativize(classFile).toString();
        return relative.substring(0, relative.length() - ".class".length())
                .replace(File.separatorChar, '.');
    }

    private Set<String> findAnnotationTypes(List<Path> roots, List<ClassEntry> entries) throws IOException {
        Set<String> annotationNames = new HashSet<>();
        try (URLClassLoader loader = createClassLoader(roots)) {
            for (ClassEntry entry : entries) {
                try {
                    Class<?> clazz = Class.forName(entry.className(), false, loader);
                    if (clazz.isAnnotation()) {
                        annotationNames.add(clazz.getName());
                    }
                } catch (LinkageError | ClassNotFoundException e) {
                    System.out.println("Skipping " + entry.className() + ": " + e.getMessage());
                }
            }
        }
        return annotationNames;
    }

    private void printAnnotationTypes(Set<String> annotationNames) {
        System.out.println();
        System.out.println("Annotation types");
        if (annotationNames.isEmpty()) {
            System.out.println("  none");
            return;
        }
        annotationNames.stream().sorted().forEach(name -> System.out.println("  " + name));
    }

    private void instrumentAnnotatedMethods(
            List<Path> roots,
            List<ClassEntry> entries,
            Set<String> annotationNames,
            Path outputFolder
    ) throws Exception {
        if (annotationNames.isEmpty()) {
            return;
        }

        ClassPool classPool = new ClassPool(true);
        try (URLClassLoader helperLoader = createClassLoader(roots)) {
            classPool.appendClassPath(new LoaderClassPath(helperLoader));
            for (Path root : roots) {
                classPool.appendClassPath(root.toString());
            }

            for (ClassEntry entry : entries) {
                CtClass ctClass = classPool.get(entry.className());
                if (ctClass.isAnnotation() || ctClass.isInterface()) {
                    ctClass.detach();
                    continue;
                }

                boolean modified = false;
                for (CtMethod method : ctClass.getDeclaredMethods()) {
                    if (hasKnownAnnotation(method, annotationNames)) {
                        String message = "[LOG] Executing " + entry.className() + "." + method.getName();
                        method.insertBefore("System.out.println(\"" + message + "\");");
                        modified = true;
                    }
                }

                if (modified) {
                    ctClass.writeFile(outputFolder.toString());
                }
                ctClass.detach();
            }
        }
    }

    private boolean hasKnownAnnotation(CtMethod method, Set<String> annotationNames) {
        MethodInfo methodInfo = method.getMethodInfo();
        return hasKnownAnnotation(methodInfo, AnnotationsAttribute.visibleTag, annotationNames)
                || hasKnownAnnotation(methodInfo, AnnotationsAttribute.invisibleTag, annotationNames);
    }

    private boolean hasKnownAnnotation(MethodInfo methodInfo, String tag, Set<String> annotationNames) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) methodInfo.getAttribute(tag);
        if (attribute == null) {
            return false;
        }
        for (Annotation annotation : attribute.getAnnotations()) {
            if (annotationNames.contains(annotation.getTypeName())) {
                return true;
            }
        }
        return false;
    }

    private void analyzePublicClasses(
            ClassLoader loader,
            List<ClassEntry> entries,
            Set<String> annotationNames
    ) {
        System.out.println();
        System.out.println("Public classes");
        for (ClassEntry entry : entries) {
            try {
                Class<?> clazz = Class.forName(entry.className(), true, loader);
                if (!Modifier.isPublic(clazz.getModifiers()) || clazz.isAnnotation()) {
                    continue;
                }

                printPrototype(clazz);
                invokeAnnotatedMethods(clazz, annotationNames);
            } catch (ReflectiveOperationException | LinkageError e) {
                System.out.println("Could not analyze " + entry.className() + ": " + e.getMessage());
            }
        }
    }

    private void printPrototype(Class<?> clazz) {
        System.out.println();
        System.out.println(clazz.toGenericString());

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("  " + field.toGenericString());
        }
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.println("  " + constructor.toGenericString());
        }
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + method.toGenericString());
        }
    }

    private void invokeAnnotatedMethods(Class<?> clazz, Set<String> annotationNames) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!isAnnotatedWithKnownAnnotation(method, annotationNames)) {
                continue;
            }

            Object[] arguments = buildArguments(method);
            if (arguments == null) {
                System.out.println("  Skipped " + method.getName() + ": unsupported parameters");
                continue;
            }

            try {
                Object target = null;
                if (!Modifier.isStatic(method.getModifiers())) {
                    target = createInstance(clazz);
                    if (target == null) {
                        System.out.println("  Skipped " + method.getName() + ": no default constructor");
                        continue;
                    }
                }

                method.setAccessible(true);
                Object result = method.invoke(target, arguments);
                System.out.println("  Invoked " + method.getName() + " -> " + result);
            } catch (ReflectiveOperationException e) {
                System.out.println("  Invocation failed for " + method.getName() + ": " + e.getMessage());
            }
        }
    }

    private boolean isAnnotatedWithKnownAnnotation(Method method, Set<String> annotationNames) {
        for (java.lang.annotation.Annotation annotation : method.getAnnotations()) {
            if (annotationNames.contains(annotation.annotationType().getName())) {
                return true;
            }
        }
        return false;
    }

    private Object[] buildArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return new Object[0];
        }
        if (parameterTypes.length == 1 && (parameterTypes[0] == int.class || parameterTypes[0] == Integer.class)) {
            return new Object[]{MOCK_INTEGER_VALUE};
        }
        return null;
    }

    private Object createInstance(Class<?> clazz) throws ReflectiveOperationException {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private URLClassLoader createClassLoader(List<Path> roots) throws IOException {
        URL[] urls = roots.stream()
                .map(path -> {
                    try {
                        return path.toUri().toURL();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .toArray(URL[]::new);
        return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    private URLClassLoader createChildFirstClassLoader(List<Path> roots) throws IOException {
        URL[] urls = roots.stream()
                .map(path -> {
                    try {
                        return path.toUri().toURL();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .toArray(URL[]::new);
        return new ChildFirstClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    private static class ChildFirstClassLoader extends URLClassLoader {
        ChildFirstClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass == null && shouldLoadLocally(name)) {
                try {
                    loadedClass = findClass(name);
                } catch (ClassNotFoundException ignored) {
                    loadedClass = super.loadClass(name, false);
                }
            }
            if (loadedClass == null) {
                loadedClass = super.loadClass(name, false);
            }
            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }

        private boolean shouldLoadLocally(String name) {
            return !name.startsWith("java.")
                    && !name.startsWith("javax.")
                    && !name.startsWith("jdk.")
                    && !name.startsWith("sun.")
                    && !name.startsWith("javassist.");
        }
    }

    private record ClassEntry(Path root, Path classFile, String className) {
    }
}
