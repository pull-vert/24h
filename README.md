# Spring Kotlin deepdive

This project is designed to show step by step how to migrate from Java to Kotlin with
Spring Boot step by step:
 * [Step 0](https://github.com/sdeleuze/spring-kotlin-deepdive/): Initial Java project
 * [Step 1](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step1): Java to Kotlin
 * [Step 2](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step2): Spring Boot 2
 * [Step 3](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step3): Spring WebFlux
 * [Step 4](https://github.com/sdeleuze/spring-kotlin-deepdive/tree/step4): Kotlin routing DSL
 
See [Spring Kotlin support documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/languages.html#kotlin) for more details.
 
## Step 4: Kotlin routing DSL

* Kotlin routing DSL is based on WebFlux Java functional API
* Only available for WebFlux, not MVC
* Router DSL
* Handlers

##Build with gradle

execute **gradlew clean bootJar**

##Docker

###Build image
To list images :
docker images
To delete the image :
docker rmi 24h-back
Then
docker build -t 24h-back .

###Run image
First verify that you don't have a container ruuning with
docker ps THEN docker ps -a
If a container is running :
docker stop 24h-back
To delete the container :
docker rm 24h-back (or the id of the container)
Then :
docker run --name 24h-back -p 8080:8080 -t 24h-back

**The end!**

If you want to go even further with Kotlin, it can be used for Gradle build via its Kotlin
DSL and for frontend / multiplatform code, see [spring-kotlin-fullstack](https://github.com/sdeleuze/spring-kotlin-fullstack)
example project.

