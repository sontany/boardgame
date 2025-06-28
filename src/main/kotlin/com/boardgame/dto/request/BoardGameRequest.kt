package com.boardgame.dto.request

import jakarta.validation.constraints.*

data class BoardGameRequest(
    @field:NotBlank(message = "게임 이름은 필수입니다")
    val name: String,
    
    @field:NotBlank(message = "게임 설명은 필수입니다")
    val description: String,
    
    @field:Min(value = 1, message = "최소 플레이어 수는 1명 이상이어야 합니다")
    val minPlayers: Int,
    
    @field:Min(value = 1, message = "최대 플레이어 수는 1명 이상이어야 합니다")
    val maxPlayers: Int,
    
    @field:Min(value = 1, message = "플레이 시간은 1분 이상이어야 합니다")
    val playTime: Int,
    
    @field:Min(value = 1, message = "난이도는 1 이상이어야 합니다")
    @field:Max(value = 5, message = "난이도는 5 이하여야 합니다")
    val difficulty: Int,
    
    @field:NotBlank(message = "카테고리는 필수입니다")
    val category: String,
    
    @field:NotBlank(message = "출판사는 필수입니다")
    val publisher: String,
    
    @field:Min(value = 1900, message = "출시년도는 1900년 이후여야 합니다")
    val releaseYear: Int,
    
    @field:DecimalMin(value = "0.0", message = "평점은 0.0 이상이어야 합니다")
    @field:DecimalMax(value = "5.0", message = "평점은 5.0 이하여야 합니다")
    val rating: Double
)
