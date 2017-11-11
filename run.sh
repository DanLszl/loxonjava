#!/bin/bash
cd auralux
mvn clean package
cd ..
java -jar auralux/target/auralux-1.0-SNAPSHOT-jar-with-dependencies.jar | java -jar Competition_Round2.jar
