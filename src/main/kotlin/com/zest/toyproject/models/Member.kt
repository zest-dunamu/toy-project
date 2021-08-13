package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import com.zest.toyproject.common.enums.Role
import javax.persistence.*

@Entity
@Table(name = "members")
class Member(
    @Column(nullable = false, length = 50, unique = true)
    var username: String,

    var nickname: String? = null,

    @Column(nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
) : BaseEntity()
