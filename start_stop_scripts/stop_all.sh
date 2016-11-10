#!/bin/bash
# Grabs and kill a process from the pidlist that has the word myapp

pid=`ps aux | grep logarchiver | grep java | awk '{print $2}'`
kill -9 ${pid}

pid=`ps aux | grep logtailer | grep java | awk '{print $2}'`
kill -9 ${pid}