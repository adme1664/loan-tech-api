# Build stage
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /workspace/app

# Copy Maven wrapper and pom.xml first for better caching
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:25-jre-alpine
VOLUME /tmp

# Add a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the extracted application layers
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Expose the application port
EXPOSE 9091

# Run the application
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.adme.learning.loan.tech.api.LoanTechApiApplication"]