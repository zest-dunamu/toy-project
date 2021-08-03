package com.zest.toyproject.dto.request

import javax.validation.constraints.NotNull

data class PostUpdateRequest(
    @field: NotNull
    val postId: Long,

    @field: NotNull
    val memberId: Long,

    val title: String?,

    val content: String?
)