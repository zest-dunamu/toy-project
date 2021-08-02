package com.zest.toyproject.responses

import com.zest.toyproject.models.Member

data class CommentResponse(
    val id: Long,
    val content: String? = null,
    val likeCount: Int,
    var writer: MemberResponse
) {
    companion object {
        fun convertMemberResponse(member: Member) : MemberResponse {
            return MemberResponse(
                id = member.id!!,
                username = member.username,
                nickname = member.nickname!!
            )
        }
    }
}
