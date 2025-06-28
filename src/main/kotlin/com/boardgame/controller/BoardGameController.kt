package com.boardgame.controller

import com.boardgame.dto.request.BoardGameRequest
import com.boardgame.dto.response.BoardGameResponse
import com.boardgame.service.BoardGameService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "보드게임", description = "보드게임 관리 API")
@RestController
@RequestMapping("/api/boardgames")
class BoardGameController(
    private val boardGameService: BoardGameService
) {
    
    @Operation(summary = "보드게임 목록 조회", description = "등록된 모든 보드게임 목록을 조회합니다")
    @GetMapping
    fun getAllBoardGames(): ResponseEntity<List<BoardGameResponse>> {
        val boardGames = boardGameService.getAllBoardGames()
        val responses = boardGames.map { BoardGameResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "보드게임 상세 조회", description = "특정 보드게임의 상세 정보를 조회합니다")
    @GetMapping("/{id}")
    fun getBoardGameById(
        @Parameter(description = "보드게임 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<BoardGameResponse> {
        val boardGame = boardGameService.getBoardGameById(id)
        return ResponseEntity.ok(BoardGameResponse.from(boardGame))
    }
    
    @Operation(summary = "카테고리별 보드게임 검색", description = "특정 카테고리의 보드게임들을 조회합니다")
    @GetMapping("/search")
    fun getBoardGamesByCategory(
        @Parameter(description = "카테고리명", required = true)
        @RequestParam category: String
    ): ResponseEntity<List<BoardGameResponse>> {
        val boardGames = boardGameService.getBoardGamesByCategory(category)
        val responses = boardGames.map { BoardGameResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "보드게임 등록", description = "새로운 보드게임을 등록합니다")
    @PostMapping
    fun createBoardGame(
        @Valid @RequestBody request: BoardGameRequest
    ): ResponseEntity<BoardGameResponse> {
        val boardGame = boardGameService.createBoardGame(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(BoardGameResponse.from(boardGame))
    }
    
    @Operation(summary = "보드게임 수정", description = "기존 보드게임 정보를 수정합니다")
    @PutMapping("/{id}")
    fun updateBoardGame(
        @Parameter(description = "보드게임 ID", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody request: BoardGameRequest
    ): ResponseEntity<BoardGameResponse> {
        val boardGame = boardGameService.updateBoardGame(id, request)
        return ResponseEntity.ok(BoardGameResponse.from(boardGame))
    }
    
    @Operation(summary = "보드게임 삭제", description = "보드게임을 삭제합니다")
    @DeleteMapping("/{id}")
    fun deleteBoardGame(
        @Parameter(description = "보드게임 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        boardGameService.deleteBoardGame(id)
        return ResponseEntity.noContent().build()
    }
}
