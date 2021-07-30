package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "boards")
class Board (
    @Column(nullable = false, length = 50, unique = true)
    var title: String,

    var description: String? = null,

    ) : BaseEntity()
