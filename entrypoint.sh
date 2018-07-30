#!/usr/bin/env bash

echo "SPRING_PROFILES_ACTIVE ===== $SPRING_PROFILES_ACTIVE"
echo "MONGO_URL  ===== $MONGO_URL"

/bin/sh -c "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Djava.security.egd=file:/dev/./urandom -jar friend-service-0.0.1-SNAPSHOT.jar"
