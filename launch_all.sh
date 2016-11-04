#!/bin/sh

rm -rf *log target/*log nohup.out
nohup java -jar logarchiver-1.0-SNAPSHOT.jar &
nohup java -jar logtailer-1.0-SNAPSHOT-jar-with-dependencies.jar -f /var/log/dnsmasq.log -s 1000 &