#!/bin/sh

echo "Building image for player 1"
./mvnw clean compile jib:dockerBuild -f player-1/pom.xml

echo "Building image for player 2"
./mvnw  clean compile jib:dockerBuild -f player-2/pom.xml

echo "Completed!"
