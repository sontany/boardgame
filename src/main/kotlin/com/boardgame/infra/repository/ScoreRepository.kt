package com.boardgame.infra.repository

import com.boardgame.application.yacht.Category
import com.boardgame.infra.entity.ScoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * 요트다이스 점수 엔티티에 대한 Repository
 */
@Repository
interface ScoreRepository : JpaRepository<ScoreEntity, Long> {
    
    /**
     * 특정 게임의 모든 점수를 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 해당 게임의 점수 리스트
     */
    fun findByGameIdOrderByRecordedAt(gameId: Long): List<ScoreEntity>
    
    /**
     * 특정 게임의 특정 카테고리 점수를 조회합니다.
     * 
     * @param gameId 게임 ID
     * @param category 카테고리
     * @return 해당 점수, 없으면 null
     */
    fun findByGameIdAndCategory(gameId: Long, category: Category): ScoreEntity?
    
    /**
     * 특정 게임의 상위 섹션 점수들을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 상위 섹션 점수 리스트
     */
    @Query("SELECT s FROM ScoreEntity s WHERE s.gameId = :gameId AND s.category IN ('ONES', 'TWOS', 'THREES', 'FOURS', 'FIVES', 'SIXES')")
    fun findUpperSectionScores(gameId: Long): List<ScoreEntity>
    
    /**
     * 특정 게임의 하위 섹션 점수들을 조회합니다.
     * 
     * @param gameId 게임 ID
     * @return 하위 섹션 점수 리스트
     */
    @Query("SELECT s FROM ScoreEntity s WHERE s.gameId = :gameId AND s.category NOT IN ('ONES', 'TWOS', 'THREES', 'FOURS', 'FIVES', 'SIXES')")
    fun findLowerSectionScores(gameId: Long): List<ScoreEntity>
    
    /**
     * 특정 게임의 총점을 계산합니다.
     * 
     * @param gameId 게임 ID
     * @return 총점
     */
    @Query("SELECT COALESCE(SUM(s.points), 0) FROM ScoreEntity s WHERE s.gameId = :gameId")
    fun calculateTotalScore(gameId: Long): Int
}
