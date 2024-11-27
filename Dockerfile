FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src /app/src
COPY src/main/resources /app/resources

RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/demo1-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5002

CMD ["java", "-jar", "app.jar"]
