package com.boardgame.dto.response

import com.boardgame.domain.GameSession
import com.boardgame.domain.GameStatus
import java.time.LocalDateTime

data class GameSessionResponse(
    val id: Long,
    val boardGameId: Long,
    val playerIds: List<Long>,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val winnerId: Long?,
    val status: GameStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(gameSession: GameSession): GameSessionResponse {
            return GameSessionResponse(
                id = gameSession.id,
                boardGameId = gameSession.boardGameId,
                playerIds = gameSession.playerIds,
                startTime = gameSession.startTime,
                endTime = gameSession.endTime,
                winnerId = gameSession.winnerId,
                status = gameSession.status,
                createdAt = gameSession.createdAt,
                updatedAt = gameSession.updatedAt
            )
        }
    }
}
