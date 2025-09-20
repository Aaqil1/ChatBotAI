FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY notification-service/pom.xml notification-service/pom.xml
RUN mvn -pl notification-service -am dependency:go-offline
COPY notification-service/src notification-service/src
RUN mvn -pl notification-service -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/notification-service/target/notification-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
