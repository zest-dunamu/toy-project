package com.zest.toyproject.dto.request

import javax.validation.constraints.NotNull

data class BoardUpdateRequest(
    @field: NotNull
    val boardId: Long,

    val title: String?,

    val description: String?
)