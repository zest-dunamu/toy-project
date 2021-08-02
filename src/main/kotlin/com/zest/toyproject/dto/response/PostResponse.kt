package com.zest.toyproject.dto.response

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val likeCount: Int,
    var writer: MemberResponse?
)