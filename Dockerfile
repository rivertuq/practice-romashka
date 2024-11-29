FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/myproducts-0.0.1-SNAPSHOT.jar /app/myproducts-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/myproducts-0.0.1-SNAPSHOT.jar"]
