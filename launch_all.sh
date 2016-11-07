#!/bin/sh

rm -rf *db *.log* target/*.log* nohup.out
nohup java -Xmx256m -Xms128m -jar logarchiver-1.0.jar &
nohup java -Xmx128m -Xms64m -jar logtailer-1.0-jar-with-dependencies.jar -f /var/log/dnsmasq.log -s 1000 &