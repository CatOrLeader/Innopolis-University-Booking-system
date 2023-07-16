# syntax=docker/dockerfile:1

#
# BUILD STAGE
#
FROM maven:3.9.2 AS build
COPY src /home/bot/src
COPY pom.xml /home/bot
COPY dockersys /home/bot
RUN mvn -f /home/bot/pom.xml clean compile assembly:single

#
# PACKAGE STAGE
#
FROM openjdk:19-alpine
ENV JAR_FILE=IUBookingBot-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY --from=build /home/bot/target/${JAR_FILE} /home/main.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/main.jar"]