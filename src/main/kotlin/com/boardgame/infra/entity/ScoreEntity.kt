package com.boardgame.infra.entity

import com.boardgame.application.yacht.Category
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 요트다이스 게임의 점수 정보를 저장하는 JPA 엔티티
 */
@Entity
@Table(
    name = "yacht_scores",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["game_id", "category"])
    ]
)
data class ScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "game_id", nullable = false)
    val gameId: Long,
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    val category: Category,
    
    @Column(name = "points", nullable = false)
    val points: Int,
    
    @Column(name = "recorded_at", nullable = false)
    val recordedAt: LocalDateTime = LocalDateTime.now()
) {
    
    init {
        require(points >= 0) { "점수는 0 이상이어야 합니다. 입력값: $points" }
    }
    
    companion object {
        /**
         * 도메인 Score 객체로부터 ScoreEntity를 생성합니다.
         * 
         * @param gameId 게임 ID
         * @param score 도메인 Score 객체
         * @return ScoreEntity
         */
        fun from(gameId: Long, score: com.boardgame.application.yacht.Score): ScoreEntity {
            return ScoreEntity(
                gameId = gameId,
                category = score.category,
                points = score.points,
                recordedAt = score.recordedAt
            )
        }
    }
    
    /**
     * 도메인 Score 객체로 변환합니다.
     * 
     * @return 도메인 Score 객체
     */
    fun toDomain(): com.boardgame.application.yacht.Score {
        return com.boardgame.application.yacht.Score(
            category = category,
            points = points,
            recordedAt = recordedAt
        )
    }
}
