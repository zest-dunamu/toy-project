FROM adoptopenjdk/openjdk14-alpine

COPY build/libs/toy-project-0.0.1-SNAPSHOT.jar ./APP.jar

EXPOSE 8080

CMD ["java", "-jar", "APP.jar"]