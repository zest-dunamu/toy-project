package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.request.SignUpMemberRequest
import com.zest.toyproject.models.request.SignInMemberRequest
import com.zest.toyproject.repositories.MemberRepository
import com.zest.toyproject.responses.MemberResponse
import org.springframework.stereotype.Service

@Service
class MemberService(
    // TODO: Spring Security + JWT 인증 구현

    private val memberRepository: MemberRepository
) {
    fun findById(memberId: Long): Member =
        memberRepository.findById(memberId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 멤버입니다.") }

    fun findByUsername(username: String): Member =
        memberRepository.findOneByUsername(username).orElseThrow { BizException(Errors.NOT_FOUND) }

    fun isExistUsername(username: String?): Boolean =
        memberRepository.existsByUsername(username)

    fun signUp(signUpMemberRequest: SignUpMemberRequest): MemberResponse? {

        if (isExistUsername(signUpMemberRequest.username))
            return throw BizException(Errors.CONFLICT, "이미 존재하는 아이디입니다.")

        val member = Member(
            username = signUpMemberRequest.username,
            password = signUpMemberRequest.password,
            nickname = signUpMemberRequest.nickname
        )
        val createdMember: Member = memberRepository.save(member)

        return createdMember.id?.let {
            MemberResponse(
                id = it,
                username = createdMember.username,
                nickname = createdMember.nickname!!
            )
        }
    }

    fun signIn(signInMemberRequest: SignInMemberRequest): MemberResponse {
        val member = findByUsername(signInMemberRequest.username)

        if (!isCorrectPassword(member, signInMemberRequest.password)) {
            throw BizException(Errors.WRONG_PASSWORD)
        }

        return MemberResponse(
            id = member.id!!,
            username = member.username,
            nickname = member.nickname!!,
        )
    }

    private fun isCorrectPassword(member: Member, password: String) =
        member.password.equals(password)
}
