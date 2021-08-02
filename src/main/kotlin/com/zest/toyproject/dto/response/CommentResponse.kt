package com.zest.toyproject.dto.response

data class CommentResponse(
    val id: Long,
    val content: String? = null,
    val likeCount: Int,
    var writer: MemberResponse
)