package com.zest.toyproject.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.models.QMember
import com.zest.toyproject.models.QPost.post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class PostRepositoryQLImpl(jpaQueryFactory: JPAQueryFactory) : QuerydslRepositorySupport(Post::class.java),
    PostRepositoryQL {
    override fun searchPostByQueryDsl(title: String?, content: String?, pageable: Pageable): MutableList<Post> {
        val builder = from(post).leftJoin(post.member).fetchJoin()

        if (title != null) {
            builder.where(post.title.contains(title))
        }
        if (content != null) {
            builder.where(post.content.contains(content))
        }

        builder.offset(pageable.offset).limit(pageable.pageSize.toLong())
        builder.orderBy(post.createdAt.desc())

        return builder.fetch()
    }
}
