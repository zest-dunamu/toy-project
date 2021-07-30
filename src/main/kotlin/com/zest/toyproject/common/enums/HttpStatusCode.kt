package com.zest.toyproject.common.enums

import org.springframework.http.HttpStatus

interface HttpStatusCode {
    val code: HttpStatus

    val value: String
}