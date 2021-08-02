package com.zest.toyproject.responses

data class BoardResponse(
    val id: Long,
    val title: String,
    val description: String? = null,
)