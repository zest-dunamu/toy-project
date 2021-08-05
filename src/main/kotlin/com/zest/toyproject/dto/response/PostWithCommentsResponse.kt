package com.zest.toyproject.dto.response

data class PostWithCommentsResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val likeCount: Int,
    var writer: MemberResponse?,
    val comments: List<CommentResponse>
)
