package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Member
import com.zest.toyproject.dto.request.SignUpMemberRequest
import com.zest.toyproject.dto.request.SignInMemberRequest
import com.zest.toyproject.repositories.MemberRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

@Transactional
class MemberServiceTest @Autowired constructor(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository
) : AbstractIntegrationTest() {

    private lateinit var testMember: Member

    @BeforeEach
    fun setup() {
        val member = Member(
            username = "testMember@dunamu.com",
            password = "zestzest",
            nickname = "zest"
        )
        testMember = memberRepository.save(member)
    }

    @Test
    @DisplayName("새로운 멤버를 등록한다")
    fun 회원가입_성공() {
        var signUpMemberRequest: SignUpMemberRequest = SignUpMemberRequest(
            username = "test@dunamu.com",
            password = "signUpTest",
            nickname = "signUp"
        )
        val createdMember = memberService.signUp(signUpMemberRequest)

        assertThat(createdMember).isNotNull
        assertThat(createdMember!!.username).isEqualTo(signUpMemberRequest.username)
        assertThat(createdMember.nickname).isEqualTo(signUpMemberRequest.nickname)
    }

    @Test
    @DisplayName("멤버의 username은 유일해야 한다. 멤버의 username은 중복 될 수 없다.")
    fun 회원가입_실패_아이디_중복() {
        var signUpMemberRequest: SignUpMemberRequest = SignUpMemberRequest(
            username = "testMember@dunamu.com",
            password = "signUpTest",
            nickname = "signUp"
        )
        Assertions.assertThatThrownBy { memberService.signUp(signUpMemberRequest) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("이미 존재하는 아이디입니다.")
    }

    @Test
    @DisplayName("로그인")
    fun 로그인_성공() {
        val signInRequest: SignInMemberRequest = SignInMemberRequest(
            username = "testMember@dunamu.com",
            password = "zestzest",
        )

        val signInMember = memberService.signIn(signInRequest)

        assertThat(signInMember).isNotNull
        assertThat(signInMember.username).isEqualTo(signInRequest.username)
    }

    @Test
    @DisplayName("멤버 조회")
    fun 멤버조회_성공() {

        val findMember = testMember.id?.let { memberService.findById(it) }

        assertThat(findMember).isNotNull
        assertThat(findMember!!.username).isEqualTo(testMember.username)
    }
}
