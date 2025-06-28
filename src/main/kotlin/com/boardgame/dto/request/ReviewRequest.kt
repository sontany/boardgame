package com.boardgame.dto.request

import jakarta.validation.constraints.*

data class ReviewRequest(
    @field:NotNull(message = "보드게임 ID는 필수입니다")
    val boardGameId: Long,
    
    @field:NotNull(message = "플레이어 ID는 필수입니다")
    val playerId: Long,
    
    @field:Min(value = 1, message = "평점은 1 이상이어야 합니다")
    @field:Max(value = 5, message = "평점은 5 이하여야 합니다")
    val rating: Int,
    
    @field:NotBlank(message = "리뷰 내용은 필수입니다")
    @field:Size(max = 1000, message = "리뷰 내용은 1000자 이하여야 합니다")
    val comment: String
)
