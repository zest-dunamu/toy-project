package com.zest.toyproject.common.enums

import org.springframework.http.HttpStatus

enum class Errors(override val code: HttpStatus, override val value: String) : HttpStatusCode {
    FAIL(HttpStatus.BAD_REQUEST, "실패"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "허용되지 않은 요청입니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않습니다"),
    CONFLICT(HttpStatus.CONFLICT, "이미 존재 합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    WRONG_USERNAME(HttpStatus.UNAUTHORIZED, "사용자명 형식이 올바르지 않습니다"),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 올바르지 않습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "요청을 수락 할 수 없습니다.")
}