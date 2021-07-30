package com.zest.toyproject.models.request

import javax.validation.constraints.*

data class SignInMemberRequest(
    @field: NotBlank(message = "아이디는 공백이 될 수 없습니다.")
    @field: Email
    val username: String,

    @field: NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    @field: Size(min = 8, message = "비밀번호는 최소한 8자 이상이어야 합니다.")
    val password: String,
)