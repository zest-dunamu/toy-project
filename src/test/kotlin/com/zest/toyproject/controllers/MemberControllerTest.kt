package com.zest.toyproject.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.dto.request.SignInMemberRequest
import com.zest.toyproject.dto.request.SignUpMemberRequest
import com.zest.toyproject.models.Member
import com.zest.toyproject.services.MemberService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.transaction.Transactional

@Transactional
@AutoConfigureMockMvc
@WithMockUser(roles = ["USER", "ADMIN"])
class MemberControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
) : AbstractIntegrationTest() {

    private lateinit var testMember: Member

    @BeforeEach
    fun setup() {
        testMember = memberService.signUp(
            SignUpMemberRequest(
                username = "test@dunamu.com",
                password = "authentication",
                nickname = "authentication"
            )
        )
    }

    @Test
    fun `회원가입`() {
        val signUpRequestMember = SignUpMemberRequest(
            username = "signUpTest@dunamu.com",
            password = "authentication",
            nickname = "authentication"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(signUpRequestMember))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("username").value(signUpRequestMember.username))
            .andExpect(MockMvcResultMatchers.jsonPath("nickname").value(signUpRequestMember.nickname))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원의 username 은 이메일 형식이다`() {
        val signUpMemberRequest = SignUpMemberRequest(
            username = "signUpTest",
            password = "authentication",
            nickname = "authentication"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(signUpMemberRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원의 비밀번호는 8자 이상이다`() {
        val signUpMemberRequest = SignUpMemberRequest(
            username = "signUpTest",
            password = "authentication",
            nickname = "1234567"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(signUpMemberRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원의 username 은 중복 될 수 없다`() {
        val signUpMemberRequest = SignUpMemberRequest(
            username = "test@dunamu.com",
            password = "authentication",
            nickname = "authentication"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(signUpMemberRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원 조회`() {

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/members/{memberId}", testMember.id)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(testMember.id))
            .andExpect(MockMvcResultMatchers.jsonPath("username").value(testMember.username))
            .andExpect(MockMvcResultMatchers.jsonPath("nickname").value(testMember.nickname))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `존재하지 않는 회원 조회`() {

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/members/{memberId}", 0)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andDo(MockMvcResultHandlers.print())
    }

}
