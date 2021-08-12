package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostLikeCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.repositories.PostRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import javax.transaction.Transactional
import kotlin.concurrent.thread

@Transactional
class PostLikeServiceTest @Autowired constructor(
    private val postLikeService: PostLikeService,
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
    fun `게시글 좋아요 등록 성공`() {
        val postLike = postLikeService.createPostLike(
            testMember, testPost
        )

        assertThat(postLike).isNotNull
        assertThat(postLike.id).isNotNull
        assertThat(postLike.member).isEqualTo(testMember)
        assertThat(postLike.post).isEqualTo(testPost)
    }

    @Test
    fun `게시글 좋아요는 중복으로 누를 수 없다`() {
        postLikeService.createPostLike(testMember, testPost)

        assertThatThrownBy {
            postLikeService.createPostLike(
                testMember, testPost
            )
        }.isInstanceOf(BizException::class.java)
            .hasMessageContaining("이미 좋아요를 누르셨습니다.")
    }
}
