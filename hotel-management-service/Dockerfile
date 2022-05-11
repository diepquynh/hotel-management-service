FROM openjdk:11-jre-slim

ARG HOTELMANAGER_VERSION=0.0.1
ARG HOTELMANAGER_REVISION=SNAPSHOT

COPY ./target/hotelmanager-$HOTELMANAGER_VERSION-$HOTELMANAGER_REVISION.jar /usr/local/lib/hotelmanager.jar
CMD ["java","-jar","/usr/local/lib/hotelmanager.jar"]
