package com.boardgame.infra.repository

import com.boardgame.infra.entity.TurnEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * 요트다이스 턴 엔티티에 대한 Repository
 */
@Repository
interface TurnRepository : JpaRepository<TurnEntity, Long> {
    
    /**
     * 특정 게임의 모든 턴을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 해당 게임의 턴 리스트 (턴 번호 순)
     */
    fun findByGameIdOrderByTurnNumber(gameId: Long): List<TurnEntity>
    
    /**
     * 특정 게임의 특정 턴을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @param turnNumber 턴 번호
     * @return 해당 턴, 없으면 null
     */
    fun findByGameIdAndTurnNumber(gameId: Long, turnNumber: Int): TurnEntity?
    
    /**
     * 특정 게임의 현재 턴을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 가장 최근 턴, 없으면 null
     */
    @Query("SELECT t FROM TurnEntity t WHERE t.gameId = :gameId ORDER BY t.turnNumber DESC LIMIT 1")
    fun findCurrentTurn(gameId: Long): TurnEntity?
    
    /**
     * 특정 게임의 완료된 턴 개수를 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 완료된 턴 개수
     */
    @Query("SELECT COUNT(t) FROM TurnEntity t WHERE t.gameId = :gameId AND t.completedAt IS NOT NULL")
    fun countCompletedTurns(gameId: Long): Long
}
