# FROM openjdk:8-jdk-alpine
# VOLUME /tmp
# ARG JAR_FILE
# COPY ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM openjdk:8-jdk-alpine
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/ImmunizationTrackerAPI-0.1.0.jar"]