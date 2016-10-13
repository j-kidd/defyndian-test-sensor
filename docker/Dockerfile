FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY TestSensor.conf TestSensor.conf
COPY defyndian.conf defyndian.conf
COPY nodes.properties nodes.properties
COPY defyndian-test-sensor-1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]