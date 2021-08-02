package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.dto.response.MemberResponse
import com.zest.toyproject.dto.response.PostResponse
import com.zest.toyproject.services.PostService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {

    @ApiOperation(value = "게시글 조회")
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): PostResponse {
        return postService.findById(postId).let {
            PostResponse(
                id = it.id!!,
                title = it.title,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.convertMemberResponse(it.member)
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
                writer = MemberResponse.convertMemberResponse(it.member)
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
        val postUpdateRequest =
            PostUpdateRequest(
                postId = postId,
                memberId = memberId,
                title = title,
                content = content
            )

        return postService.updatePost(postUpdateRequest).let {
            PostResponse(
                id = it.id!!,
                title = it.title,
                content = it.content,
                likeCount = it.likeCount,
                writer = MemberResponse.convertMemberResponse(it.member)
            )
        }
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{boardId}")
    fun delete(@PathVariable boardId: Long) = postService.deletePost(boardId)
}
