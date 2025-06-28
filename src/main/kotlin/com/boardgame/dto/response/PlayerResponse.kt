package com.boardgame.dto.response

import com.boardgame.domain.Player
import java.time.LocalDateTime

data class PlayerResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val level: Int,
    val totalGamesPlayed: Int,
    val favoriteCategory: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(player: Player): PlayerResponse {
            return PlayerResponse(
                id = player.id,
                nickname = player.nickname,
                email = player.email,
                level = player.level,
                totalGamesPlayed = player.totalGamesPlayed,
                favoriteCategory = player.favoriteCategory,
                createdAt = player.createdAt,
                updatedAt = player.updatedAt
            )
        }
    }
}
