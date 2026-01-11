FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copie le jar Spring Boot construit par Maven
ARG JAR_FILE=target/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]