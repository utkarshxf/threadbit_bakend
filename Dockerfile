FROM ubuntu:latest
LABEL authors="utkarsh"
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar" , "/app.jar"]
EXPOSE 8080