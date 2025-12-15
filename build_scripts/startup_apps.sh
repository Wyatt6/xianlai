#!/bin/sh
java -jar /app/xianlai-app-gateway/application.jar --spring.profiles.active=run
java -jar /app/xianlai-app-common/application.jar --spring.profiles.active=run
java -jar /app/xianlai-app-iam/application.jar --spring.profiles.active=run
