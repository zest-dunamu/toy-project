package com.zest.toyproject.configs

import com.zest.toyproject.common.exceptions.BadRequestException
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.common.response.ExceptionDto
import com.zest.toyproject.common.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.function.Consumer

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ExceptionDto> {
        val exception = ExceptionDto(HttpStatus.BAD_REQUEST, ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<ExceptionDto> {
        val exception = ExceptionDto(HttpStatus.BAD_REQUEST, ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): Map<String, String?> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage
        })
        return errors
    }

    @ExceptionHandler(BizException::class)
    fun handleBizException(ex: BizException): ResponseEntity<ExceptionDto> {
        val exception = ExceptionDto(ex.resultCode.code, ex.exactMessage)
        return ResponseEntity.status(ex.resultCode.code).body(exception)
    }
}
