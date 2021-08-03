package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.repositories.PostRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
    fun `게시글 등록 성공`() {
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
    fun `게시글 조회 성공`() {
        val findPost = testPost.id?.let { postService.findById(it) }

        assertThat(findPost).isNotNull
        assertThat(findPost!!.id).isEqualTo(testPost.id)
        assertThat(findPost.title).isEqualTo(testPost.title)
    }

    @Test
    fun `게시글 조회 실패`() {
        Assertions.assertThatThrownBy { postService.findById(0) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 게시글입니다.")
    }

    @Test
    fun `게시글 변경 성공`() {
        val originPost = testPost.id?.let { postService.findById(it) }

        val changePost = postService.updatePost(
            originPost!!, testMember,
            PostUpdateRequest(
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
    fun `게시글 내용만 변경 성공`() {
        val originPost = testPost.id?.let { postService.findById(it) }

        val changePost = postService.updatePost(
            originPost!!, testMember,
            PostUpdateRequest(
                title = null,
                content = "only change content"
            )
        )

        assertThat(changePost).isNotNull
        assertThat(changePost!!.id).isEqualTo(originPost!!.id)
        assertThat(changePost.title).isEqualTo(originPost.title)
        assertThat(changePost.content).isEqualTo("only change content")
    }

    @Test
    fun `게시글 제목만 변경 성공`() {
        val originPost = testPost.id?.let { postService.findById(it) }

        val changePost = postService.updatePost(
            originPost!!, testMember,
            PostUpdateRequest(
                title = "only change title",
                content = null
            )
        )

        assertThat(changePost).isNotNull
        assertThat(changePost!!.id).isEqualTo(originPost!!.id)
        assertThat(changePost.title).isEqualTo("only change title")
        assertThat(changePost.content).isEqualTo(originPost.content)
    }

    @Test
    fun `게시글 삭제 성공`(){
        postService.deletePost(testPost)

        Assertions.assertThatThrownBy { postService.findById(testPost.id!!) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 게시글입니다.")
    }
}
