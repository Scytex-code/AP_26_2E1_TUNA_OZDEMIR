package com.example.lab12compulsory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionRunner {
    public static void main(String[] args) {
        String className = args.length > 0 ? args[0] : "com.example.lab12compulsory.samples.SampleRunnable";

        try {
            Class<?> targetClass = Class.forName(className);
            Method runMethod = targetClass.getDeclaredMethod("run");

            if (runMethod.getParameterCount() != 0) {
                System.out.println("The run method must not have arguments.");
                return;
            }

            Object instance = null;
            if (!Modifier.isStatic(runMethod.getModifiers())) {
                Constructor<?> constructor = targetClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                instance = constructor.newInstance();
            }

            runMethod.setAccessible(true);
            System.out.println("Invoking " + targetClass.getName() + ".run()");
            runMethod.invoke(instance);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found in the application classpath: " + className);
        } catch (NoSuchMethodException e) {
            System.out.println("The class does not contain a run method with no arguments.");
        } catch (ReflectiveOperationException e) {
            System.out.println("Could not invoke run: " + e.getMessage());
        }
    }
}
