package com.zest.toyproject.dto.request

import javax.validation.constraints.NotNull

data class CommentUpdateRequest(
    @field: NotNull
    val commentId: Long,

    @field: NotNull
    val memberId: Long,

    val content: String?
)