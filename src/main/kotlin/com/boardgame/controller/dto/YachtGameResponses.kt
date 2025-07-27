package com.boardgame.controller.dto

import com.boardgame.application.yacht.Category
import com.boardgame.infra.entity.GameEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 게임 응답 DTO
 */
@Schema(description = "게임 정보 응답")
data class GameResponse(
    @Schema(description = "게임 ID", example = "1")
    val gameId: Long,
    
    @Schema(description = "플레이어 이름", example = "홍길동")
    val playerName: String,
    
    @Schema(description = "게임 상태", example = "IN_PROGRESS")
    val status: String,
    
    @Schema(description = "현재 턴", example = "5")
    val currentTurn: Int,
    
    @Schema(description = "게임 생성 시간")
    val createdAt: LocalDateTime,
    
    @Schema(description = "게임 완료 시간")
    val completedAt: LocalDateTime? = null
) {
    companion object {
        fun from(gameEntity: GameEntity): GameResponse {
            return GameResponse(
                gameId = gameEntity.id,
                playerName = gameEntity.playerName,
                status = gameEntity.status.name,
                currentTurn = gameEntity.currentTurn,
                createdAt = gameEntity.createdAt,
                completedAt = gameEntity.completedAt
            )
        }
    }
}

/**
 * 주사위 굴리기 응답 DTO
 */
@Schema(description = "주사위 굴리기 응답")
data class DiceRollResponse(
    @Schema(description = "주사위 값들", example = "[1, 2, 3, 4, 5]")
    val diceValues: List<Int>,
    
    @Schema(description = "굴린 횟수", example = "2")
    val rollCount: Int,
    
    @Schema(description = "더 굴릴 수 있는지 여부", example = "true")
    val canRollMore: Boolean,
    
    @Schema(description = "점수 기록 가능 여부", example = "true")
    val canRecordScore: Boolean
)

/**
 * 점수 정보 DTO
 */
@Schema(description = "점수 정보")
data class ScoreInfo(
    @Schema(description = "카테고리", example = "ONES")
    val category: Category,
    
    @Schema(description = "점수", example = "3")
    val points: Int,
    
    @Schema(description = "기록 시간")
    val recordedAt: LocalDateTime
)

/**
 * 점수판 응답 DTO
 */
@Schema(description = "점수판 응답")
data class ScoreCardResponse(
    @Schema(description = "기록된 점수들")
    val scores: Map<Category, ScoreInfo>,
    
    @Schema(description = "상위 섹션 총점", example = "45")
    val upperSectionTotal: Int,
    
    @Schema(description = "상위 섹션 보너스", example = "0")
    val upperSectionBonus: Int,
    
    @Schema(description = "하위 섹션 총점", example = "120")
    val lowerSectionTotal: Int,
    
    @Schema(description = "총점", example = "165")
    val totalScore: Int,
    
    @Schema(description = "게임 완료 여부", example = "false")
    val isComplete: Boolean,
    
    @Schema(description = "사용 가능한 카테고리들")
    val availableCategories: List<Category>
)
