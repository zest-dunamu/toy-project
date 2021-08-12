package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.dto.response.*
import com.zest.toyproject.services.BoardService
import com.zest.toyproject.services.CommentService
import com.zest.toyproject.services.MemberService
import com.zest.toyproject.services.PostService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["게시글"])
@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
    private val memberService: MemberService,
    private val commentService: CommentService,
    private val boardService: BoardService
) {

    @ApiOperation(value = "게시글 조회")
    @GetMapping("/{postId}")
    fun getPost(
        @PathVariable postId: Long, @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): PostWithCommentsResponse {
        val post = postService.findById(postId)
        postService.upViews(post)

        return commentService.findAllByPostPagination(post, PageRequest.of(page - 1, size)).let {
            val comments: MutableList<CommentResponse> = mutableListOf()

            for (comment in it) {
                comments.add(
                    CommentResponse.of(comment)
                )
            }

            PostWithCommentsResponse(
                id = post.id!!,
                title = post.title,
                content = post.content,
                views = post.views,
                likeCount = post.likeCount,
                writer = MemberResponse.of(post.member!!),
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
                views = it.views,
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
                views = it.views,
                likeCount = it.likeCount,
                writer = MemberResponse.of(it.member)
            )
        }
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{postId}")
    fun delete(@PathVariable postId: Long) {
        val post = postService.findById(postId)
        postService.deletePost(post)
    }

    @ApiOperation(value = "게시글 검색")
    @GetMapping("/search")
    fun searchPost(
        @RequestParam(required = false) title: String?,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): MutableList<PostResponse> {
        val posts =
            postService.findByTitleLike(title!!, PageRequest.of(page - 1, size, Sort.by("createdAt").descending()))
        val postResponses = mutableListOf<PostResponse>()

        for (post in posts) {
            postResponses.add(
                PostResponse.of(post)
            )
        }
        return postResponses
    }

    @ApiOperation(value = "게시글 검색")
    @GetMapping("/search/query")
    fun searchPostByQuery(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) content: String?,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): MutableList<PostResponse> {
        val posts =
            postService.searchPostByQueryDsl(title, content, PageRequest.of(page - 1, size))
        val postResponses = mutableListOf<PostResponse>()

        for (post in posts) {
            postResponses.add(
                PostResponse.of(post)
            )
        }
        return postResponses
    }

    @ApiOperation(value = "게시판에 해당하는 게시글들 조회 + 페이징")
    @GetMapping
    fun getPostsByBoard(
        @RequestParam("boardId") boardId: Long, @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): PostResponses {
        val board = boardService.findById(boardId)
        val postList = postService.findAllByBoardPagination(
            board,
            PageRequest.of(page - 1, size, Sort.by("createdAt").descending())
        )
        val posts = mutableListOf<PostResponse>()

        for (post in postList) {
            posts.add(PostResponse.of(post))
        }

        return PostResponses(posts)
    }
}
