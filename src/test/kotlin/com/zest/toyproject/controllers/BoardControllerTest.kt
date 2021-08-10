package com.zest.toyproject.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.dto.request.BoardCreateRequest
import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.SignUpMemberRequest
import com.zest.toyproject.dto.response.BoardWithPostsResponse
import com.zest.toyproject.dto.response.PostResponse
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.services.BoardService
import com.zest.toyproject.services.MemberService
import com.zest.toyproject.services.PostService
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
import org.springframework.util.LinkedMultiValueMap
import javax.transaction.Transactional

@Transactional
@AutoConfigureMockMvc
@WithMockUser(roles = ["USER", "ADMIN"])
class BoardControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
    private val boardService: BoardService,
    private val postService: PostService
) : AbstractIntegrationTest() {

    private lateinit var testMember: Member
    private lateinit var testBoard: Board
    private lateinit var testPost: Post

    @BeforeEach
    fun setup() {
        testMember = memberService.signUp(
            SignUpMemberRequest(
                username = "test@dunamu.com",
                password = "authentication",
                nickname = "authentication"
            )
        )

        testBoard = boardService.createBoard(
            BoardCreateRequest(
                title = "createBoardTitle",
                description = "createBoardDesc"
            )
        )

        testPost = postService.createPost(
            PostCreateRequest(
                title = "post-create",
                content = "post-create-content",
                memberId = testMember.id!!,
                boardId = testBoard.id!!,
            )
        )
    }

    @Test
    fun `게시판 등록`() {
        val boardCreateRequest = BoardCreateRequest(
            title = "board-create",
            description = "board-create"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("title").value(boardCreateRequest.title))
            .andExpect(MockMvcResultMatchers.jsonPath("description").value(boardCreateRequest.description))
            .andDo(MockMvcResultHandlers.print())
    }


    @Test
    fun `게시판의 제목은 중복 될 수 없다`() {
        val boardCreateRequest = BoardCreateRequest(
            title = "createBoardTitle",
            description = "board-create"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `게시판 조회`() {
        val expectResponse = BoardWithPostsResponse(
            id = testBoard.id!!,
            title = testBoard.title,
            description = testBoard.description,
            posts = listOf(PostResponse.of(testPost))
        )

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("page", "1")
        queryParams.add("size", "1")

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/boards/{boardId}", testBoard.id).queryParams(queryParams)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(testBoard.id))
            .andExpect(MockMvcResultMatchers.jsonPath("title").value(testBoard.title))
            .andExpect(MockMvcResultMatchers.jsonPath("description").value(testBoard.description))
            .andExpect(MockMvcResultMatchers.jsonPath("posts").isArray)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `존재하지 않는 게시판 조회`() {
        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("page", "1")
        queryParams.add("size", "1")

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/boards/{boardId}", 0).queryParams(queryParams)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `게시판 변경`() {

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("title", "change-title")
        queryParams.add("description", "change-desc")

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/boards/{boardId}", testBoard.id)
                .queryParams(queryParams)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(testBoard.id))
            .andExpect(MockMvcResultMatchers.jsonPath("title").value("change-title"))
            .andExpect(MockMvcResultMatchers.jsonPath("description").value("change-desc"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `게시판 삭제`() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/boards/{boardId}", testBoard.id)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }
}
