# provisor
This is a Java Spring application to work with autoprovision on Linksys SPA 921/941 and SNR VP-51/52.
Application remaked to a backend REST API format. Client part of this application was enchanced and moved on browser Angular application.

Template of application.properties file:

spring.datasource.url=(SQL database URL in format: jdbc:*database type: postgres or mysql*://*database address*:*database port*/*table name*)

spring.datasource.username=Database user name

spring.datasource.password=Database user password

ecss.url=URL to acces to Eltex ECSS-10

ecss.login=Login to acces to Eltex ECSS-10

ecss.password=Password to acces to Eltex ECSS-10

ecss.domain=Domain with aliases to be management

ecss.group=*

path.snrvp.confs=Path to directory to SNR VP-51/52 configuration files

path.spa.confs=Path to directory to Linksys SPA 921/941 configuration files

path.snrvp.templ=Path to SNR VP-51/52 configuration file template

path.spa.templ=Path to Linksys SPA 921/941 configuration file template

path.snrvp51.init=Path to SNR VP-51 initial config (must be accesable from TFTP)

path.snrvp52.init=Path to SNR VP-52 initial config (must be accesable from TFTP)

path.spa921.init=Path to SPA 921 initial config (must be accesable from TFTP)

path.spa941.init=Path to SPA 941 initial config (must be accesable from TFTP)

tftp.service.name=Name of TFTP service (default: tftpd-hpa.service)

dhcp.service.name=Name of DHCP service (default: isc-dhcp-server.service)

dhcp.service.configuration.path=Path to configuration file of DHCP service

tftp.service.configuration.path=Path to configuration file of TFTP service

spa.admin.name=Linksys SPA 921/941 username

spa.admin.pass=Linksys SPA 921/941 password

spa.digest.realm=spa admin

host.arp.table.path=/proc/net/arp

host.ip.address=Addres of interface in phone's network

view.application.url=URL of view application

#debug

#server.error.include-stacktrace=ALWAYS

#logging.level.org.springframework=DEBUG
