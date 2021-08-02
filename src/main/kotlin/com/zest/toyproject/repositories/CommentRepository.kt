package com.zest.toyproject.repositories

import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Comment
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
}
