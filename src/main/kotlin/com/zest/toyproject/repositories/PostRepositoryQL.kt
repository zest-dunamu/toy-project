package com.zest.toyproject.repositories

import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Post
import org.springframework.data.domain.Pageable

interface PostRepositoryQL {
    fun searchPostByQueryDsl(
        title: String?,
        content: String?,
        pageable: Pageable
    ): MutableList<Post>

    fun findAllByBoardOrderByLikes(board: Board, pageable: Pageable): MutableList<Post>
}