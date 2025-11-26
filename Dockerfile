FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY target/api-cardaio-0.0.1-SNAPSHOT.jar app.jar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
