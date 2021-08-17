package com.zest.toyproject.dto.request

import com.zest.toyproject.common.enums.Role
import javax.validation.constraints.*

data class GrantRoleRequest(
    @field: NotNull
    val memberId: Long,

    @field: NotNull
    val role: Role,
)