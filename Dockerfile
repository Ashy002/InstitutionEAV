# Étape 1 : Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Run
FROM openjdk:17-jdk-slim
WORKDIR /app
# On récupère le JAR généré (school-management-0.0.1-SNAPSHOT.jar)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Limitation de mémoire pour économiser tes 5$
ENTRYPOINT ["java", "-Xmx256m", "-Xms256m", "-jar", "app.jar"]
