package com.zest.toyproject.dto.response

import com.zest.toyproject.models.Member

data class MemberResponse(
    val id: Long,
    val username: String,
    val nickname: String
) {
    companion object {
        fun of(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                username = member.username,
                nickname = member.nickname!!
            )
        }
    }
}
