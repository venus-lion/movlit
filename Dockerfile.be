FROM openjdk:17
COPY ./be/build/libs/*.jar app.jar
ENTRYPOINT ["java", \
    "-Dspring.config.location=file:/be/conf/", \
    "-jar", "app.jar", \
    "--spring.profiles.active=elastic,mysql,mongodb,redis,oauth,aws,jwt"]