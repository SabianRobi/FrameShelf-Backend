# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre

# Copy the JAR from build stage
COPY --from=build target/*.jar FrameShelf-Backend.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/FrameShelf-Backend.jar"]
