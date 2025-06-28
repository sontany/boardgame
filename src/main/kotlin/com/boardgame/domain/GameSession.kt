package com.boardgame.domain

import java.time.LocalDateTime

data class GameSession(
    val id: Long,
    val boardGameId: Long,
    val playerIds: List<Long>,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val winnerId: Long?,
    val status: GameStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class GameStatus {
    WAITING, PLAYING, FINISHED
}
