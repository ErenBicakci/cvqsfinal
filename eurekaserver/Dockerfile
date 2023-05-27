

FROM maven:3-openjdk-17-slim as build
COPY target/eurekaserver-0.0.1-SNAPSHOT.jar /
EXPOSE 8761
CMD exec java -jar eurekaserver-0.0.1-SNAPSHOT.jar