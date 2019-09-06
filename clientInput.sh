#!/bin/bash
echo $1
ls
cd target/classes
java -classpath target/team01-1.0-SNAPSHOT-jar-with-dependencies.jar com.razdolbai.client.Client $1

