Dockerfile Examples

Docker commands
docker build -t in28min/hello-world-docker:v1 .


##########=============Dockerfile - 1 - Creating Docker Images=============##########
FROM eclipse-temurin:21-jre-alpine
COPY target/*.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/app.jar"]







########==========Dockerfile - 2 - Build Jar File - Multi Stage==============###########


##====Single Module Project=====##
FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18.0-slim
EXPOSE 5000
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]




##====Multi Module Project=====##
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /home/app

# Copy the entire multi-module project (so parent pom.xml is available)
COPY . .

# Build only this module, but include dependencies (parent POM)
RUN mvn -B -am -pl spring-hello-world-java-service -DskipTests clean package


### === RUNTIME STAGE ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

EXPOSE 5000

# Copy only the compiled JAR of this module
COPY --from=build /home/app/spring-hello-world-java-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

Note:-
mvn -pl spring-hello-world-java-service -am clean package
-pl selects your module
-am automatically builds the parent POM



##########=============Dockerfile - 3 - Caching================########
FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /home/app

COPY ./pom.xml /home/app/pom.xml
COPY ./src/main/java/com/in28minutes/rest/webservices/restfulwebservices/RestfulWebServicesApplication.java	/home/app/src/main/java/com/in28minutes/rest/webservices/restfulwebservices/RestfulWebServicesApplication.java

RUN mvn -f /home/app/pom.xml clean package

COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18.0-slim
EXPOSE 5000
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]