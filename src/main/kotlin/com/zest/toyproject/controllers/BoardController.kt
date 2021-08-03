package com.zest.toyproject.controllers

import com.zest.toyproject.dto.request.BoardCreateRequest
import com.zest.toyproject.dto.request.BoardUpdateRequest
import com.zest.toyproject.dto.response.BoardResponse
import com.zest.toyproject.models.Board
import com.zest.toyproject.services.BoardService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["게시판"])
@RestController
@RequestMapping("/api/boards")
class BoardController(
    private val boardService: BoardService
) {

    @ApiOperation(value = "게시판 조회")
    @GetMapping("/{boardId}")
    fun getBoard(@PathVariable boardId: Long): BoardResponse {
        return boardService.findById(boardId).let {
            BoardResponse(
                id = it.id!!,
                title = it.title,
                description = it.description,
            )
        }
    }

    @ApiOperation(value = "게시판 등록")
    @PostMapping
    fun create(@Valid @RequestBody boardCreateRequest: BoardCreateRequest): ResponseEntity<BoardResponse> {
        val body: BoardResponse = boardService.createBoard(boardCreateRequest).let {
            BoardResponse(
                id = it.id!!,
                title = it.title,
                description = it.description,
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(body)
    }

    @ApiOperation(value = "게시판 변경")
    @PutMapping("/{boardId}")
    fun update(
        @PathVariable boardId: Long,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) description: String?,
    ): BoardResponse {
        val board: Board = boardService.findById(boardId)

        val boardUpdateRequest =
            BoardUpdateRequest(
                title = title,
                description = description
            )

        return boardService.updateBoard(board, boardUpdateRequest).let {
            BoardResponse(
                id = it.id!!,
                title = it.title,
                description = it.description,
            )
        }
    }

    @ApiOperation(value = "게시판 삭제")
    @DeleteMapping("/{boardId}")
    fun delete(@PathVariable boardId: Long) {
        val board = boardService.findById(boardId)
        boardService.deleteBoard(board)
    }
}
