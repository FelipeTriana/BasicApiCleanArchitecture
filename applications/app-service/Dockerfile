# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container to /app
WORKDIR /app

# Add the project's jar file to the container
ADD ./build/libs/applications-app-service-0.0.1.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","/app/app.jar"]