package com.zest.toyproject.services

import com.zest.toyproject.AbstractIntegrationTest
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Board
import com.zest.toyproject.models.request.BoardCreateRequest
import com.zest.toyproject.models.request.BoardUpdateRequest
import com.zest.toyproject.repositories.BoardRepository
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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
    @DisplayName("새로운 게시판을 등록한다")
    fun 게시판등록_성공() {
        var boardCreateRequest: BoardCreateRequest = BoardCreateRequest(
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
    @DisplayName("게시판의 title은 유일해야 한다.")
    fun 게시판등록_실패_제목_중복() {
        var boardCreateRequest: BoardCreateRequest = BoardCreateRequest(
            title = "testBoard",
            description = "this is test board"
        )
        assertThatThrownBy { boardService.createBoard(boardCreateRequest) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("이미 존재하는 게시판 제목 입니다.")
    }

    @Test
    @DisplayName("게시판 조회")
    fun 게시판조회_성공() {

        val findBoard = testBoard.id?.let { boardService.findById(it) }

        assertThat(findBoard).isNotNull
        assertThat(findBoard!!.id).isEqualTo(testBoard.id)
        assertThat(findBoard.title).isEqualTo(testBoard.title)
    }

    @Test
    @DisplayName("게시판 변경")
    fun 게시판변경_성공() {

        val originBoard = testBoard.id?.let { boardService.findById(it) }

        val changeBoard = boardService.updateBoard(
            BoardUpdateRequest(
                boardId = testBoard.id!!,
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
    @DisplayName("게시판 제목만 변경")
    fun 게시판_제목만_변경_성공() {

        val originBoard = testBoard.id?.let { boardService.findById(it) }

        val changeBoard = boardService.updateBoard(
            BoardUpdateRequest(
                boardId = testBoard.id!!,
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
    @DisplayName("게시판 삭제")
    fun 게시판_삭제() {
        boardService.deleteBoard(testBoard.id!!)

        assertThatThrownBy { boardService.findById(testBoard.id!!) }.isInstanceOf(
            BizException::class.java
        ).hasMessageContaining("존재하지 않는 게시판입니다.")
    }
}