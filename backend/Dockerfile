FROM amazoncorretto:22

LABEL version="1.0"

EXPOSE 8080

WORKDIR /app

COPY Appoiment/target/Appoiment-0.0.1-SNAPSHOT.jar /app/Appoiment.jar

ENTRYPOINT ["java", "-jar", "Appoiment.jar"]

