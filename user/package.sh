#!/usr/bin/env bash
mvn clean package -Dmaven.test.skip=true -U
docker build -t micro-service-user .
docker push micro-service-user