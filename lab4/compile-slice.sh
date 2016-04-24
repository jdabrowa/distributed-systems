#!/bin/bash

if [ ! -d target/generated-sources ]; then
    mkdir target/generated-sources
fi;

if [ ! -d generated ]; then
    mkdir generated
fi

if [ ! -d generated/sources ]; then
    mkdir generated/sources
fi

slice2java src/main/slice/ex1.ice --meta java:package:pl.jdabrowa.agh.distributed.ice.generated --output-dir generated/sources