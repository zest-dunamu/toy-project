package com.zest.toyproject.dto.response

import com.zest.toyproject.models.Post

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String?,
    val viewCount: Int,
    val likeCount: Int,
    var writer: MemberResponse?
) {
    companion object {
        fun of(post: Post): PostResponse {
            return PostResponse(
                id = post.id!!,
                title = post.title,
                content = post.content,
                viewCount = post.viewCount,
                likeCount = post.likes.size,
                writer = MemberResponse.of(post.member)
            )
        }
    }
}