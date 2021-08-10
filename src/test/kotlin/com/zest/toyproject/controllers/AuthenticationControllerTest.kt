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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.transaction.Transactional

@Transactional
@AutoConfigureMockMvc
class AuthenticationControllerTest @Autowired constructor(
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
    fun `로그인을 하면 인증에 필요한 토큰과 memberId를 반환한다`() {
        val requestLogin = SignInMemberRequest(
            username = "test@dunamu.com",
            password = "authentication"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(requestLogin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )

            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("memberId").value(testMember.id))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `코틀린 DSL - 로그인을 하면 인증에 필요한 토큰과 memberId를 반환`() {
        val requestLogin = SignInMemberRequest(
            username = "test@dunamu.com",
            password = "authentication"
        )

        mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestLogin)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.token") { exists() }
            jsonPath("$.memberId") { value(testMember.id) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `회원의 username 은 이메일 형식이다`() {
        val requestLogin = SignInMemberRequest(
            username = "test",
            password = "authentication"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(requestLogin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )

            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원의 비밀번호는 8자 이상이다`() {
        val requestLogin = SignInMemberRequest(
            username = "test",
            password = "1234567"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(requestLogin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )

            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `로그인 회원의 username이 존재하지 않거나 틀림`() {
        val requestLogin = SignInMemberRequest(
            username = "test@naver.com",
            password = "authentication"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(requestLogin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )

            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.error_description").value(Errors.WRONG_USERNAME.value)
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `로그인 회원의 비밀번호가 틀림`() {
        val requestLogin = SignInMemberRequest(
            username = "test@dunamu.com",
            password = "123456789"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .content(objectMapper.writeValueAsString(requestLogin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        )

            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.error_description").value(Errors.WRONG_PASSWORD.value)
            )
            .andDo(MockMvcResultHandlers.print())
    }
}
