package com.zest.toyproject.common.exceptions

class BadRequestException : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
}