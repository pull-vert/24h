# One At A Time Backend

This project is the Backend of OAAT, a Social Media with short living unique message per user for 24h.
 
## Technical choices

Kotlin routing DSL (based on WebFlux Java functional API) :
* WebFlux
* Router DSL
* Handlers
* Reactor based Reactive Programming
* MongoDB accessed via reactive driver
* reactor-netty webserver
* HTTP/2
* Spring Security (reactive) JWT

## Build with gradle

execute :

gradlew clean bootJar

## Docker

### Build image
To list images :

docker images

To delete the image :

docker rmi oaat

Then

docker build -t oaat:latest .

### Run container from image
First verify that you don't have a container running with

docker ps 

THEN 

docker ps -a

If a container is running (or use the id of the container) :

docker stop oaat

To delete the container (or use the id of the container) :

docker rm oaat

Then :
1) Start mongo (if not already started)

docker run -d -p 27017:27017 --name mongo mongo

2) start spring boot app, with link to mongo

docker run -p 8080:8080 --name oaat --link=mongo oaat
