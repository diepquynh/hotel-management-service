FROM openjdk:11-jre-slim

ARG API_GW_VERSION=0.0.1
ARG API_GW_REVISION=SNAPSHOT

COPY ./target/api-gateway-$API_GW_VERSION-$API_GW_REVISION.jar /usr/local/lib/api-gateway.jar

CMD ["java","-jar","/usr/local/lib/api-gateway.jar"]
