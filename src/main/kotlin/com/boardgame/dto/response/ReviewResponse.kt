package com.boardgame.dto.response

import com.boardgame.domain.Review
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val boardGameId: Long,
    val playerId: Long,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                id = review.id,
                boardGameId = review.boardGameId,
                playerId = review.playerId,
                rating = review.rating,
                comment = review.comment,
                createdAt = review.createdAt,
                updatedAt = review.updatedAt
            )
        }
    }
}
