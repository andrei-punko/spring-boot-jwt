FROM openjdk:17-jdk-alpine
VOLUME /tmp
EXPOSE 9091
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD build/libs/resource-server-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
