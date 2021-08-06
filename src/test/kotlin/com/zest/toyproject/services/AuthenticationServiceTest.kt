package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.configs.security.JwtTokenProvider
import com.zest.toyproject.models.Member
import com.zest.toyproject.dto.request.SignUpMemberRequest
import com.zest.toyproject.dto.request.SignInMemberRequest
import com.zest.toyproject.dto.response.LoginResponse
import com.zest.toyproject.repositories.MemberRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional
import kotlin.math.log

@Transactional
class AuthenticationServiceTest @Autowired constructor(
    private val memberService: MemberService,
    private val authenticationService: AuthenticationService,
    private val jwtTokenProvider: JwtTokenProvider,
) : AbstractIntegrationTest() {

    private lateinit var testMember: Member

    @BeforeEach
    fun setup() {
        testMember = memberService.signUp(
            SignUpMemberRequest(
                username = "test@dunamu.com",
                password = "signUpTest",
                nickname = "signUp"
            )
        )
    }

    @Test
    fun `로그인 성공`() {
        val loginResponse = authenticationService.login("test@dunamu.com", "signUpTest")
        val loginMember = memberService.findById(loginResponse.memberId)

        assertThat(loginResponse.memberId).isNotNull.isEqualTo(loginMember.id)
        assertThat(loginResponse.token).isNotEmpty
        assertThat(jwtTokenProvider.isValidateToken(loginResponse.token)).isTrue
        assertThat(loginMember.username).isEqualTo("test@dunamu.com")
    }

    @Test
    fun `로그인 실패 비밀번호 틀림`() {
        assertThatThrownBy { authenticationService.login("test@dunamu.com", "sign") }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("잘못된 비밀번호입니다.")
    }

    @Test
    fun `로그인 실패 아이디 틀림`() {
        assertThatThrownBy { authenticationService.login("te@dunamu.com", "signUpTest") }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("아이디가 존재하지 않거나 틀렸습니다.")
    }
}
