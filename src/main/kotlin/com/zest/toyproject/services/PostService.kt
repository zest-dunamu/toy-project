package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.common.utils.LogUtil.Companion.log
import com.zest.toyproject.dto.request.PostCreateRequest
import com.zest.toyproject.dto.request.PostUpdateRequest
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.repositories.PostRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val memberService: MemberService,
    private val boardService: BoardService,
) {
    fun findById(postId: Long): Post =
        postRepository.findById(postId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시글입니다.") }

    fun findWithMemberById(postId: Long): Post =
        postRepository.findWithMemberById(postId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시글입니다.") }

    fun findWithMemberWithCommentsById(postId: Long): Post =
        postRepository.findWithMemberWithCommentsById(postId)
            .orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시글입니다.") }

    fun createPost(postCreateRequest: PostCreateRequest): Post {
        val post = Post(
            title = postCreateRequest.title,
            content = postCreateRequest.content,
            member = memberService.findById(postCreateRequest.memberId),
            board = boardService.findById(postCreateRequest.boardId)
        )
        return postRepository.save(post)
    }

    fun updatePost(post: Post, member: Member, postUpdateRequest: PostUpdateRequest): Post {
        if (!post.member.equals(member)) {
            throw BizException(Errors.NOT_ACCEPTABLE, "게시글의 작성자가 아닙니다.")
        }

        post.title = postUpdateRequest.title ?: post.title
        post.content = postUpdateRequest.content ?: post.content

        return postRepository.save(post)
    }

    fun deletePost(post: Post) = postRepository.delete(post)

    fun findByTitleLike(title: String): List<Post> = postRepository.findByTitleContains(title)

    fun findByTitleLike(title: String, pageable: Pageable): List<Post> {
        log.info("pageable = $pageable")
        val pagingPost = postRepository.findByTitleContains(title, pageable)
        log.info("pagingPost = $pagingPost")
        return pagingPost.content
    }

    fun findAllByBoardPagination(board: Board, pageable: Pageable): List<Post> {
        log.info("pageable = $pageable")
        val pagingPost = postRepository.findAllByBoard(board, pageable)
        log.info("pagingPost = $pagingPost")
        return pagingPost.content
    }

    fun searchPostByQueryDsl(title: String?, content: String?, pageable: Pageable): List<Post> {
        return postRepository.searchPostByQueryDsl(title, content, pageable)
    }
}
