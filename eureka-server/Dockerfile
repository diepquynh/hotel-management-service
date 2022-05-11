FROM openjdk:11-jre-slim

ARG EUREKA_SERVER_VERSION=0.0.1
ARG EUREKA_SERVER_REVISION=SNAPSHOT

COPY ./target/eureka-server-$EUREKA_SERVER_VERSION-$EUREKA_SERVER_REVISION.jar /usr/local/lib/eureka-server.jar

CMD ["java","-jar","/usr/local/lib/eureka-server.jar"]
