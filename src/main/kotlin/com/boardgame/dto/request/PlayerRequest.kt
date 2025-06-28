package com.boardgame.dto.request

import jakarta.validation.constraints.*

data class PlayerRequest(
    @field:NotBlank(message = "닉네임은 필수입니다")
    @field:Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
    val nickname: String,
    
    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이어야 합니다")
    val email: String,
    
    @field:Min(value = 1, message = "레벨은 1 이상이어야 합니다")
    val level: Int,
    
    @field:Min(value = 0, message = "총 게임 플레이 횟수는 0 이상이어야 합니다")
    val totalGamesPlayed: Int,
    
    val favoriteCategory: String?
)
