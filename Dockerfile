FROM maven:3.9.9-eclipse-temurin-21-alpine as build
WORKDIR /prices

COPY prices/pom.xml .
RUN mvn dependency:go-offline

COPY prices/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine as runtime

COPY --from=build /prices/target/prices-0.0.1-SNAPSHOT.jar /prices/prices.jar

EXPOSE 8080

CMD ["java", "-jar", "/prices/prices.jar"]
