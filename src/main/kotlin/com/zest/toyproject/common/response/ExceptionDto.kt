package com.zest.toyproject.common.response

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ExceptionDto(val error: HttpStatus, val error_description: String?, val at: LocalDateTime = LocalDateTime.now())