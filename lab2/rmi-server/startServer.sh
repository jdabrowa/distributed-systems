#!/usr/bin/env bash

if [ "$#" -ne 0 ]; then
    mvn clean package
    mvn dependency:copy-dependencies
fi

#pkill -f rmiregistry
#cd target
#rmiregistry 1099 &
#cd -

CODEBASE_JAR=/home/yorg/Desktop/Rozprochy/lab/homeworks/lab2/rmi-server/target/lab2-rmi-server-1.0-SNAPSHOT.jar

if [ -f ${CODEBASE_JAR} ]; then
    echo "File exists"
fi

RMI_CODEBASE=file:${CODEBASE_JAR}
MAIN_CLASS=pl.jdabrowa.distributed.GameServerMain
CLASSPATH=target/dependency/*:target/lab2-rmi-server-1.0-SNAPSHOT.jar

java -cp ${CLASSPATH} ${MAIN_CLASS}