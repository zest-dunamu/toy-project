package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "boards")
class Board (
    @Column(nullable = false, length = 50, unique = true)
    var title: String,

    var description: String? = null,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL])
    var posts: MutableList<Post> = mutableListOf()

    ) : BaseEntity()
