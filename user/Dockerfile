FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copie le jar Spring Boot généré par Maven
ARG JAR_FILE=target/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]