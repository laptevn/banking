#!/bin/bash

cd account
mvn clean install
mvn jib:dockerBuild

cd ../bank
mvn clean install
mvn jib:dockerBuild

cd ../gateway
mvn clean install
mvn jib:dockerBuild

cd ../configuration
mvn clean install
mvn jib:dockerBuild