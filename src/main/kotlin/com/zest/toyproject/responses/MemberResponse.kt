package com.zest.toyproject.responses

import com.zest.toyproject.models.Member

data class MemberResponse(
    val id: Long,
    val username: String,
    val nickname: String
){
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
