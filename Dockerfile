FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /backend
COPY clinic .

RUN mvn -DskipTests clean package

# Runtime image
FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY --from=build /backend/target/*.jar app.jar

EXPOSE 8778
CMD ["java", "-jar", "app.jar"]
