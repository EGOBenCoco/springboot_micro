FROM openjdk:17-jdk-alpine
#EXPOSE 8888
ADD target/subscriber-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]