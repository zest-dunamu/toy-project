package com.zest.toyproject.responses

data class BoardWithPostsResponse(
    val id: Long,
    val title: String,
    val description: String? = null,
    val posts: List<PostResponse>
)