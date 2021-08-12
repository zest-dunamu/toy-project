package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.models.PostLike
import com.zest.toyproject.repositories.PostLikeRepository
import com.zest.toyproject.repositories.PostRepository
import org.springframework.stereotype.Service

@Service
class PostLikeService(
    private val postLikeRepository: PostLikeRepository,
) {
    fun findById(postLikeId: Long): PostLike =
        postLikeRepository.findById(postLikeId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시글 좋아요입니다.") }

    fun existsByMemberAndPost(member: Member, post: Post): Boolean =
        postLikeRepository.existsByMemberAndPost(member, post)

    fun createPostLike(member: Member, post: Post): PostLike {
        if (existsByMemberAndPost(member, post)) {
            throw BizException(Errors.CONFLICT, "이미 좋아요를 누르셨습니다.")
        }

        return postLikeRepository.save(
            PostLike(member, post)
        )
    }

    fun deletePostLike(postLike: PostLike) = postLikeRepository.delete(postLike)
}
