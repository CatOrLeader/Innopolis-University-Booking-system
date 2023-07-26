# syntax=docker/dockerfile:1

#
# BUILD STAGE
#
FROM gradle AS build
COPY --chown=gradle:gradle src /home/gradle/src
COPY build.gradle /home/gradle
COPY settings.gradle /home/gradle
WORKDIR /home/gradle
RUN gradle clean build shadowJar --no-daemon

#
# PACKAGE STAGE
#
FROM openjdk:19-alpine
WORKDIR /app
ENV JAR_FILE=IUBookingBot-1.0-SNAPSHOT-all.jar
COPY --from=build /home/gradle/build/libs/${JAR_FILE} /app/main.jar
CMD ["java", "-jar", "/app/main.jar"]