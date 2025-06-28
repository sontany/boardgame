package com.boardgame.controller

import com.boardgame.dto.request.PlayerRequest
import com.boardgame.dto.response.PlayerResponse
import com.boardgame.service.PlayerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "플레이어", description = "플레이어 관리 API")
@RestController
@RequestMapping("/api/players")
class PlayerController(
    private val playerService: PlayerService
) {
    
    @Operation(summary = "플레이어 목록 조회", description = "등록된 모든 플레이어 목록을 조회합니다")
    @GetMapping
    fun getAllPlayers(): ResponseEntity<List<PlayerResponse>> {
        val players = playerService.getAllPlayers()
        val responses = players.map { PlayerResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "플레이어 상세 조회", description = "특정 플레이어의 상세 정보를 조회합니다")
    @GetMapping("/{id}")
    fun getPlayerById(
        @Parameter(description = "플레이어 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<PlayerResponse> {
        val player = playerService.getPlayerById(id)
        return ResponseEntity.ok(PlayerResponse.from(player))
    }
    
    @Operation(summary = "플레이어 등록", description = "새로운 플레이어를 등록합니다")
    @PostMapping
    fun createPlayer(
        @Valid @RequestBody request: PlayerRequest
    ): ResponseEntity<PlayerResponse> {
        val player = playerService.createPlayer(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponse.from(player))
    }
    
    @Operation(summary = "플레이어 정보 수정", description = "기존 플레이어 정보를 수정합니다")
    @PutMapping("/{id}")
    fun updatePlayer(
        @Parameter(description = "플레이어 ID", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody request: PlayerRequest
    ): ResponseEntity<PlayerResponse> {
        val player = playerService.updatePlayer(id, request)
        return ResponseEntity.ok(PlayerResponse.from(player))
    }
    
    @Operation(summary = "플레이어 삭제", description = "플레이어를 삭제합니다")
    @DeleteMapping("/{id}")
    fun deletePlayer(
        @Parameter(description = "플레이어 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        playerService.deletePlayer(id)
        return ResponseEntity.noContent().build()
    }
}
