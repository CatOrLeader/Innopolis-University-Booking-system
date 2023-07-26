# syntax=docker/dockerfile:1

#
# BUILD STAGE
#
FROM maven AS build
COPY src /home/mock/src
COPY pom.xml /home/mock
RUN mvn -f /home/mock/pom.xml clean package

#
# PACKAGE STAGE
#
FROM openjdk:19-alpine
ENV JAR_FILE=swagger-spring-1.0.0.jar
COPY --from=build /home/mock/target/${JAR_FILE} /home/main.jar
EXPOSE 8080
CMD ["java", "-jar", "/home/main.jar", "-b"]