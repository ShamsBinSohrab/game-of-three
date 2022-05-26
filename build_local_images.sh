#!/bin/sh

echo "Building image for player 1"
  case "$(uname -p)" in
   arm) ./mvnw clean compile jib:dockerBuild -- -Djib-maven-plugin.architecture=arm64 -f player-1/pom.xml ;;
   *) ./mvnw clean compile jib:dockerBuild -f player-1/pom.xml ;;
  esac

echo "Building image for player 2"
  case "$(uname -p)" in
   arm) ./mvnw clean compile jib:dockerBuild -- -Djib-maven-plugin.architecture=arm64 -f player-2/pom.xml ;;
   *) ./mvnw clean compile jib:dockerBuild -f player-2/pom.xml ;;
  esac

echo "Completed!"
