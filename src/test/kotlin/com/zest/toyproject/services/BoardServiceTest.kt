package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Board
import com.zest.toyproject.dto.request.BoardCreateRequest
import com.zest.toyproject.dto.request.BoardUpdateRequest
import com.zest.toyproject.repositories.BoardRepository
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.security.PrivateKey
import javax.transaction.Transactional

@Transactional
class BoardServiceTest @Autowired constructor(
    private val boardService: BoardService,
    private val boardRepository: BoardRepository
) : AbstractIntegrationTest() {

    private lateinit var testBoard: Board

    @BeforeEach
    fun setup() {
        val board = Board(
            title = "testBoard",
            description = "this is test board"
        )
        testBoard = boardRepository.save(board)
    }

    @Test
    fun `게시판 등록 성공`() {
        var boardCreateRequest = BoardCreateRequest(
            title = "testBoard-2",
            description = "this is test board-2"
        )
        val createdBoard = boardService.createBoard(boardCreateRequest)

        assertThat(createdBoard).isNotNull
        assertThat(createdBoard.id).isNotNull
        assertThat(createdBoard.title).isEqualTo(boardCreateRequest.title)
        assertThat(createdBoard.description).isEqualTo(boardCreateRequest.description)
    }

    @Test
    fun `게시판 등록 실패-제목 중복`() {
        var boardCreateRequest = BoardCreateRequest(
            title = "testBoard",
            description = "this is test board"
        )
        assertThatThrownBy { boardService.createBoard(boardCreateRequest) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("이미 존재하는 게시판 제목 입니다.")
    }

    @Test
    fun `게시판 조회 성공`() {

        val findBoard = testBoard.id?.let { boardService.findById(it) }

        assertThat(findBoard).isNotNull
        assertThat(findBoard!!.id).isEqualTo(testBoard.id)
        assertThat(findBoard.title).isEqualTo(testBoard.title)
    }

    @Test
    fun `게시판 변경 성공`() {

        val originBoard = testBoard.id?.let { boardService.findById(it) }

        val changeBoard = boardService.updateBoard(
            originBoard!!,
            BoardUpdateRequest(
                title = "change",
                description = "change desc"
            )
        )

        assertThat(changeBoard).isNotNull
        assertThat(changeBoard!!.id).isEqualTo(originBoard!!.id)
        assertThat(changeBoard.title).isEqualTo("change")
        assertThat(changeBoard.description).isEqualTo("change desc")
    }

    @Test
    fun `게시판 제목만 변경 성공`() {

        val originBoard = testBoard.id?.let { boardService.findById(it) }

        val changeBoard = boardService.updateBoard(
            originBoard!!,
            BoardUpdateRequest(
                title = "change",
                description = null
            )
        )

        assertThat(changeBoard).isNotNull
        assertThat(changeBoard!!.id).isEqualTo(originBoard!!.id)
        assertThat(changeBoard.title).isEqualTo("change")
        assertThat(changeBoard.description).isEqualTo(originBoard.description)
    }

    @Test
    fun `게시판 내용만 변경 성공`() {

        val originBoard = testBoard.id?.let { boardService.findById(it) }

        val changeBoard = boardService.updateBoard(
            originBoard!!,
            BoardUpdateRequest(
                title = null,
                description = "change description"
            )
        )

        assertThat(changeBoard).isNotNull
        assertThat(changeBoard!!.id).isEqualTo(originBoard!!.id)
        assertThat(changeBoard.title).isEqualTo(originBoard.title)
        assertThat(changeBoard.description).isEqualTo("change description")
    }

    @Test
    fun `게시판 삭제`() {
        boardService.deleteBoard(testBoard)

        assertThatThrownBy { boardService.findById(testBoard.id!!) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 게시판입니다.")
    }

    @Test
    fun `게시판과 게시글들 조회 성공`() {
        val findBoard = boardService.findWithPostsWithMemberById(1L)

        assertThat(findBoard).isNotNull
        assertThat(findBoard.posts).isNotEmpty
        assertThat(findBoard.posts.first().title)
    }
}
