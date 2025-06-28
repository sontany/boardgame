package com.boardgame.domain

import java.time.LocalDateTime

data class Review(
    val id: Long,
    val boardGameId: Long,
    val playerId: Long,
    val rating: Int, // 1-5
    val comment: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
