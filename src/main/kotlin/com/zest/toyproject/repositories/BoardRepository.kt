package com.zest.toyproject.repositories

import com.zest.toyproject.models.Board
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BoardRepository : JpaRepository<Board, Long> {
    fun findOneByTitle(title: String): Optional<Board>
    fun existsByTitle(title: String): Boolean

    @Query("select b from Board b left join fetch b.posts where b.id = ?1")
    fun findBoardWithPosts(id: Long): Optional<Board>

    // N+1 발생!
    @EntityGraph(attributePaths = ["posts"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithPostsById(id: Long): Optional<Board>

    // Board -> posts 를 가져올 때 post의 member도 한번에 가져오게 하여 N+1 문제 해결 하나의 쿼리문으로 모두 가져옴
    @EntityGraph(attributePaths = ["posts", "posts.member"], type = EntityGraph.EntityGraphType.LOAD)
    fun findWithPostsWithMemberById(id: Long): Optional<Board>
}
