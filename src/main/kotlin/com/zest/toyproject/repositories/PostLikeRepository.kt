package com.zest.toyproject.repositories

import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.models.PostLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostLikeRepository : JpaRepository<PostLike, Long> {
    fun existsByMemberAndPost(member: Member, post: Post): Boolean
}
