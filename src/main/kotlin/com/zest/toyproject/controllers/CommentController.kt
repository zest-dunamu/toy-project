package com.zest.toyproject.controllers

import com.zest.toyproject.models.request.CommentCreateRequest
import com.zest.toyproject.models.request.CommentUpdateRequest
import com.zest.toyproject.responses.CommentResponse
import com.zest.toyproject.responses.MemberResponse
import com.zest.toyproject.services.CommentService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService
) {

    @ApiOperation(value = "댓글 조회")
    @GetMapping("/{commentId}")
    fun getPost(@PathVariable commentId: Long): CommentResponse {
        return commentService.findById(commentId).let {
            CommentResponse(
                id = it.id!!,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.convertMemberResponse(member = it.member)
            )
        }
    }

    @ApiOperation(value = "댓글 등록")
    @PostMapping
    fun create(@Valid @RequestBody commentCreateRequest: CommentCreateRequest): ResponseEntity<CommentResponse> {
        val body: CommentResponse = commentService.createComment(commentCreateRequest).let {
            CommentResponse(
                id = it.id!!,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.convertMemberResponse(member = it.member)
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{commentId}")
    fun update(
        @PathVariable commentId: Long,
        @RequestParam(required = true) memberId: Long,
        @RequestParam(required = false) content: String?,
    ): CommentResponse {
        val commentUpdateRequest =
            CommentUpdateRequest(
                commentId = commentId,
                memberId = memberId,
                content = content
            )

        return commentService.updateComment(commentUpdateRequest).let {
            CommentResponse(
                id = it.id!!,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.convertMemberResponse(member = it.member)
            )
        }
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    fun delete(@PathVariable commentId: Long) = commentService.deleteComment(commentId)
}
