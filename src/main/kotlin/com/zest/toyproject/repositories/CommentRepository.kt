package com.zest.toyproject.repositories

import com.zest.toyproject.models.Comment
import com.zest.toyproject.models.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findAllByPost(post: Post, pageable: Pageable): Page<Comment>
}
