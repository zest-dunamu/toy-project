package com.zest.toyproject.common.exceptions

import org.springframework.http.HttpStatus
import java.sql.Timestamp
import java.time.LocalDateTime

data class ExceptionDto(val error: HttpStatus, val error_description: String?,val at: LocalDateTime = LocalDateTime.now())