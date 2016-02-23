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

* command
```
/path/to/universal-jvm-agent/bin/launcher -p pid
```

## agent.properties

* file :  ___/path/to/universal-jvm-agent/conf/agent.properties___
* use ___rmi.server.port___ to change rmi port
* turn off logback log ___logback.use=false___
* for jndi traverse 
    * ___jndi.user.id=___
    * ___jndi.user.password=___
    
## For Use Rmi Client

* ___/path/to/universal-jvm-agent/bin/launcher -c -h rmi_server_addr___ command will give you prompt '>'
* command list
    * ___mbean servers___ : print all MBeanServer ids from MBeanServerFactory
    * ___mbeans___ : to print mbeans
        * __all__ : all MBeans
        * __id__ from 'mbean servers'  command : print MBeans from MBeanServer with id
    * ___domains___ : print MBeanServer's domains
        * __all__ : all domains from all MBeanServers
        * __id__ from 'mbean servers'  command : print domains from MBeanServer with id
    * ___jndi traverse___
        * print jndi name and class name from NameClassPair
        * if security manager is exist, id & password in ___aggent.properties___ file can be used

## Status

* developed with weblogic 12c
* not fully tested
