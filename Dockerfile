# =========================
# ETAPA 1 - BUILD
# =========================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia primeiro o pom para aproveitar o cache do Docker
COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline -B

# Copia o código
COPY src ./src

# Compila e gera o JAR
RUN mvn clean package -DskipTests

# Verifica se o JAR foi realmente criado
RUN ls -lah /app/target


# =========================
# ETAPA 2 - EXECUÇÃO
# =========================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Railway utiliza a variável PORT
EXPOSE 8080

# Inicia o Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]