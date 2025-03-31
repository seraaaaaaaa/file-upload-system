FROM openjdk:17-jdk-alpine
COPY target/upload-0.0.1.jar upload-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "upload-0.0.1.jar"]