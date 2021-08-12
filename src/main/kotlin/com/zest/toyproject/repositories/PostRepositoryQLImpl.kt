package com.zest.toyproject.repositories

import com.querydsl.jpa.impl.JPAQueryFactory
import com.zest.toyproject.models.*
import com.zest.toyproject.models.QPost.post
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

    override fun findAllByBoardOrderByLikes(board: Board, pageable: Pageable): MutableList<Post> {
        val builder = from(post).leftJoin(post.member).fetchJoin()
            .where(post.board.eq(board))
        builder.orderBy(post.likes.size().desc()).offset(pageable.offset).limit(pageable.pageSize.toLong())
        return builder.fetch()
    }
}
