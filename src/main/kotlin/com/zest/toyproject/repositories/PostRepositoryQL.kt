package com.zest.toyproject.repositories

import com.zest.toyproject.models.Post

interface PostRepositoryQL {
    fun searchPostByQueryDsl(
        title: String?,
        content: String?,
        pageable: org.springframework.data.domain.Pageable
    ): MutableList<Post>
}