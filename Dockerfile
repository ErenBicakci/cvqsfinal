FROM maven:3-openjdk-17-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
COPY target/management-0.0.1-SNAPSHOT.jar /
EXPOSE 8080
ADD target/management-0.0.1-SNAPSHOT.jar management.jar
CMD exec java -jar management-0.0.1-SNAPSHOT.jar