#!/bin/bash
##this file to start concurrServer.jar and concurrClient.jar

count0='ps -ef|grep concurrClient.jar |grep java |wc -l'
if [$count0 -ne 0];then
	 echo "the concurrClient.jar has been up "
else 
        java -jar concurrClient.jar -Xms512m -Xmx1024m > concurrClient.out &
        ps -ef|grep java |grep concurrClient.jar
fi

count1='ps -ef|grep concurrServer.jar |grep java |wc -l'
if [$count1 -ne 0];then
	echo "the concurrServer.jar has been up "
else
	java -jar concurrServer.jar -Xms512m -Xmx1024m > concurrServer.out &
	ps -ef|grep java |grep concurrServer.jar
fi
