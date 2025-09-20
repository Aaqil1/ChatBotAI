FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY sentiment-service/pom.xml sentiment-service/pom.xml
RUN mvn -pl sentiment-service -am dependency:go-offline
COPY sentiment-service/src sentiment-service/src
RUN mvn -pl sentiment-service -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/sentiment-service/target/sentiment-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
