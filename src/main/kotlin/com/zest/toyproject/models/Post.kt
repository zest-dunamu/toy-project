package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "posts")
class Post (
    @Column(nullable = false, length = 50)
    var title: String,

    var content: String? = null,

    var likeCount: Int = 0,

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    var board: Board,

) : BaseEntity()
