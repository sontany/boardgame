package com.boardgame.dto.request

import jakarta.validation.constraints.*

data class GameSessionRequest(
    @field:NotNull(message = "보드게임 ID는 필수입니다")
    val boardGameId: Long,
    
    @field:NotEmpty(message = "플레이어 목록은 필수입니다")
    val playerIds: List<Long>
)

data class GameSessionStartRequest(
    @field:NotNull(message = "게임 세션 ID는 필수입니다")
    val sessionId: Long
)

data class GameSessionFinishRequest(
    @field:NotNull(message = "게임 세션 ID는 필수입니다")
    val sessionId: Long,
    
    val winnerId: Long?
)
