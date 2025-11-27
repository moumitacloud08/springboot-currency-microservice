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
