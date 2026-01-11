FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copie le jar Spring Boot généré par Maven (évite les jar "plain" si jamais)
ARG JAR_FILE=target/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 9999
ENTRYPOINT ["java","-jar","/app/app.jar"]