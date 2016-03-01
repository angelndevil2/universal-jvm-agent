# Universal Jvm Agent

jvm inspection agent

## Agent Load

### Load when jvm start

```
use -javaagent:/path/to/universal-jvm-agent/universal-jvm-agent-version.jar
```
### Load to running jvm

can be used with HOTSPOT jvm

* check running jvm's ___pid___
* edit /path/to/universal-jvm-agent/bin/launcher
    * export UNIVERSAL_JVM_AGENT_OPTS=-Xbootclasspath/a:<path_to_jdk>/lib/tools.jar
* command
```
/path/to/universal-jvm-agent/bin/launcher -p pid
```

## agent.properties

* file :  ___/path/to/universal-jvm-agent/conf/agent.properties___
* turn off logback log ___logback.use=false___

## Status

* developed with weblogic 12c
* not fully tested
