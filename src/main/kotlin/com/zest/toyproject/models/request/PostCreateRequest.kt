package com.zest.toyproject.models.request

import javax.validation.constraints.*

data class PostCreateRequest(
    @field: NotBlank(message = "게시글 제목은 공백이 될 수 없습니다.")
    val title: String,

    val content: String,

    @field: NotNull
    val memberId:Long,

    @field: NotNull
    val boardId: Long
)