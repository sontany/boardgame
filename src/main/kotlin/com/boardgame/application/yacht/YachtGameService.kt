package com.boardgame.application.yacht

import com.boardgame.infra.entity.GameEntity
import com.boardgame.infra.entity.ScoreEntity
import com.boardgame.infra.entity.TurnEntity
import com.boardgame.infra.repository.GameRepository
import com.boardgame.infra.repository.ScoreRepository
import com.boardgame.infra.repository.TurnRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 요트다이스 게임의 비즈니스 로직을 처리하는 서비스
 */
@Service
@Transactional
class YachtGameService(
    private val gameRepository: GameRepository,
    private val scoreRepository: ScoreRepository,
    private val turnRepository: TurnRepository
) {
    
    /**
     * 새로운 게임을 생성합니다.
     * 
     * @param playerName 플레이어 이름
     * @return 생성된 게임 엔티티
     */
    fun createGame(playerName: String): GameEntity {
        val gameEntity = GameEntity(playerName = playerName)
        return gameRepository.save(gameEntity)
    }
    
    /**
     * 게임을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 게임 엔티티
     * @throws IllegalArgumentException 게임을 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    fun getGame(gameId: Long): GameEntity {
        return gameRepository.findById(gameId)
            .orElseThrow { IllegalArgumentException("게임을 찾을 수 없습니다. ID: $gameId") }
    }
    
    /**
     * 게임의 현재 상태를 도메인 객체로 재구성합니다.
     * 
     * @param gameId 게임 ID
     * @return 도메인 Game 객체
     */
    @Transactional(readOnly = true)
    fun getGameState(gameId: Long): Game {
        val gameEntity = getGame(gameId)
        val scores = scoreRepository.findByGameIdOrderByRecordedAt(gameId)
        val currentTurnEntity = turnRepository.findCurrentTurn(gameId)
        
        // ScoreCard 재구성
        val scoreCard = ScoreCard()
        scores.forEach { scoreEntity ->
            scoreCard.recordScore(scoreEntity.toDomain())
        }
        
        // Player 재구성
        val player = Player(gameEntity.playerName, scoreCard)
        
        // 현재 Turn 재구성
        val currentTurn = currentTurnEntity?.toDomain() ?: Turn.start()
        
        return Game(
            id = gameEntity.id,
            player = player,
            currentTurn = currentTurn,
            turnNumber = gameEntity.currentTurn,
            isCompleted = gameEntity.status == GameEntity.GameStatus.COMPLETED
        )
    }
    
    /**
     * 주사위를 굴립니다.
     * 
     * @param gameId 게임 ID
     * @param rerollIndices 다시 굴릴 주사위 인덱스들
     * @return 업데이트된 도메인 Game 객체
     */
    fun rollDice(gameId: Long, rerollIndices: Set<Int> = emptySet()): Game {
        val game = getGameState(gameId)
        require(!game.isCompleted) { "완료된 게임에서는 주사위를 굴릴 수 없습니다." }
        
        val updatedGame = game.rollDice(rerollIndices)
        
        // 턴 정보 저장
        val turnEntity = TurnEntity.from(gameId, game.turnNumber, updatedGame.currentTurn)
        turnRepository.save(turnEntity)
        
        return updatedGame
    }
    
    /**
     * 점수를 기록하고 다음 턴으로 진행합니다.
     * 
     * @param gameId 게임 ID
     * @param category 점수를 기록할 카테고리
     * @return 업데이트된 도메인 Game 객체
     */
    fun recordScore(gameId: Long, category: Category): Game {
        val game = getGameState(gameId)
        require(!game.isCompleted) { "완료된 게임에서는 점수를 기록할 수 없습니다." }
        require(game.currentTurn.canRecordScore()) { "점수를 기록할 수 없는 상태입니다." }
        
        val updatedGame = game.recordScoreAndNextTurn(category)
        
        // 점수 저장
        val score = game.currentTurn.calculateScore(category)
        val scoreEntity = ScoreEntity.from(gameId, score)
        scoreRepository.save(scoreEntity)
        
        // 게임 상태 업데이트
        val gameEntity = getGame(gameId)
        val updatedGameEntity = if (updatedGame.isCompleted) {
            gameEntity.complete()
        } else {
            gameEntity.nextTurn()
        }
        gameRepository.save(updatedGameEntity)
        
        return updatedGame
    }
    
    /**
     * 게임의 점수판을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 점수판 정보
     */
    @Transactional(readOnly = true)
    fun getScoreCard(gameId: Long): ScoreCard {
        val scores = scoreRepository.findByGameIdOrderByRecordedAt(gameId)
        val scoreCard = ScoreCard()
        
        scores.forEach { scoreEntity ->
            scoreCard.recordScore(scoreEntity.toDomain())
        }
        
        return scoreCard
    }
}
