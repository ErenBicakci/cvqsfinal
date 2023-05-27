

FROM maven:3-openjdk-17-slim as build
COPY target/management-0.0.1-SNAPSHOT.jar /
EXPOSE 8282
CMD exec java -jar management-0.0.1-SNAPSHOT.jar