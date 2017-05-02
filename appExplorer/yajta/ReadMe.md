# Yet Another Java Tracing Agent

Java agent tracing methods calls.

Usage:
```
java -javaagent:yajta/target/yajta-1.0-SNAPSHOT-jar-with-dependencies.jar myApp > output.json
```

To clean the output file:
```
java -jar yajta/target/yajta-1.0-SNAPSHOT-jar-with-dependencies.jar output.json
```

