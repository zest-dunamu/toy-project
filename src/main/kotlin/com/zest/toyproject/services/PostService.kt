package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Post
import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.repositories.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService (
    private val postRepository: PostRepository,
    private val memberService: MemberService,
    private val boardService: BoardService,
    ) {
    fun findById(postId: Long): Post =
        postRepository.findById(postId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시글입니다.") }

    fun createPost(postCreateRequest: PostCreateRequest): Post {
        val post = Post(
            title = postCreateRequest.title,
            content = postCreateRequest.content,
            member = memberService.findById(postCreateRequest.memberId),
            board = boardService.findById(postCreateRequest.boardId)
        )
        return postRepository.save(post)
    }

    fun updatePost(postUpdateRequest: PostUpdateRequest): Post {
        var post = findById(postUpdateRequest.postId)
        val member = memberService.findById(postUpdateRequest.memberId)

        if(post.member != member){
            throw BizException(Errors.NOT_ACCEPTABLE,"게시글의 작성자가 아닙니다.")
        }

        post.title = postUpdateRequest.title ?: post.title
        post.content = postUpdateRequest.content ?: post.content

        return postRepository.save(post)
    }

    fun deletePost(postId: Long) {
        var post = findById(postId)
        return postRepository.delete(post)
    }
}
