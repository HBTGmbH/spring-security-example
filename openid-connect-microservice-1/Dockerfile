FROM openjdk:12-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./entrypoint.sh /
RUN chmod 755 /entrypoint.sh
CMD /entrypoint.sh
