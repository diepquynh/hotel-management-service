FROM openjdk:11-jre-slim

ARG USER_SERVICE_VERSION=0.0.1
ARG USER_SERVICE_REVISION=SNAPSHOT

COPY ./target/user-service-$USER_SERVICE_VERSION-$USER_SERVICE_REVISION.jar /usr/local/lib/user-service.jar
CMD ["java","-jar","/usr/local/lib/user-service.jar"]
