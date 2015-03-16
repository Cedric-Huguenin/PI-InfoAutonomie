#!/bin/bash
# Grabs and kill a process from the pidlist that has the word myapp

pid=`ps aux | grep EnoPush | awk '{print $2}'`
kill $pid
if [[ -f "/var/lock/LCK..ttyAMA0" ]]
then
	rm /var/lock/LCK..ttyAMA0
fi
