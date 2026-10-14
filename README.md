# simple-executor-master

A Java library and OSGi service for executing external programs and collecting their results.
Built on Apache Commons Exec, with per-program configuration loaded from a properties file,
an execution buffer for multi-destination results, JMX-based configuration reloading,
and Apache Camel Blueprint routes for request-driven execution over a message queue.

## How it works

1. A **program request** (`Program` DTO: name + argument list) is submitted to the executor.
2. The `ConfigurationManager` resolves the program's configuration from a properties file:
   the binary path, the exit codes that count as success, a cache directory and a user.
3. `ProCExecutorCommonsExec` runs the binary via Apache Commons Exec, captures `stdout` and
   `stderr`, waits for the exit code, and reads the program's output file from a temp directory.
4. The result is returned as an `Execution` DTO: program, start/end timestamps, return code,
   a success flag (derived from the configured success exit codes), stdout, stderr,
   an error description on failure, and the raw output bytes.

In the OSGi deployment, a Camel route watches a drop directory for JSON program requests,
executes them through the buffer, and publishes the JSON `Execution` results to an
ActiveMQ out queue. A second route consumes requests from an in queue. Program
configuration can be reloaded at runtime through the `ConfigReloader` JMX MBean.

## Features

- Execute arbitrary external programs with arguments (`ProgramExecutor` /
  `ProCExecutorCommonsExec`, Apache Commons Exec).
- Properties-file based per-program configuration
  (`ConfigurationManagerPropertiesImpl`).
- Rich execution results (`Execution` DTO): timestamps, return code, stdout/stderr,
  success flag, error description, raw result bytes.
- Execution buffering (`ExecutionBuffer` / `ExecutionBufferController`) to fan a
  single result out to multiple destinations, with per-execution log files.
- JMX `ConfigReloader` MBean to reload program configuration without restarting.
- Apache Camel Blueprint integration: file-drop request route, JSON marshalling
  (Jackson), ActiveMQ in/out queues.
- OSGi bundle packaging (`maven-bundle-plugin`), deployable on Red Hat Fuse /
  JBoss Fuse style containers.

## Configuration

Programs are declared in a Java properties file. For each program `<name>` the
following keys are recognized (see `ConfigurationManager` for the exact names):

```properties
<name>.path=<absolute path of the program binary>
<name>.success-return-values=<comma-separated list of exit codes treated as success>
<name>.cache-dir=<directory passed to the program as an extra argument>
<name>.user=<user passed to the program as an extra argument>
```

Example:

```properties
mycron.path=/opt/tools/mycron.sh
mycron.success-return-values=0
mycron.cache-dir=/var/cache/mycron
mycron.user=svcuser
```

If `success-return-values` is empty or missing, any exit code is treated as success.
The executor appends the program's result temp file path, `cache-dir` and `user` to
the argument list before running the binary.

A program request is a JSON document unmarshalled into the `Program` DTO:

```json
{
  "name": "mycron",
  "arguments": ["--daily", "--verbose"]
}
```

## Camel / OSGi deployment

The Blueprint descriptors live in `src/main/resources/OSGI-INF/blueprint/`:

| File | Purpose |
|---|---|
| `00-properties.xml` | Property placeholders (drop directory, queue names) |
| `01-destinations.xml` | Destination / buffer wiring |
| `02-process-bean.xml` | Executor and buffer bean wiring |
| `03-main-route.xml` | Main routes: file drop -> execute -> ActiveMQ out; ActiveMQ in -> execute |
| `04-jmx-mbeans.xml` | JMX MBean registration (config reload) |

## Building

Requires JDK 7+ and Maven 3. The Maven project (with `pom.xml`) is under `bin/`:

```bash
cd bin
mvn clean install
```

This produces the `simple-executor` OSGi bundle. `bin/` also carries the Eclipse
project files (`.project`, `.settings`).

> Note: `bin/src` is a copy of the same sources as `src/` at the repository root.
> `bin/` is the buildable Maven layout; the root `src/` tree is kept as the
> canonical source view.

## Project structure

```
README.md
src/
  main/java/com/rsa/proc/
    ProgramExecutor.java              # Execution contract
    ProCExecutorCommonsExec.java      # Apache Commons Exec implementation
    config/
      ConfigurationManager.java       # Configuration contract + property key names
      impl/ConfigurationManagerPropertiesImpl.java  # Properties-file loader
      ConfigurationEnvironment.java   # Environment variables for executions
      ProgramConfiguration.java       # Per-program configuration DTO
    dto/
      Program.java                    # Request DTO: name + arguments
      Execution.java                  # Result DTO: timestamps, return code, streams
    buffer/
      ExecutionBuffer.java            # Fan-out execution over destinations
      ExecutionBufferController.java  # Destination list contract
      impl/ExecutionBufferControllerMapImpl.java
    jmx/
      ConfigReloader.java             # JMX MBean: reload configuration at runtime
      ConfigReloaderMBean.java
    streams/
      AccumulatingLogOutputStream.java # Captures process stdout/stderr
    ClassDiagramModel.ucls            # UML class model of the design
  main/resources/OSGI-INF/blueprint/  # Camel routes and bean wiring
  test/resources/                      # log4j.properties and a sample SOAP request
bin/                                   # Maven project (pom.xml) + Eclipse files
target/                                # Build output (generated)
```

## Requirements

- Java 7 or later
- Maven 3
- Apache Commons Exec, SLF4J, Apache Camel (Blueprint + Jackson), ActiveMQ
  (pulled in via the Fabric8 BOM `1.2.0.redhat-621084` declared in the POM)

## License

No license file is present in this repository. If you intend to reuse this code,
add a license that reflects the original author's intent.
