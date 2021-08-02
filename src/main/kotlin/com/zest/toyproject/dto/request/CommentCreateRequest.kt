package com.zest.toyproject.dto.request

import javax.validation.constraints.*

data class CommentCreateRequest(

    val content: String,

    @field: NotNull
    val memberId: Long,

    @field: NotNull
    val postId: Long
)