package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.CommentCreateRequest
import com.zest.toyproject.dto.request.CommentUpdateRequest
import com.zest.toyproject.dto.response.CommentResponse
import com.zest.toyproject.dto.response.MemberResponse
import com.zest.toyproject.services.CommentService
import com.zest.toyproject.services.MemberService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["댓글"])
@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService,
    private val memberService: MemberService
) {

    @ApiOperation(value = "댓글 조회")
    @GetMapping("/{commentId}")
    fun getPost(@PathVariable commentId: Long): CommentResponse {
        return commentService.findById(commentId).let {
            CommentResponse(
                id = it.id!!,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.of(member = it.member)
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
                writer = MemberResponse.of(member = it.member)
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping("/{commentId}")
    fun update(
        @PathVariable commentId: Long,
        @RequestParam(required = true) memberId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): CommentResponse {
        val comment = commentService.findById(commentId)
        val member = memberService.findById(memberId)

        return commentService.updateComment(comment, member, commentUpdateRequest).let {
            CommentResponse(
                id = it.id!!,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.of(member = it.member)
            )
        }
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    fun delete(@PathVariable commentId: Long) {
        val comment = commentService.findById(commentId)
        commentService.deleteComment(comment)
    }
}
