/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.at.a.time.web

import org.springframework.web.reactive.function.client.WebClient
import java.util.*

private fun buildAutorizationHeader(): String {
    val auth = "sdeleuze:password"
    val encodedAuth = String(Base64.getEncoder().encode(auth.toByteArray(Charsets.UTF_8)));
    println("generating base64 basic $encodedAuth")
    return "basic $encodedAuth"
}

fun WebClient.RequestHeadersSpec<*>.addAuthHeader() =
        this.header("Authorization", buildAutorizationHeader())