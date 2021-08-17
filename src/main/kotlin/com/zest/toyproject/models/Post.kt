package com.zest.toyproject.models

import com.zest.toyproject.common.entities.BaseEntity
import org.hibernate.annotations.BatchSize
import javax.persistence.*

@Entity
@Table(name = "posts")
class Post(
    @Column(nullable = false, length = 50)
    var title: String,

    var content: String? = null,

    @Transient
    var likeCount: Int = 0,

    var viewCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    var board: Board,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    var comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    @BatchSize(size = 500)
    var likes: MutableSet<PostLike> = mutableSetOf(),
) : BaseEntity()
