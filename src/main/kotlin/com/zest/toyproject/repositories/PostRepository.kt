package com.zest.toyproject.repositories

import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long>, PostRepositoryQL {

    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithMemberById(id: Long): Optional<Post>

    @EntityGraph(attributePaths = ["member", "comments", "comments.member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithMemberWithCommentsById(id: Long): Optional<Post>

    fun findByTitleContains(title: String): List<Post>

    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findByTitleContains(title: String, pageable: Pageable): Page<Post>

    @EntityGraph(attributePaths = ["member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findAllByBoard(board: Board, pageable: Pageable): Page<Post>

    fun findAllByBoardOrderByViewCountDesc(board: Board, pageable: Pageable): List<Post>

    fun findTop5ByBoardOrderByLikes(board: Board): List<Post>

    //comments에 대해 N+1 발생
    fun findAllByBoard(board: Board): List<Post>

    //comments fetch join - JPQL
    @Query("select p from Post p join fetch p.comments where p.board = ?1")
    fun findPostsByBoardJPQL(board: Board): List<Post>

    //comments fetch join - Entity Graph
    @EntityGraph(attributePaths = ["comments"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithCommentsByBoard(board: Board): List<Post>
}
