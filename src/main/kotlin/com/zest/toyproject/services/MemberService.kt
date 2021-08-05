package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.dto.request.SignInMemberRequest
import com.zest.toyproject.dto.request.SignUpMemberRequest
import com.zest.toyproject.dto.response.MemberResponse
import com.zest.toyproject.models.Member
import com.zest.toyproject.repositories.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) : UserDetailsService {
    fun findById(memberId: Long): Member =
        memberRepository.findById(memberId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 멤버입니다.") }

    fun findByUsername(username: String): Member =
        memberRepository.findOneByUsername(username).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 멤버입니다.") }

    fun isExistUsername(username: String?): Boolean =
        memberRepository.existsByUsername(username)

    fun signUp(signUpMemberRequest: SignUpMemberRequest): Member {

        if (isExistUsername(signUpMemberRequest.username))
            return throw BizException(Errors.CONFLICT, "이미 존재하는 아이디입니다.")

        val member = Member(
            username = signUpMemberRequest.username,
            password = passwordEncoder.encode(signUpMemberRequest.password),
            nickname = signUpMemberRequest.nickname
        )

        return memberRepository.save(member)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val member = findByUsername(username)
        return User(
            member.username,
            member.password,
            true,
            true,
            true,
            true,
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
    }
}
