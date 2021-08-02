package com.zest.toyproject.repositories

import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Member
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
}
