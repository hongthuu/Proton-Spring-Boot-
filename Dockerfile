# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set the working directory in container
WORKDIR /app

# Copy the jar file into the container
COPY target/JavaSpring-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
