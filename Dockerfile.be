FROM openjdk:17
COPY ./be/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]