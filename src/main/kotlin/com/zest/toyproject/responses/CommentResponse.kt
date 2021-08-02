package com.zest.toyproject.responses

data class CommentResponse(
    val id: Long,
    val content: String? = null,
    val likeCount: Int,
    var writer: MemberResponse
)