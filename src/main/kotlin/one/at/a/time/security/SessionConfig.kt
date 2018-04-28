package one.at.a.time.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.mongo.config.annotation.web.reactive.EnableMongoWebSession
import org.springframework.web.server.session.HeaderWebSessionIdResolver
import org.springframework.web.server.session.WebSessionIdResolver



@Configuration
@EnableMongoWebSession
class SessionConfig {

    @Bean
    fun webSessionIdResolver(): WebSessionIdResolver {
        val resolver = HeaderWebSessionIdResolver()
        resolver.headerName = "X-AUTH-TOKEN"
        return resolver
    }
}