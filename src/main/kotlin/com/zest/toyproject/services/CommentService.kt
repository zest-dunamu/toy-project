package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.common.utils.LogUtil
import com.zest.toyproject.models.Comment
import com.zest.toyproject.dto.request.CommentCreateRequest
import com.zest.toyproject.dto.request.CommentUpdateRequest
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.repositories.CommentRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val memberService: MemberService,
    private val postService: PostService,
) {
    fun findById(commentId: Long): Comment =
        commentRepository.findById(commentId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 댓글입니다.") }

    fun createComment(commentCreateRequest: CommentCreateRequest): Comment {
        return commentRepository.save(
            Comment(
                content = commentCreateRequest.content,
                member = memberService.findById(commentCreateRequest.memberId),
                post = postService.findById(commentCreateRequest.postId),
                likeCount = 0
            )
        )
    }

    fun updateComment(comment: Comment, member: Member, commentUpdateRequest: CommentUpdateRequest): Comment {
        if (comment.member != member) {
            throw BizException(Errors.NOT_ACCEPTABLE, "댓글의 작성자가 아닙니다.")
        }

        comment.content = commentUpdateRequest.content ?: comment.content

        return commentRepository.save(comment)
    }

    fun deleteComment(comment: Comment) = commentRepository.delete(comment)

    fun findAllByPostPagination(post: Post, pageable: Pageable): List<Comment> =
        commentRepository.findAllByPost(post, pageable).content
}
