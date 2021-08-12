package com.zest.toyproject.dto.request

import javax.validation.constraints.*

data class PostLikeCreateRequest(
    @field: NotNull
    val memberId: Long,

    @field: NotNull
    val postId: Long
)