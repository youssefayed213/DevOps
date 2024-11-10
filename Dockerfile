# Use an official base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the project deliverable (e.g., JAR file or code)
COPY target/Foyer-0.0.1-SNAPSHOT.jar /app/

# Command to run your application
CMD ["java", "-jar", "Foyer-0.0.1-SNAPSHOT.jar"]
