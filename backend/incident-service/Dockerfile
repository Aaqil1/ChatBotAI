FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY incident-service/pom.xml incident-service/pom.xml
RUN mvn -pl incident-service -am dependency:go-offline
COPY incident-service/src incident-service/src
RUN mvn -pl incident-service -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/incident-service/target/incident-service-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
