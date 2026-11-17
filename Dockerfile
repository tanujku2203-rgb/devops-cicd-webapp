
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/devops-cicd-webapp-1.0.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
