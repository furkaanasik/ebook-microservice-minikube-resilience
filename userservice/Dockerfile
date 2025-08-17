FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

VOLUME /app/data

ENTRYPOINT ["java", "-jar", "app.jar"]