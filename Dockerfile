# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/LearnOne-0.0.1-SNAPSHOT.jar /LearnOne.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/LearnOne.jar"]