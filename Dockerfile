FROM openjdk:21

COPY target/desafio-btg-pactual-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app/app.jar"]