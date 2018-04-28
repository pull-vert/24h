# One At A Time Backend

This project is a Social Media with short living unique message per user for 24h.
 
## Technical choices

Kotlin routing DSL (based on WebFlux Java functional API) :
* Only available for WebFlux, not MVC
* Router DSL
* Handlers
* Reactor based Reactive Programming
* MongoDB accessed via reactive driver
* reactor-netty webserver

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

## Inspirations
 * [spring-kotlin-deepdive](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step4): Kotlin routing DSL
 * [Spring Kotlin support documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/languages.html#kotlin) for more details.
 * [Spring reactive application with Spring security](https://www.codementor.io/hantsy/build-a-reactive-application-with-angular-5-and-spring-boot-2-0-fv8uif7wg)