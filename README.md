move to [dsee](https://github.com/angelndevil2/dsee)

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

* file :  __/path/to/universal-jvm-agent/conf/agent.properties__
* turn off logback log __logback.use=false__

## jetty.properties

* file : __/path/to/universal-jvm-agent/conf/jetty.properties__
* default port : 1080
    * property : http.port 

## logback.xml

* file : __/path/to/universal-jvm-agent/conf/logback.xml__
* log file : /path/to/universal-jvm-agent/logs/universal-jvm-agent.log
* rotation : daily

## Status

* developed with weblogic 12c
* mbean developed with websphere 8.5.5.8
* not fully tested
