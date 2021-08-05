package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.dto.response.CommentResponse
import com.zest.toyproject.dto.response.MemberResponse
import com.zest.toyproject.dto.response.PostResponse
import com.zest.toyproject.dto.response.PostWithCommentsResponse
import com.zest.toyproject.models.Comment
import com.zest.toyproject.services.MemberService
import com.zest.toyproject.services.PostService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["게시글"])
@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
    private val memberService: MemberService
) {

    @ApiOperation(value = "게시글 조회")
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): PostWithCommentsResponse {
        return postService.findWithMemberWithCommentsById(postId).let {
            val comments: MutableList<CommentResponse> = mutableListOf()

            for (comment in it.comments) {
                comments.add(
                    CommentResponse.of(comment)
                )
            }

            PostWithCommentsResponse(
                id = it.id!!,
                title = it.title,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.of(it.member!!),
                comments = comments
            )
        }
    }

    @ApiOperation(value = "게시글 등록")
    @PostMapping
    fun create(@Valid @RequestBody postCreateRequest: PostCreateRequest): ResponseEntity<PostResponse> {
        val body: PostResponse = postService.createPost(postCreateRequest).let {
            PostResponse(
                id = it.id!!,
                title = it.title,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.of(it.member!!)
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @ApiOperation(value = "게시글 수정")
    @PutMapping("/{postId}")
    fun update(
        @PathVariable postId: Long,
        @RequestParam(required = true) memberId: Long,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) content: String?,
    ): PostResponse {
        val post = postService.findById(postId)
        val member = memberService.findById(memberId)

        val postUpdateRequest =
            PostUpdateRequest(
                title = title,
                content = content
            )

        return postService.updatePost(post, member, postUpdateRequest).let {
            PostResponse(
                id = it.id!!,
                title = it.title,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.of(it.member!!)
            )
        }
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{postId}")
    fun delete(@PathVariable postId: Long) {
        val post = postService.findById(postId)
        postService.deletePost(post)
    }
}
