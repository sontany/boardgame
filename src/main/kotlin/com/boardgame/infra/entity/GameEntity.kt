package com.boardgame.infra.entity

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 요트다이스 게임 정보를 저장하는 JPA 엔티티
 */
@Entity
@Table(name = "yacht_games")
data class GameEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "player_name", nullable = false)
    val playerName: String,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: GameStatus = GameStatus.IN_PROGRESS,
    
    @Column(name = "current_turn", nullable = false)
    val currentTurn: Int = 1,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "completed_at")
    val completedAt: LocalDateTime? = null
) {
    
    /**
     * 게임 상태를 나타내는 enum
     */
    enum class GameStatus {
        IN_PROGRESS,    // 진행 중
        COMPLETED       // 완료
    }
    
    /**
     * 게임을 완료 상태로 변경합니다.
     * 
     * @return 완료된 게임 엔티티
     */
    fun complete(): GameEntity {
        return this.copy(
            status = GameStatus.COMPLETED,
            completedAt = LocalDateTime.now()
        )
    }
    
    /**
     * 다음 턴으로 진행합니다.
     * 
     * @return 턴이 증가된 게임 엔티티
     */
    fun nextTurn(): GameEntity {
        return this.copy(currentTurn = currentTurn + 1)
    }
}
