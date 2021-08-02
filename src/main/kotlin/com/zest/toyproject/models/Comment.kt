package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "comments")
class Comment (

    var content: String? = null,

    var likeCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post,

) : BaseEntity()
