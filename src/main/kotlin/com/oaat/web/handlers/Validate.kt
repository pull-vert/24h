/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.web.handlers

import com.oaat.web.BadRequestStatusException
import javax.validation.Validator

interface Validate {
    val validator: Validator

    fun callValidator(any: Any) {
        val errors = validator.validate(any)
        if (!errors.isEmpty()) {
            var msg = ""
            errors.forEach { error ->
                msg += "${error.propertyPath}(${error.invalidValue}):${error.message};"
            }
            msg = msg.removeSuffix(";") // remove last ';'
            throw BadRequestStatusException(msg)
        }
    }
}
