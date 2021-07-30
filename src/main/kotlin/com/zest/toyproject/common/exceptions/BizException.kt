package com.zest.toyproject.common.exceptions

import com.zest.toyproject.common.enums.Errors

class BizException : RuntimeException {

    var resultCode: Errors

    constructor(resultCode: Errors) : super() {
        this.resultCode = resultCode
    }

    constructor(resultCode: Errors, message: String) : super(message) {
        this.resultCode = resultCode
    }

    constructor(resultCode: Errors, e: Exception) : super(e) {
        this.resultCode = resultCode
    }

    override val message: String
        get() {
            var msg = """{"code": ${resultCode.code}, "message": "${resultCode.value}"}"""
            if (!super.message.isNullOrEmpty()) {
                msg += ", message: ${super.message}"
            }
            if (cause != null) {
                msg += ", cause message: ${cause.message}"
            }
            return msg
        }

    val exactMessage: String
        get() = super.message ?: resultCode.value

    override fun toString(): String {
        return BizException::class.qualifiedName + "${BizException::class.qualifiedName}: $message"
    }
}