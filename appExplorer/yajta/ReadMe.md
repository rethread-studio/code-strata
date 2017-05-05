# yajta (Yet Another Java Tracing Agent)

Java agent tracing methods calls.

Usage:
```
java -javaagent:path/to/yajta/target/yajta-1.0-SNAPSHOT-jar-with-dependencies.jar myApp > output.json
```

To clean the output file:
```
java -jar path/to/yajta/target/yajta-1.0-SNAPSHOT-jar-with-dependencies.jar output.json
```

## Known Limitations

Does not log native methods yet.

# tie (Test Impact Explorer)


## Run test with traces

```
mvn -DargLine="-javaagent:path/to/yajta/target/yajta-1.0-SNAPSHOT-jar-with-dependencies.jar=\"strict-includes|print=list|includes=org.myApp|excludes=fr.inria.yalta\"" test > testLog
```

## Organize the output as a map of methods / set of test

```
java -cp path/to/yajta/target/yajta-1.0-SNAPSHOT-jar-with-dependencies.jar fr.inria.tie.Cleaner -i testLog -o methodsImpact.json -l method
```

