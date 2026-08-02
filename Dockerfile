FROM ubuntu:latest AS build
LABEL authors="Eduar"

WORKDIR /app
COPY . .

RUN apt-get update && \
    apt-get install -y openjdk-25-jdk maven git curl && \
    rm -rf /var/lib/apt/lists/*


COPY src ./src
RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]