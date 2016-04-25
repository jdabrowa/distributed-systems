#!/usr/bin/env bash

mvn package -DskipTests

if [ ! -d target/dependency ]; then
    mvn dependency:copy-dependencies
fi


java -cp target/ice-middleware-1.0-SNAPSHOT.jar:target/dependency/* pl.jdabrowa.agh.distributed.ice.server.SimpleServer