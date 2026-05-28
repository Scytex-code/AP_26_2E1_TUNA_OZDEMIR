# Lab 12 - Compulsory

Loads a known class from the application classpath, searches for a no-argument `run` method, and invokes it through reflection.

Run the default sample:

```bash
mvn -q package
java -cp target/compulsory-12-1.0-SNAPSHOT.jar com.example.lab12compulsory.ReflectionRunner
```

Run another class already available in the classpath:

```bash
java -cp target/compulsory-12-1.0-SNAPSHOT.jar com.example.lab12compulsory.ReflectionRunner fully.qualified.ClassName
```
