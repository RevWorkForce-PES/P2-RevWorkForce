# Deployment Guide

## 1. Environment Preparation
The RevWorkForce application is a standard Spring Boot application packaged into an executable JAR.

### Requirements:
- Java Runtime Environment (JRE) or JDK 17+
- Relational Database Instance (MySQL 8+ recommended natively)
- Sufficient memory allocation (Recommended 1GB RAM minimum)

## 2. Application Properties Configuration
Before generating the deployment payload, ensure `application-prod.properties` is configured, or prepare Environment Variables to override properties dynamically.

Required environment variables for secure deployment:
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:<port>/<db_name>
SPRING_DATASOURCE_USERNAME=<db_user>
SPRING_DATASOURCE_PASSWORD=<secure_password>
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=<secure_base64_encoded_random_string>
JWT_EXPIRATION_MS=86400000
```

## 3. Generating the Build Payload
Compile and package the application using the local Maven wrapper.
```bash
# Navigate to the project root
cd /path/to/P2-RevWorkForce

# Clean target mappings and package an executable JAR omitting active tests routines
mvn clean package -DskipTests
```
The output will reside within the `target/` directory formatted as `revworkforce-1.0.0.jar`.

## 4. Execution Strategies

### Standard Execution (Systemd or Unix Daemon)
You can invoke the application directly via Java.
```bash
java -jar -Xms512m -Xmx1024m target/revworkforce-1.0.0.jar --spring.profiles.active=prod
```

### Docker Execution (Containerized)
A standard `Dockerfile` setup:
```dockerfile
# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the fat jar into the container at /app
COPY target/revworkforce-*.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
```

Build and execute the container:
```bash
docker build -t revworkforce:latest .
docker run -d -p 8080:8080 --name hrms-app --env-file .env revworkforce:latest
```

## 5. Post Deployment Context
Always establish an embedded Reverse Proxy (`Nginx` or `Apache`) mapping routing port 80/443 pointing internally to 8080. This enables HTTPS bindings effectively wrapping the sensitive HR payload securely.
