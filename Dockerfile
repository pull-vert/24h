FROM openjdk:8-jdk
#FROM openjdk:8-jdk-alpine
COPY /build/libs/24h-0.0.1-SNAPSHOT.jar /home/24h.jar
#RUN apk update && apk add libstdc++ && rm -rf /var/cache/apk/*
CMD ["java","-jar","/home/24h.jar"]
