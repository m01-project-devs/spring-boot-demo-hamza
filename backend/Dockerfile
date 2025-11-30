FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY target/*.jar app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]