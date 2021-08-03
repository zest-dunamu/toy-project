package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Comment
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.dto.request.CommentCreateRequest
import com.zest.toyproject.dto.request.CommentUpdateRequest
import com.zest.toyproject.repositories.CommentRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

@Transactional
class CommentServiceTest @Autowired constructor(
    private val postService: PostService,
    private val commentRepository: CommentRepository,
    private val memberService: MemberService,
    private val commentService: CommentService,
) : AbstractIntegrationTest() {

    private lateinit var testPost: Post
    private lateinit var testMember: Member
    private lateinit var testBoard: Board
    private lateinit var testComment: Comment

    @BeforeEach
    fun setup() {
        testMember = memberService.findById(1L)
        testPost = postService.findById(1L)

        val comment = Comment(
            content = "this is test comment",
            member = testMember,
            post = testPost,
            likeCount = 0
        )
        testComment = commentRepository.save(comment)
    }

    @Test
    fun `댓글 등록 성공`() {
        val comment = commentService.createComment(
            CommentCreateRequest(
                memberId = testMember.id!!,
                postId = testPost.id!!,
                content = "test post test post",
            )
        )

        assertThat(comment).isNotNull
        assertThat(comment.id).isNotNull
        assertThat(comment.member).isEqualTo(testMember)
        assertThat(comment.post).isEqualTo(testPost)
    }

    @Test
    fun `댓글 조회 성공`() {
        val findComment = testComment.id?.let { commentService.findById(it) }

        assertThat(findComment).isNotNull
        assertThat(findComment!!.id).isEqualTo(testComment.id)
        assertThat(findComment.content).isEqualTo(testComment.content)
    }

    @Test
    fun `댓글 조회 실패`() {
        Assertions.assertThatThrownBy { commentService.findById(0) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 댓글입니다.")
    }

    @Test
    fun `댓글 변경 성공`() {
        val originComment = testComment.id?.let { commentService.findById(it) }

        val changeComment = commentService.updateComment(
            originComment!!, testMember,
            CommentUpdateRequest(
                content = "change content"
            )
        )

        assertThat(changeComment).isNotNull
        assertThat(changeComment!!.id).isEqualTo(originComment!!.id)
        assertThat(changeComment.content).isEqualTo("change content")
    }

    @Test
    fun `댓글 삭제 성공`() {
        commentService.deleteComment(testComment)

        Assertions.assertThatThrownBy { commentService.findById(testComment.id!!) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 댓글입니다.")
    }
}
