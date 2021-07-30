package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "members")
class Member (
    @Column(nullable = false, length = 50, unique = true)
    var username: String,

    var nickname: String? = null,

    @Column(nullable = false)
    var password: String,

) : BaseEntity()
