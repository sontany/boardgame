package com.boardgame.domain

import java.time.LocalDateTime

data class Player(
    val id: Long,
    val nickname: String,
    val email: String,
    val level: Int,
    val totalGamesPlayed: Int,
    val favoriteCategory: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
