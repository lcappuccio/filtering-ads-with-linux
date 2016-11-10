# How to filter (some) advertising with linux and dnsmasq

[![Build Status](https://travis-ci.org/lcappuccio/filtering-ads-with-linux.svg?branch=master)](https://travis-ci.org/lcappuccio/filtering-ads-with-linux)
[![codecov](https://codecov.io/gh/lcappuccio/filtering-ads-with-linux/branch/master/graph/badge.svg)](https://codecov.io/gh/lcappuccio/filtering-ads-with-linux)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b6cf8eb58c5c441eaaa52a79b6f91bf7)](https://www.codacy.com/app/lcappuccio/filtering-ads-with-linux?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lcappuccio/filtering-ads-with-linux&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/420cede9-dd38-4629-ac71-0d377143553b)](https://codebeat.co/projects/github-com-lcappuccio-filtering-ads-with-linux)

A brief recap about how to filter (some) ads with a linux box. This will not remove all ads but can greatly help if used with AdBlock, uBlockOrigin, Ghostery, etc.

The idea is very simple and very well documented on the 'net:

*Use dnsmasq to point a list of domains to a local webserver that will replace the ads with a 1px transparent gif.*

To obtain this we will need:
* a linux machine or a raspberry (I'm using a Pi3)
* dnsmasq installed
* a local webserver (I'm using apache)
* a running mysql database

## Installing dnsmasq

This is basically a matter of typing:

`sudo apt-get install dnsmasq`

### Configuring dnsmasq

See `dnsmasq.conf` and `hosts`. On my example I'm also using this machine as DHCP server and resolving some local hostnames. I'm also using google's DNS.

Example `dnsmasq.conf` (**please be sure to disable dhcp logging, otherwise logtailer will crash**)

```
######### dns ########
# Never forward plain names (without a dot or domain part)
domain-needed
# Never forward addresses in the non-routed address spaces
bogus-priv
# dont read resolv.conf   use the defined servers instead
no-resolv
server=8.8.8.8
server=8.8.4.4
# increase dns cache from 512 to 4096
cache-size=4096

######### dhcp ##########
# Add local-only domains here, queries in these domains are answered
# from /etc/hosts or DHCP only
local=/home/
# Set this (and domain: see below) if you want to have a domain
# automatically added to simple names in a hosts-file.
expand-hosts
# adds my localdomain to each dhcp host
domain=home
# my private dhcp range + subnetmask + 14d lease time
dhcp-range=192.168.0.100,192.168.0.254,255.255.255.0,8h
# set route to my local network router
dhcp-option=option:router,192.168.0.1
#windows 7 float fix
#http://brielle.sosdg.org/archives/522-Windows-7-flooding-DHCP-server-with-DHCPINFORM-messages.html
dhcp-option=252,"\n"

###### logging ############
# own logfile
log-facility=/var/log/dnsmasq.log
log-async
# log dhcp infos
# log-dhcp
# debugging dns
log-queries
```

Logrotate needs to set the correct permission on the dnsmasq log file when rotating:

```
/var/log/dnsmasq.log
 {
         monthly
         missingok
         notifempty
         delaycompress
         sharedscripts
         postrotate
                 [ ! -f /var/run/dnsmasq/dnsmasq.pid ] || kill -USR2 `cat /var/run/dnsmasq/dnsmasq.pid`
         endscript
         create 0644 dnsmasq root
 }
 ```

Everything is on domain `home`. After installing and configuring:

`sudo service dnsmasq restart`

## Configure router

On my home modem/router I had to set the new dnsmasq machine as DNS. Remember to disable also the DHCP server, otherwise disable the DHCP on the dnsmasq machine.

## Configure local webserver

Will not get much into detail here, simply install lighttpd, apache, pixelserv or whatever other web server of your choice. Personally I'm using lighttpd.

In `/etc/apache2/sites-enabled/000-default.conf` file:

```
<VirtualHost *:80>
 	# The ServerName directive sets the request scheme, hostname and port that
 	# the server uses to identify itself. This is used when creating
 	# redirection URLs. In the context of virtual hosts, the ServerName
 	# specifies what hostname must appear in the request's Host: header to
 	# match this virtual host. For the default virtual host (this file) this
 	# value is not decisive as it is used as a last resort host regardless.
 	# However, you must set it for any further virtual host explicitly.
 	#ServerName www.example.com
 
 	ServerAdmin webmaster@localhost
 	DirectoryIndex 1px.gif
 	DocumentRoot /var/www/html
 
 	# Available loglevels: trace8, ..., trace1, debug, info, notice, warn,
 	# error, crit, alert, emerg.
 	# It is also possible to configure the loglevel for particular
 	# modules, e.g.
 	#LogLevel info ssl:warn
 
 	ErrorLog ${APACHE_LOG_DIR}/error.log
 	CustomLog ${APACHE_LOG_DIR}/access.log combined
 
 	# For most configuration files from conf-available/, which are
 	# enabled or disabled at a global level, it is possible to
 	# include a line for only one particular virtual host. For example the
 	# following line enables the CGI configuration for this host only
 	# after it has been globally disabled with "a2disconf".
 	#Include conf-available/serve-cgi-bin.conf
 </VirtualHost>
```

This will forward requests to `http://adtrap_ip_address` directly to our 1px gif.

## Configure database

`sudo apt-get install mysql-server` and create the adtrap schema and user.

In order to make things easier I suggest:

`sudo apt-get install phpmyadmin`

## Create the lists

Now for the fun part. Actually I'm not creating anything, just putting together pieces that I've found on the web. Inspired by a post about [pihole](https://pi-hole.net) I started to gather information and create a DIY solution to address the same problem:

* A [post](https://www.reddit.com/r/pihole/comments/4p2tp7/adding_easylist_and_other_adblocklike_sources_to/) on reddit
* Look for google *dnsmasq block ads*

The entrypoint is `manual_easylist.sh`. This bash script will download the lists in `lists.lst`, parse them and put them into another file.

If no web server is running on your local machine replace the `127.0.0.1` on the end of the script with the ip 
of your ad trap web server 1px gif image.

On this same file I'm appending some other hosts found on [URLBlacklist](http://www.urlblacklist.com). After all this parsing and appending the script will sort and remove duplicates.

Finally we will generate `blacklist_dnsmasq.txt`. Copy this file to `/etc/dnsmasq.d` and again restart dnsmasq.

Start browsing and issue
`> sudo tail -f /var/log/dnsmasq.log | grep 127.0.0.1`

And you should see something like this:

`Oct  4 19:40:38 dnsmasq[9669]: config pagead2.googlesyndication.com is 127.0.0.1`

`Oct  4 19:40:38 dnsmasq[9669]: config s7.addthis.com is 127.0.0.1`

So we are redirecting some stuff. :)

## Extras

* If using DHCP I've prepared a php page listing all leases (if you don't want to deploy the java components)
* Bandwidthd monitor (`sudo apt-get install bandwidthd`), useful if using the same box as LAN gateway

# Software (optional)

The adtrap java application is used to listen the dnsmasq logfile and store events in a MySQL database. (should work 
with MariaDb as well).

Compile with: 

`mvn clean package spring-boot:repackage`

It is a java Springboot REST backend with a monitoring console using Google Charts. 

See `application.properties` to configure.

## init, launch and stop scripts

Use `launch.sh` and `stop.sh` in the folder where the jar was copied.

Create a folder `config` and copy (with the opportune values) the `application.properties` configuration file.

## Web Console

### DHCP Information / Top Clients

![DHCP Information / Top Clients](goodies/images/clients.png?raw=true)

### Top Requests

![Top Requests](goodies/images/domains.png?raw=true)

### Top Advertisers

![Top Advertisers](goodies/images/advertisers_trapped.png?raw=true)

### Statistics

![Statistics](goodies/images/advertiser_statistics.png?raw=true)

### System Monitoring Information

![System Monitoring Information](goodies/images/monitor.png?raw=true)

## System Monitoring

Spring actuators are deployed, see the included postman collection to extend the current system monitoring interface.

## REST endpoints

See the included postman collection, additional endpoints can be added by:

1. Extend `org.systemexception.adtrap.service.DataService`
2. Implement method in `org.systemexception.adtrap.service.MySqlDataService`
3. Add REST endpoint in `org.systemexception.adtrap.controller.RestController`
4. Extend tests accordingly

To transform new endpoints to fancy charts:

1. Create a new javascript class
2. Create a new webpage
3. Extend the menu to include your new page

## Database queries

Connect to your database with IDE of choice and see queries : 

```
-- Total
select count(*)
from DNS_LOG_LINES
order by LOG_TIME desc
go

-- Group by query type
SELECT QUERY_TYPE, count(*) as TOTAL
FROM DNS_LOG_LINES
GROUP BY QUERY_TYPE
ORDER BY 2 DESC
go

-- Check top requesters
select QUERY_TARGET, count(*) as TOTAL
from DNS_LOG_LINES
where QUERY_TYPE = 'query[A]'
group by QUERY_TARGET
order by 2 desc
go

-- Check top requested domains
SELECT QUERY_DOMAIN, count(*) as TOTAL
FROM DNS_LOG_LINES
GROUP BY QUERY_DOMAIN
ORDER BY 2 DESC
go

-- Check top advertisers filtered
select QUERY_DOMAIN, count(*) as TOTAL
from DNS_LOG_LINES
where QUERY_TARGET = '192.168.0.4'
group by QUERY_DOMAIN
order by 2 desc
//limit 20
go

-- Count advertisers
select count(*) as TOTAL
from DNS_LOG_LINES
where QUERY_TARGET = '192.168.0.4'
go

-- Count advertisers per hour
select FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y %H') as LOG_DATE, count(*) as TOTAL
from DNS_LOG_LINES
where QUERY_TARGET = '192.168.0.4'
and STR_TO_DATE(FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y'), '%d/%m/%Y') > CURRENT_DATE - INTERVAL 24 hour
group by FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y %H')
order by 1 desc
go

-- Count advertisers per day
select FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y') as LOG_DATE, count(*) as TOTAL
from DNS_LOG_LINES
where QUERY_TARGET = '192.168.0.4'
and STR_TO_DATE(FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y'), '%d/%m/%Y') > CURRENT_DATE - INTERVAL 30 DAY
group by FROM_UNIXTIME(LOG_TIME/1000, '%d/%m/%Y')
order by 1 desc
go

-- Table size
SELECT 
table_name AS `Table`, round(((data_length + index_length) / 1024 / 1024), 2) `Size in MB` 
FROM INFORMATION_SCHEMA.TABLES 
WHERE table_schema = "adtrap"
go
```

Application queries in: `org.systemexception.adtrap.pojo.Queries`

## ToDo

* How to handle HTTPS requests
* Integrate logtailer in logarchiver and deploy as single application(?)
* Installation script
* list creation frontend