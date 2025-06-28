package com.boardgame.controller

import com.boardgame.dto.request.GameSessionRequest
import com.boardgame.dto.response.GameSessionResponse
import com.boardgame.service.GameSessionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "게임 세션", description = "게임 세션 관리 API")
@RestController
@RequestMapping("/api/game-sessions")
class GameSessionController(
    private val gameSessionService: GameSessionService
) {
    
    @Operation(summary = "게임 세션 목록 조회", description = "등록된 모든 게임 세션 목록을 조회합니다")
    @GetMapping
    fun getAllGameSessions(): ResponseEntity<List<GameSessionResponse>> {
        val gameSessions = gameSessionService.getAllGameSessions()
        val responses = gameSessions.map { GameSessionResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "게임 세션 상세 조회", description = "특정 게임 세션의 상세 정보를 조회합니다")
    @GetMapping("/{id}")
    fun getGameSessionById(
        @Parameter(description = "게임 세션 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<GameSessionResponse> {
        val gameSession = gameSessionService.getGameSessionById(id)
        return ResponseEntity.ok(GameSessionResponse.from(gameSession))
    }
    
    @Operation(summary = "게임 세션 생성", description = "새로운 게임 세션을 생성합니다")
    @PostMapping
    fun createGameSession(
        @Valid @RequestBody request: GameSessionRequest
    ): ResponseEntity<GameSessionResponse> {
        val gameSession = gameSessionService.createGameSession(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(GameSessionResponse.from(gameSession))
    }
    
    @Operation(summary = "게임 시작", description = "대기 중인 게임 세션을 시작합니다")
    @PutMapping("/{id}/start")
    fun startGameSession(
        @Parameter(description = "게임 세션 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<GameSessionResponse> {
        val gameSession = gameSessionService.startGameSession(id)
        return ResponseEntity.ok(GameSessionResponse.from(gameSession))
    }
    
    @Operation(summary = "게임 종료", description = "진행 중인 게임 세션을 종료합니다")
    @PutMapping("/{id}/finish")
    fun finishGameSession(
        @Parameter(description = "게임 세션 ID", required = true)
        @PathVariable id: Long,
        @Parameter(description = "승자 플레이어 ID (선택사항)")
        @RequestParam(required = false) winnerId: Long?
    ): ResponseEntity<GameSessionResponse> {
        val gameSession = gameSessionService.finishGameSession(id, winnerId)
        return ResponseEntity.ok(GameSessionResponse.from(gameSession))
    }
    
    @Operation(summary = "게임 세션 삭제", description = "게임 세션을 삭제합니다")
    @DeleteMapping("/{id}")
    fun deleteGameSession(
        @Parameter(description = "게임 세션 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        gameSessionService.deleteGameSession(id)
        return ResponseEntity.noContent().build()
    }
}
