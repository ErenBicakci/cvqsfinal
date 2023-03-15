FROM maven:3-openjdk-17-slim as build
COPY target/auth-0.0.1-SNAPSHOT.jar /
EXPOSE 8181
CMD exec java -jar auth-0.0.1-SNAPSHOT.jar