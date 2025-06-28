package com.boardgame.domain

import java.time.LocalDateTime

data class BoardGame(
    val id: Long,
    val name: String,
    val description: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playTime: Int, // 분 단위
    val difficulty: Int, // 1-5 난이도
    val category: String,
    val publisher: String,
    val releaseYear: Int,
    val rating: Double,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
