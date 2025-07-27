package com.boardgame.infra.repository

import com.boardgame.infra.entity.GameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * 요트다이스 게임 엔티티에 대한 Repository
 */
@Repository
interface GameRepository : JpaRepository<GameEntity, Long> {
    
    /**
     * 진행 중인 게임들을 조회합니다.
     * 
     * @return 진행 중인 게임 리스트
     */
    fun findByStatus(status: GameEntity.GameStatus): List<GameEntity>
    
    /**
     * 특정 플레이어의 게임들을 조회합니다.
     * 
     * @param playerName 플레이어 이름
     * @return 해당 플레이어의 게임 리스트
     */
    fun findByPlayerNameOrderByCreatedAtDesc(playerName: String): List<GameEntity>
    
    /**
     * 완료된 게임의 개수를 조회합니다.
     * 
     * @return 완료된 게임 개수
     */
    @Query("SELECT COUNT(g) FROM GameEntity g WHERE g.status = 'COMPLETED'")
    fun countCompletedGames(): Long
}
