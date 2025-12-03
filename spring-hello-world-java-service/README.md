Dockerfile Examples

Docker commands
docker build -t in28min/hello-world-docker:v1 .
docker container run -d -p 5000:5000 in28min/hello-world-docker:v3






wsl -d Ubuntu
sudo apt update
docker version
docker info
cd /mnt/c/workspace/17-11-2024/SpringFramework/springboot-currency-microservice/spring-hello-world-java-service
mvn clean package spring-boot:build-image

docker run -d -p 9411:9411 openzipkin/zipkin:2.23
docker run -d -p 9411:9411 openzipkin/zipkin:latest












##########=========================Dockerfile - 1 - Creating Docker Images=========================##########
FROM eclipse-temurin:21-jre-alpine
COPY target/*.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/app.jar"]







########===================Dockerfile - 2 - Build Jar File - Multi Stage=========================###########


##====================Single Module Project=======================##
FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:21-jre-alpine
EXPOSE 5000
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]




##=================Multi Module Project============##
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

#### Note  ####
mvn -pl spring-hello-world-java-service -am clean package
-pl selects your module
-am automatically builds the parent POM

### command ###
docker build -f spring-hello-world-java-service/Dockerfile -t in28min/hello-world-docker:v3 .









##########=======================================Dockerfile - 3 - Caching==========================================########


##====================Single Module Project======================##
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





##==================Multi Module Project================##
### === BUILD STAGE ===
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /home/app

# -------------------------
# 1) Copy only project-level POMs for dependency caching
# -------------------------
COPY pom.xml .
COPY spring-hello-world-java-service/pom.xml spring-hello-world-java-service/
COPY springboot-limit-service/pom.xml springboot-limit-service/
COPY spring-cloud-config-server/pom.xml spring-cloud-config-server/
COPY currency-exchange-service/pom.xml currency-exchange-service/
COPY currency-conversion-service/pom.xml currency-conversion-service/
COPY naming-server/pom.xml naming-server/
COPY api-gateway/pom.xml api-gateway/


# Pre-fetch dependencies
RUN mvn -B dependency:go-offline


# -------------------------
# 2) Copy only the main application file you specified
#    (This will NOT break caching of dependencies)
# -------------------------
COPY spring-hello-world-java-service/src/main/java/com/rest/webservices/restfulwebservices/RestfulWebServicesApplication.java \
     /home/app/src/main/java/com/rest/webservices/restfulwebservices/RestfulWebServicesApplication.java


# -------------------------
# 3) Copy full source AFTER caching layers
# -------------------------
COPY . .


# -------------------------
# 4) Run your requested Maven build command
# -------------------------
RUN mvn -f /home/app/pom.xml clean package -DskipTests


### === RUNTIME STAGE ===
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
EXPOSE 5000

COPY --from=build /home/app/spring-hello-world-java-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
