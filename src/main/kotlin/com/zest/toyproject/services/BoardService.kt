package com.zest.toyproject.services

import com.zest.toyproject.common.enums.Errors
import com.zest.toyproject.common.exceptions.BizException
import com.zest.toyproject.models.Board
import com.zest.toyproject.dto.request.BoardCreateRequest
import com.zest.toyproject.dto.request.BoardUpdateRequest
import com.zest.toyproject.repositories.BoardRepository
import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardRepository: BoardRepository
) {
    fun findById(boardId: Long): Board =
        boardRepository.findById(boardId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시판입니다.") }

    fun findByTitle(title: String): Board =
        boardRepository.findOneByTitle(title).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시판입니다.") }

    fun isExistTitle(title: String): Boolean =
        boardRepository.existsByTitle(title)

    fun createBoard(boardCreateRequest: BoardCreateRequest): Board {
        boardDuplicateCheck(boardCreateRequest.title)

        val board = Board(
            title = boardCreateRequest.title,
            description = boardCreateRequest.description
        )
        return boardRepository.save(board)
    }

    fun updateBoard(boardUpdateRequest: BoardUpdateRequest): Board {
        val board = findById(boardUpdateRequest.boardId)

        board.title = boardUpdateRequest.title?.let {
            boardDuplicateCheck(it)
            boardUpdateRequest.title
        } ?: board.title

        board.description = boardUpdateRequest.description ?: board.description

        return boardRepository.save(board)
    }

    fun deleteBoard(boardId: Long) {
        val board: Board = findById(boardId)
        return boardRepository.delete(board)
    }

    fun findBoardWithPostsById(boardId: Long): Board =
        boardRepository.findBoardWithPosts(boardId).orElseThrow { BizException(Errors.NOT_FOUND, "존재하지 않는 게시판입니다.") }


    private fun boardDuplicateCheck(title: String) {
        if (isExistTitle(title))
            return throw BizException(Errors.CONFLICT, "이미 존재하는 게시판 제목 입니다.")
    }
}
