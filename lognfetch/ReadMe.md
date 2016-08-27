To configure log4j:
 -Dlog4JPropertyFile="C:/Work/workspaces/miscPocs/lognfetch/configs/log4j.properties"
 
 To configure JMX:
   -Dcom.sun.management.jmxremote.rmi.port=8700  -Dcom.sun.management.jmxremote.port=8600 -Dcom.sun.management.jmxremote.password.file=C:/Work/workspaces/miscPocs/lognfetch/jmxremote.password -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
   
If on Windows 7 set permissions on the jmx password file:
c:\Work\workspaces\miscPocs\lognfetch>icacls jmxremote.password /setowner joe
processed file: jmxremote.password
Successfully processed 1 files; Failed processing 0 files

c:\Work\workspaces\miscPocs\lognfetch>icacls jmxremote.password /inheritance:r
processed file: jmxremote.password
Successfully processed 1 files; Failed processing 0 files

c:\Work\workspaces\miscPocs\lognfetch>icacls jmxremote.password /grant:r joe:(r,w)
processed file: jmxremote.password
Successfully processed 1 files; Failed processing 0 files

see http://tech-eureka.blogspot.de/2014/09/how-to-secure-password-file-on-windows.html