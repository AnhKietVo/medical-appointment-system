FROM amazoncorretto:22

WORKDIR /app

COPY target/Appointment.jar Appointment.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Appointment.jar"]
