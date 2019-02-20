/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

internal class BadRequestStatusException(reason: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, reason)
internal class UnauthorizedStatusException : ResponseStatusException(HttpStatus.UNAUTHORIZED)
internal class NotFoundStatusException(reason: String? = null) : ResponseStatusException(HttpStatus.NOT_FOUND, reason)
