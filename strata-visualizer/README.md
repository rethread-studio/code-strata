## Monitor a java application

### Call tree

 * See ../yalta

### Bytecode

 * Include all jars containing your classes

### x86

 * `java -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly your.app.Main > assembly.log`

### System call

 * `strace -f java your.app.Main`

### Generate web report

```
cd TreeDisplay
mvn clean install
java -jar target/TreeDisplay-1.0-SNAPSHOT-jar-with-dependencies.jar your.config.file
```
