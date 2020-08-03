FROM openjdk:8-jdk-alpine
WORKDIR /
COPY FiveInARow-0.0.1-SNAPSHOT.jar app.jar
COPY conf.yaml conf.yaml
EXPOSE 8888
CMD ["java", "-jar", "app.jar", "server", "conf.yaml"]

