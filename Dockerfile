FROM maven:3.9.4-eclipse-temurin-21-alpine as build
WORKDIR /app
COPY pom.xml /app/
RUN mvn dependency:go-offline

COPY src /app/src/
RUN mvn clean package -DskipTests
RUN ls -al /app/target/

FROM openjdk:21-slim
VOLUME /tmp
COPY --from=build /app/target/*.jar /trackmycoin.jar

ENTRYPOINT ["java", "-jar", "/trackmycoin.jar"]
