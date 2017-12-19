# working, 8-jre-slim image = 205MB
#FROM openjdk:8-jre-slim
# working, fat 8-jdk image = 737MB
#FROM openjdk:8-jdk
#not working with embedded Mongo, slim image = 102MB
FROM openjdk:8-jdk-alpine
COPY /build/libs/24h-0.0.1-SNAPSHOT.jar /home/24h.jar

# Specific to make alpine work with embbeded Mongo
#RUN apk update && apk add libstdc++ && rm -rf /var/cache/apk/*

CMD ["java","-jar","/home/24h.jar"]
