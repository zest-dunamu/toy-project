package com.zest.toyproject.repositories

import com.zest.toyproject.models.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findOneByTitle(title: String): Optional<Post>
    fun existsByTitle(title: String): Boolean
}
