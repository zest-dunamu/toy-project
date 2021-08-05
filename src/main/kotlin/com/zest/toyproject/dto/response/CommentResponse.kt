package com.zest.toyproject.dto.response

import com.zest.toyproject.models.Comment

data class CommentResponse(
    val id: Long,
    val content: String? = null,
    val likeCount: Int,
    var writer: MemberResponse
) {
    companion object {
        fun of(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                content = comment.content,
                likeCount = comment.likeCount,
                writer = MemberResponse.of(comment.member)
            )
        }
    }
}