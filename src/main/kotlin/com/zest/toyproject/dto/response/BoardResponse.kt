package com.zest.toyproject.dto.response

data class BoardResponse(
    val id: Long,
    val title: String,
    val description: String? = null,
)