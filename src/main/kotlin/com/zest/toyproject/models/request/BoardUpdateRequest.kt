package com.zest.toyproject.models.request

import com.sun.istack.Nullable
import javax.validation.constraints.*

data class BoardUpdateRequest(
    @field: NotNull
    val boardId : Long,

    val title: String?,

    val description: String?
)