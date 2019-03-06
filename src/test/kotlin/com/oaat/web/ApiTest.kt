/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.oaat.entities.Role
import com.oaat.entities.Role.ROLE_ADMIN
import com.oaat.entities.Role.ROLE_USER
import com.oaat.entities.User
import com.oaat.security.JWTUtil
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.ExchangeStrategies
import reactor.netty.http.client.HttpClient


internal typealias ServerResponseError = Map<String, Any>

@ExtendWith(
        RestDocumentationExtension::class,
        SpringExtension::class
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(
        uriScheme = "https",
        uriPort = 8443
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal abstract class ApiTest {

    protected lateinit var client: WebTestClient
    private lateinit var jwtUtil: JWTUtil

    @BeforeAll
    fun before(
            @Autowired jwtUtil: JWTUtil,
            @Autowired restDocumentation: RestDocumentationContextProvider,
            @Autowired objectMapper: ObjectMapper,
            @LocalServerPort port: Int
    ) {
        this.jwtUtil = jwtUtil
        val sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        val httpClient = HttpClient.create().secure{ t -> t.sslContext(sslContext) }
        val httpConnector = ReactorClientHttpConnector(httpClient)

        client = WebTestClient.bindToServer(httpConnector)
                .baseUrl("https://localhost:$port")
                .exchangeStrategies(
                        ExchangeStrategies.builder().codecs { codecs ->
                            val defaults = codecs.defaultCodecs()
                            defaults.jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                            defaults.jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
                        }.build())
                .filter(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build()
    }

    private fun buildAuthHeader(role: Role): String {
        val user = when(role) {
            ROLE_USER -> User("Fred", "password", authorities = mutableListOf(ROLE_USER), enabled = true)
            ROLE_ADMIN -> User("Boss", "secured_password", authorities = mutableListOf(ROLE_ADMIN), enabled = true)
        }
        val jwtToken = jwtUtil.generateToken(user)
        println("generating jwt token : $jwtToken")
        return "Bearer $jwtToken"
    }

    /**
     * Extension function to [WebTestClient.RequestHeadersSpec] allowing to add
     * a jwt token HTTP Header -> Authorization : Bearer JwtToken
     * Can be used only in Tests extending [ApiTest]
     */
    protected fun WebTestClient.RequestHeadersSpec<*>.addAuthHeader(role: Role = ROLE_USER) =
            this.header(HttpHeaders.AUTHORIZATION, buildAuthHeader(role))

    protected class ConstrainedFields internal constructor(input: Class<*>) {

        private val constraintDescriptions = ConstraintDescriptions(input)

        internal fun withPath(path: String) =
                fieldWithPath(path).attributes(key("constraints").value(
                        StringUtils.collectionToDelimitedString(
                                this.constraintDescriptions.descriptionsForProperty(path), "\n", "* ", "")))
    }
}
