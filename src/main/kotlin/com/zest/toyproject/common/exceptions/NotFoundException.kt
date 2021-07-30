package com.zest.toyproject.common.exceptions

class NotFoundException : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
}