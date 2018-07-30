FROM openjdk:8

# Add Friend Service Application
ADD build/libs/friend-service-0.0.1-SNAPSHOT.jar /app/friend-service-0.0.1-SNAPSHOT.jar
ADD entrypoint.sh /bin/entrypoint.sh

WORKDIR /app

EXPOSE 8080

CMD ["/bin/entrypoint.sh"]