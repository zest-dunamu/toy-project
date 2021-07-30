package com.zest.toyproject.responses

import jdk.jfr.Description

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String? = null,
    val likeCount: Int
)