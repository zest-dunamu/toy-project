package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.PostLikeCreateRequest
import com.zest.toyproject.dto.response.MemberResponse
import com.zest.toyproject.dto.response.PostLikeResponse
import com.zest.toyproject.dto.response.PostResponse
import com.zest.toyproject.services.BoardService
import com.zest.toyproject.services.MemberService
import com.zest.toyproject.services.PostLikeService
import com.zest.toyproject.services.PostService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["게시글 좋아요"])
@RestController
@RequestMapping("/api/post-likes")
class PostLikeController(
    private val postLikeService: PostLikeService,
    private val postService: PostService,
    private val memberService: MemberService,
    private val boardService: BoardService,
) {

    @ApiOperation(value = "좋아요 정보 조회")
    @GetMapping("/{postLikeId}")
    fun getPost(
        @PathVariable postLikeId: Long
    ): PostLikeResponse {
        return postLikeService.findById(postLikeId).let {
            PostLikeResponse(
                id = it.id!!,
                member = MemberResponse.of(it.member),
                post = PostResponse.of(it.post)
            )
        }
    }

    @ApiOperation(value = "게시글 좋아요 등록")
    @PostMapping
    fun create(@Valid @RequestBody postLikeCreateRequest: PostLikeCreateRequest): ResponseEntity<PostLikeResponse> {
        val member = memberService.findById(postLikeCreateRequest.memberId)
        val post = postService.findById(postLikeCreateRequest.postId)

        val body: PostLikeResponse = postLikeService.createPostLike(member, post).let {
            PostLikeResponse(
                id = it.id!!,
                member = MemberResponse.of(it.member),
                post = PostResponse.of(it.post)
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @ApiOperation(value = "게시글 좋아요 삭제(좋아요 취소)")
    @DeleteMapping("/{postLikeId}")
    fun delete(@PathVariable postLikeId: Long) {
        postLikeService.findById(postLikeId).let {
            postLikeService.deletePostLike(it)
        }
    }
}
