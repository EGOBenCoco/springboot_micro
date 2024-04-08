FROM openjdk:17-jdk-alpine
#EXPOSE 8083
ADD target/post-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]