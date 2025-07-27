package com.boardgame.controller

import com.boardgame.application.yacht.Category
import com.boardgame.application.yacht.YachtGameService
import com.boardgame.controller.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 요트다이스 게임 API 컨트롤러
 */
@RestController
@RequestMapping("/api/yacht")
@Tag(name = "Yacht Dice Game", description = "요트다이스 게임 API")
class YachtGameController(
    private val yachtGameService: YachtGameService
) {
    
    /**
     * 새 게임을 생성합니다.
     */
    @PostMapping("/games")
    @Operation(summary = "새 게임 생성", description = "새로운 요트다이스 게임을 생성합니다.")
    fun createGame(@RequestBody request: CreateGameRequest): ResponseEntity<GameResponse> {
        val gameEntity = yachtGameService.createGame(request.playerName)
        val response = GameResponse.from(gameEntity)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
    
    /**
     * 게임 상태를 조회합니다.
     */
    @GetMapping("/games/{gameId}")
    @Operation(summary = "게임 상태 조회", description = "게임의 현재 상태를 조회합니다.")
    fun getGame(
        @Parameter(description = "게임 ID") @PathVariable gameId: Long
    ): ResponseEntity<GameResponse> {
        val gameEntity = yachtGameService.getGame(gameId)
        val response = GameResponse.from(gameEntity)
        return ResponseEntity.ok(response)
    }
    
    /**
     * 주사위를 굴립니다.
     */
    @PostMapping("/games/{gameId}/roll")
    @Operation(summary = "주사위 굴리기", description = "주사위를 굴리거나 선택된 주사위만 다시 굴립니다.")
    fun rollDice(
        @Parameter(description = "게임 ID") @PathVariable gameId: Long,
        @RequestBody request: RollDiceRequest
    ): ResponseEntity<DiceRollResponse> {
        val game = yachtGameService.rollDice(gameId, request.rerollIndices)
        
        val response = DiceRollResponse(
            diceValues = game.currentTurn.currentDice?.values() ?: emptyList(),
            rollCount = game.currentTurn.rollCount,
            canRollMore = game.currentTurn.canRoll(),
            canRecordScore = game.currentTurn.canRecordScore()
        )
        
        return ResponseEntity.ok(response)
    }
    
    /**
     * 점수를 기록합니다.
     */
    @PostMapping("/games/{gameId}/score")
    @Operation(summary = "점수 기록", description = "현재 주사위로 선택한 카테고리에 점수를 기록합니다.")
    fun recordScore(
        @Parameter(description = "게임 ID") @PathVariable gameId: Long,
        @RequestBody request: RecordScoreRequest
    ): ResponseEntity<ScoreCardResponse> {
        val game = yachtGameService.recordScore(gameId, request.category)
        val scoreCard = game.player.scoreCard
        
        val scores = scoreCard.getAllScores().mapValues { (_, score) ->
            ScoreInfo(
                category = score.category,
                points = score.points,
                recordedAt = score.recordedAt
            )
        }
        
        val response = ScoreCardResponse(
            scores = scores,
            upperSectionTotal = scoreCard.getUpperSectionTotal(),
            upperSectionBonus = scoreCard.getUpperSectionBonus(),
            lowerSectionTotal = scoreCard.getLowerSectionTotal(),
            totalScore = scoreCard.getTotalScore(),
            isComplete = scoreCard.isComplete(),
            availableCategories = scoreCard.getAvailableCategories()
        )
        
        return ResponseEntity.ok(response)
    }
    
    /**
     * 점수판을 조회합니다.
     */
    @GetMapping("/games/{gameId}/scorecard")
    @Operation(summary = "점수판 조회", description = "게임의 현재 점수판을 조회합니다.")
    fun getScoreCard(
        @Parameter(description = "게임 ID") @PathVariable gameId: Long
    ): ResponseEntity<ScoreCardResponse> {
        val scoreCard = yachtGameService.getScoreCard(gameId)
        
        val scores = scoreCard.getAllScores().mapValues { (_, score) ->
            ScoreInfo(
                category = score.category,
                points = score.points,
                recordedAt = score.recordedAt
            )
        }
        
        val response = ScoreCardResponse(
            scores = scores,
            upperSectionTotal = scoreCard.getUpperSectionTotal(),
            upperSectionBonus = scoreCard.getUpperSectionBonus(),
            lowerSectionTotal = scoreCard.getLowerSectionTotal(),
            totalScore = scoreCard.getTotalScore(),
            isComplete = scoreCard.isComplete(),
            availableCategories = scoreCard.getAvailableCategories()
        )
        
        return ResponseEntity.ok(response)
    }
    
    /**
     * 점수 미리보기를 제공합니다.
     */
    @GetMapping("/games/{gameId}/preview/{category}")
    @Operation(summary = "점수 미리보기", description = "현재 주사위로 특정 카테고리의 점수를 미리 계산합니다.")
    fun previewScore(
        @Parameter(description = "게임 ID") @PathVariable gameId: Long,
        @Parameter(description = "카테고리") @PathVariable category: Category
    ): ResponseEntity<Map<String, Int>> {
        val game = yachtGameService.getGameState(gameId)
        val previewScore = game.previewScore(category)
        
        return ResponseEntity.ok(mapOf("previewScore" to previewScore))
    }
}
