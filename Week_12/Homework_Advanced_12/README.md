# Lab 12 - Homework and Advanced

This project scans a folder containing `.class` files, compiles `.java` source files found in that folder, identifies annotation types, prints public class prototypes, instruments annotated methods with Javassist, and invokes annotated methods that have either no parameters or one integer parameter.

Run against the included sample classes:

```bash
mvn -q package
java -jar target/homework-advanced-12-1.0-SNAPSHOT.jar target/classes
```

Run against any other folder:

```bash
java -jar target/homework-advanced-12-1.0-SNAPSHOT.jar C:\path\to\classes-or-sources
```
