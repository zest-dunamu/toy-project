package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.common.utils.LogUtil
import com.zest.toyproject.common.utils.LogUtil.Companion.log
import com.zest.toyproject.configs.security.JwtTokenProvider
import com.zest.toyproject.dto.response.LoginResponse
import com.zest.toyproject.models.Member
import com.zest.toyproject.repositories.MemberRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun login(username: String, password: String): LoginResponse {
        val member: Member = memberRepository.findOneByUsername(username)
            .orElseThrow { BizException(Errors.WRONG_USERNAME) }

        if (!isMatchPassword(password, member.password)) {
            throw BizException(Errors.WRONG_PASSWORD)
        }
        val token: String = jwtTokenProvider.createJwtToken(member.username, listOf("USER"))
        log.info("token : $token")
        return LoginResponse(member.id!!, token)
    }

    private fun isMatchPassword(password: String, memberEncryptedPassword: String): Boolean {
        return passwordEncoder.matches(password, memberEncryptedPassword)
    }
}
