FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /opt/app/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]