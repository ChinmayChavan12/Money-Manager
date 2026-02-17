# Stage 1: Build the JAR using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Run the maven build command to create the JAR
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR from the 'build' stage (not your local machine)
COPY --from=build /app/target/MoneyManager-0.0.1-SNAPSHOT.jar moneymanager-v1.0.jar

EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "moneymanager-v1.0.jar"]