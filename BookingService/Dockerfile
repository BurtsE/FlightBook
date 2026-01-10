# Builder stage: use Maven image with JDK 21
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only pom.xml first (to leverage Docker layer caching)
COPY pom.xml .
# If you use a custom settings.xml, uncomment:
# COPY settings.xml /root/.m2/settings.xml

# Download dependencies (optional optimization)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the JAR (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Runtime stage: use minimal JRE
FROM eclipse-temurin:21-jre

# Create non-root user
RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --gid 1001 --uid 1001 appuser

WORKDIR /app

# Copy the built JAR
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]