package com.zest.toyproject.dto.response

data class PostLikeResponse(
    val id: Long,
    val member: MemberResponse,
    val post: PostResponse,
)