# Stage 1: Build with Maven & JDK 17
FROM maven:3.9.6-amazoncorretto-17 as builder

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Stage 2: Run with JDK 17
FROM amazoncorretto:17

WORKDIR /app
COPY --from=builder /app/target/Appointment.jar Appointment.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Appointment.jar"]
