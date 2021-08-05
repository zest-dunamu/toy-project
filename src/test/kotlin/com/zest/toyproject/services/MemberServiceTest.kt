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
    fun `회원가입 성공`() {
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
    fun `회원가입 실패 아이디 중복`() {
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
    fun `멤버 조회 성공`() {

        val findMember = testMember.id?.let { memberService.findById(it) }

        assertThat(findMember).isNotNull
        assertThat(findMember!!.username).isEqualTo(testMember.username)
    }
}
