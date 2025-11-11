##
## Build stage
##
#FROM maven:3.8.1-openjdk-17-slim AS build
#COPY src /app/src
#COPY pom.xml /app
#RUN mvn -f /app/pom.xml clean package -DskipTests
#
##
## Package stage
##
#FROM openjdk:17-jdk-slim
#COPY --from=build /app/target/finplan-0.0.1-SNAPSHOT.jar /usr/local/lib/finplan.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/usr/local/lib/finplan.jar"]

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/finplan-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
