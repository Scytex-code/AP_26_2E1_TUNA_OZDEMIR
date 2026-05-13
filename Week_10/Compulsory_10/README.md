# Lab 10 - Compulsory

This folder contains two projects:

- `ServerApplication`: contains `GameServer` and `ClientThread`.
- `ClientApplication`: contains `GameClient`.

Run the server first:

```text
cd ServerApplication
mvn package
java -jar target/server-application-compulsory-1.0-SNAPSHOT.jar 5000
```

Then run the client:

```text
cd ClientApplication
mvn package
java -jar target/client-application-compulsory-1.0-SNAPSHOT.jar localhost 5000
```

The client reads commands from the keyboard. It exits on `exit`. The server stops when it receives `stop` and returns `Server stopped`; otherwise it returns `Server received the request ...`.
