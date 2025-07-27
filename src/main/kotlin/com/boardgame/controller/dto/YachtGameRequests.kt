package com.boardgame.controller.dto

import com.boardgame.application.yacht.Category
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 새 게임 생성 요청 DTO
 */
@Schema(description = "새 게임 생성 요청")
data class CreateGameRequest(
    @Schema(description = "플레이어 이름", example = "홍길동")
    val playerName: String
) {
    init {
        require(playerName.isNotBlank()) { "플레이어 이름은 비어있을 수 없습니다." }
    }
}

/**
 * 주사위 굴리기 요청 DTO
 */
@Schema(description = "주사위 굴리기 요청")
data class RollDiceRequest(
    @Schema(description = "다시 굴릴 주사위 인덱스들 (0-4)", example = "[0, 2, 4]")
    val rerollIndices: Set<Int> = emptySet()
) {
    init {
        require(rerollIndices.all { it in 0..4 }) { "주사위 인덱스는 0부터 4까지만 가능합니다." }
    }
}

/**
 * 점수 기록 요청 DTO
 */
@Schema(description = "점수 기록 요청")
data class RecordScoreRequest(
    @Schema(description = "점수를 기록할 카테고리", example = "ONES")
    val category: Category
)
