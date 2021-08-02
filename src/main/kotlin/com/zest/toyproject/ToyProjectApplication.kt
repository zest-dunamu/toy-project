package com.zest.toyproject

import com.zest.toyproject.models.Board
import com.zest.toyproject.models.Comment
import com.zest.toyproject.models.Member
import com.zest.toyproject.models.Post
import com.zest.toyproject.models.request.BoardCreateRequest
import com.zest.toyproject.repositories.BoardRepository
import com.zest.toyproject.repositories.CommentRepository
import com.zest.toyproject.repositories.MemberRepository
import com.zest.toyproject.repositories.PostRepository
import com.zest.toyproject.services.PostService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication
class ToyProjectApplication {
    private val log = LoggerFactory.getLogger(ToyProjectApplication::class.java)

    @Profile("local")
    @Bean
    fun init(
        memberRepository: MemberRepository,
        boardRepository: BoardRepository,
        postReposotiry: PostRepository,
        commentRepository: CommentRepository,
    ) = CommandLineRunner {

        log.info("============INIT DUMMY DATA=============")
        // dummy member
        val dummyMember = memberRepository.save(
            Member(
                username = "dummy@dunamu.com",
                password = "dummydummy",
                nickname = "dummy"
            )
        )

        //dummy board
        val dummyBoard = boardRepository.save(
            Board(
                title = "dummy",
                description = "dummy",
            )
        )

        //dummy post
        val dummyPost = postReposotiry.save(
            Post(
                title = "dummy",
                content = "dummy",
                member = dummyMember,
                board = dummyBoard,
                likeCount = 0
            )
        )

        //dummy comment
        val dummyComment = commentRepository.save(
            Comment(
                content = "dummy",
                member = dummyMember,
                post = dummyPost,
                likeCount = 0
            )
        )
        log.info("========================================")
    }
}

fun main(args: Array<String>) {
    runApplication<ToyProjectApplication>(*args)
}
