# =========================
# BUILD
# =========================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom
COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila e gera o JAR
RUN mvn clean package -DskipTests

# Apenas para confirmar no log do Railway
RUN ls -lah /app/target


# =========================
# EXECUÇÃO
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]