package com.boardgame.dto.response

import com.boardgame.domain.BoardGame
import java.time.LocalDateTime

data class BoardGameResponse(
    val id: Long,
    val name: String,
    val description: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playTime: Int,
    val difficulty: Int,
    val category: String,
    val publisher: String,
    val releaseYear: Int,
    val rating: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(boardGame: BoardGame): BoardGameResponse {
            return BoardGameResponse(
                id = boardGame.id,
                name = boardGame.name,
                description = boardGame.description,
                minPlayers = boardGame.minPlayers,
                maxPlayers = boardGame.maxPlayers,
                playTime = boardGame.playTime,
                difficulty = boardGame.difficulty,
                category = boardGame.category,
                publisher = boardGame.publisher,
                releaseYear = boardGame.releaseYear,
                rating = boardGame.rating,
                createdAt = boardGame.createdAt,
                updatedAt = boardGame.updatedAt
            )
        }
    }
}
