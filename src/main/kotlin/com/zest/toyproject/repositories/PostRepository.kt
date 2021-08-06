package com.zest.toyproject.repositories

import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithMemberById(id: Long): Optional<Post>

    @EntityGraph(attributePaths = ["member", "comments", "comments.member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithMemberWithCommentsById(id: Long): Optional<Post>

    fun findByTitleContains(title: String): List<Post>

    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findByTitleContains(title: String, pageable: Pageable): Page<Post>

    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findAllByBoard(board: Board, pageable: Pageable): Page<Post>
}
