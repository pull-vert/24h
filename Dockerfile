# working, 8-jre-slim image = 205MB
#FROM openjdk:8-jre-slim
#not working with embedded Mongo, slim image = 102MB
FROM openjdk:8-jdk-alpine
ADD /build/libs/oaat-0.0.1-SNAPSHOT.jar /home/oaat.jar

# Specific to make alpine work with embbeded Mongo (not sufficient)
#RUN apk update && apk add libstdc++ && rm -rf /var/cache/apk/*

ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo/test","-Djava.security.egd=file:/dev/./urandom","-jar","/home/oaat.jar"]
