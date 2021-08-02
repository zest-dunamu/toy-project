package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.models.request.PostCreateRequest
import com.zest.toyproject.models.request.PostUpdateRequest
import com.zest.toyproject.repositories.PostRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

@Transactional
class PostServiceTest @Autowired constructor(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val memberService: MemberService,
    private val boardService: BoardService,
) : AbstractIntegrationTest() {

    private lateinit var testPost: Post
    private lateinit var testMember: Member
    private lateinit var testBoard: Board

    @BeforeEach
    fun setup() {
        testMember = memberService.findById(1L)
        testBoard = boardService.findById(1L)

        val post = Post(
            title = "testPost",
            content = "this is test post",
            member = testMember,
            board = testBoard,
        )
        testPost = postRepository.save(post)
    }

    @Test
    @DisplayName("새로운 게시글 등록")
    fun 게시글등록_성공() {
        var post = postService.createPost(
            PostCreateRequest(
                memberId = testMember.id!!,
                boardId = testBoard.id!!,
                title = "add post",
                content = "test post test post"
            )
        )

        assertThat(post).isNotNull
        assertThat(post.id).isNotNull
        assertThat(post.member).isEqualTo(testMember)
        assertThat(post.board).isEqualTo(testBoard)
    }

    @Test
    @DisplayName("게시글 조회")
    fun 게시글조회_성공() {

        val findPost = testPost.id?.let { postService.findById(it) }

        assertThat(findPost).isNotNull
        assertThat(findPost!!.id).isEqualTo(testPost.id)
        assertThat(findPost.title).isEqualTo(testPost.title)
    }

    @Test
    @DisplayName("게시글 변경")
    fun 게시글변경_성공() {

        val originPost = testPost.id?.let { postService.findById(it) }

        val changePost = postService.updatePost(
            PostUpdateRequest(
                memberId = testMember.id!!,
                postId = testPost.id!!,
                title = "change",
                content = "change content"
            )
        )

        assertThat(changePost).isNotNull
        assertThat(changePost!!.id).isEqualTo(originPost!!.id)
        assertThat(changePost.title).isEqualTo("change")
        assertThat(changePost.content).isEqualTo("change content")
    }

    @Test
    @DisplayName("게시글 내용만 변경")
    fun 게시글_내용만_변경_성공() {

        val originPost = testPost.id?.let { postService.findById(it) }

        val changePost = postService.updatePost(
            PostUpdateRequest(
                memberId = testMember.id!!,
                postId = testPost.id!!,
                title = null,
                content = "only change content"
            )
        )

        assertThat(changePost).isNotNull
        assertThat(changePost!!.id).isEqualTo(originPost!!.id)
        assertThat(changePost.title).isEqualTo(originPost.title)
        assertThat(changePost.content).isEqualTo("only change content")
    }
}
