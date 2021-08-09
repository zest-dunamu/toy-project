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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    private lateinit var testPosts: MutableList<Post>
    private val ENOUGH_DATA_NUM = 100

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
    fun `게시글 삭제 성공`() {
        postService.deletePost(testPost)

        Assertions.assertThatThrownBy { postService.findById(testPost.id!!) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 게시글입니다.")
    }

    @Test
    fun `게시글과 댓글들 조회 성공`() {
        val findPost = postService.findWithMemberWithCommentsById(1L)

        assertThat(findPost).isNotNull
        assertThat(findPost.comments).isNotEmpty
        assertThat(findPost.comments.first().content).isEqualTo("dummy")
    }

    @Test
    fun `게시글 제목 검색 + 페이지네이션 성공`() {
        testPosts = mutableListOf()
        for (i in 1..ENOUGH_DATA_NUM) {
            testPosts.add(
                Post(
                    title = "example post $i",
                    content = "this is example post $i",
                    member = testMember,
                    board = testBoard,
                )
            )
        }
        postRepository.saveAll(testPosts)

        val findPosts = postService.findByTitleLike("example", PageRequest.of(1, 20, Sort.by("createdAt")))

        assertThat(findPosts).isNotEmpty
        assertThat(findPosts.size).isEqualTo(20)
        for (post in findPosts) {
            println("post.title = ${post.title}")
            assertThat(post).isNotNull
            assertThat(post.title).contains("example")
        }
    }

    @Test
    fun `게시판의 게시글들 조회 + 페이지네이션 성공`() {
        testPosts = mutableListOf()
        for (i in 1..ENOUGH_DATA_NUM) {
            testPosts.add(
                Post(
                    title = "example post $i",
                    content = "this is example post $i",
                    member = testMember,
                    board = testBoard,
                )
            )
        }
        postRepository.saveAll(testPosts)

        val findPosts =
            postService.findAllByBoardPagination(testBoard, PageRequest.of(0, 20, Sort.by("createdAt").descending()))

        assertThat(findPosts).isNotEmpty
        assertThat(findPosts.size).isEqualTo(20)
        for (post in findPosts) {
            println("post.title = ${post.title}")
            assertThat(post).isNotNull
            assertThat(post.title).contains("example")
        }
    }

    @Test
    fun `게시글 내용 검색 + 페이지네이션 성공 - QueryDSL`() {
        testPosts = mutableListOf()
        for (i in 1..ENOUGH_DATA_NUM) {
            testPosts.add(
                Post(
                    title = "example post $i",
                    content = "this is example content post $i",
                    member = testMember,
                    board = testBoard,
                )
            )
        }
        postRepository.saveAll(testPosts)

        val findPosts =
            postService.searchPostByQueryDsl(
                null,
                "example content",
                PageRequest.of(0, 20, Sort.by("createdAt").descending())
            )

        assertThat(findPosts).isNotEmpty
        assertThat(findPosts.size).isEqualTo(20)
        for (post in findPosts) {
            println("post.content = ${post.content}")
            assertThat(post).isNotNull
            assertThat(post.content).contains("example content")
        }
    }
}
