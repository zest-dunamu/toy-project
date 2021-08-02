package com.zest.toyproject.dto.request

import javax.validation.constraints.*

data class BoardCreateRequest(
    @field: NotBlank(message = "게시판 제목은 공백이 될 수 없습니다.")
    val title: String,

    val description: String
)