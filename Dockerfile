FROM eclipse-temurin:25-jre-alpine
LABEL authors="Eduar"

WORKDIR /app
COPY . .

RUN apt-get update && \
    apt-get install -y openjdk-25-jdk maven git curl && \
    rm -rf /var/lib/apt/lists/*


COPY src ./src
RUN mvn clean package -DskipTests -B

OM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]