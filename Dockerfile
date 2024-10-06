FROM gradle:8.10.2-jdk21-alpine AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
COPY --from=builder /home/gradle/src/build/libs/camunda_app-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]