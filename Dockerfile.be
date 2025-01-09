FROM openjdk:17
COPY ./be/build/libs/*.jar app.jar
ENTRYPOINT ["java","-Dspring.config.additional-location=file:/be/conf/","-jar","app.jar","--spring.profiles.active=dev"]
