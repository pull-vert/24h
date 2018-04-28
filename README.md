# One At A Time Backend

## Inspirations

[Spring reactive application with Spring security](https://www.codementor.io/hantsy/build-a-reactive-application-with-angular-5-and-spring-boot-2-0-fv8uif7wg)

[Spring Kotlin support documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/languages.html#kotlin)
 
## Architecture

* Kotlin routing DSL for REST API (based on WebFlux Java functional API)
* Reactive MongoDB driver
* End to end use of Reactor (Mono & Flux)
* Spring Security (reactive) & Spring session in MongoDB

## Build with gradle

execute :

gradlew clean bootJar

## Docker

### Build image
To list images :

docker images

To delete the image :

docker rmi 24h-back

Then

docker build -t 24h-back:latest .

### Run container from image
First verify that you don't have a container running with
docker ps 

THEN docker ps -a

If a container is running (or use the id of the container) :

docker stop 24h-back

To delete the container (or use the id of the container) :

docker rm 24h-back

Then :
1) Start mongo (if not already started)

docker run -d -p 27017:27017 --name mongo mongo

2) start spring boot app, with link to mongo

docker run -p 8080:8080 --name 24h-back --link=mongo 24h-back

