package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "post_likes")
class PostLike(

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    var post: Post,
) : BaseEntity()
