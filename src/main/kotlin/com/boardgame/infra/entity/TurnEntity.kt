package com.boardgame.infra.entity

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 요트다이스 게임의 턴 정보를 저장하는 JPA 엔티티
 */
@Entity
@Table(name = "yacht_turns")
data class TurnEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(name = "game_id", nullable = false)
    val gameId: Long,
    
    @Column(name = "turn_number", nullable = false)
    val turnNumber: Int,
    
    @Column(name = "roll_count", nullable = false)
    val rollCount: Int = 0,
    
    @Column(name = "dice_values")
    val diceValues: String? = null, // "1,2,3,4,5" 형태로 저장
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "completed_at")
    val completedAt: LocalDateTime? = null
) {
    
    init {
        require(rollCount in 0..3) { "굴린 횟수는 0부터 3까지만 가능합니다. 입력값: $rollCount" }
        require(turnNumber > 0) { "턴 번호는 1 이상이어야 합니다. 입력값: $turnNumber" }
    }
    
    companion object {
        /**
         * 도메인 Turn 객체로부터 TurnEntity를 생성합니다.
         * 
         * @param gameId 게임 ID
         * @param turnNumber 턴 번호
         * @param turn 도메인 Turn 객체
         * @return TurnEntity
         */
        fun from(gameId: Long, turnNumber: Int, turn: com.boardgame.application.yacht.Turn): TurnEntity {
            val diceValues = turn.currentDice?.values()?.joinToString(",")
            
            return TurnEntity(
                gameId = gameId,
                turnNumber = turnNumber,
                rollCount = turn.rollCount,
                diceValues = diceValues
            )
        }
    }
    
    /**
     * 주사위 값들을 리스트로 파싱합니다.
     * 
     * @return 주사위 값 리스트, 없으면 null
     */
    fun parseDiceValues(): List<Int>? {
        return diceValues?.split(",")?.map { it.toInt() }
    }
    
    /**
     * 도메인 Turn 객체로 변환합니다.
     * 
     * @return 도메인 Turn 객체
     */
    fun toDomain(): com.boardgame.application.yacht.Turn {
        val diceSet = parseDiceValues()?.let { values ->
            com.boardgame.application.yacht.DiceSet.of(*values.toIntArray())
        }
        
        return com.boardgame.application.yacht.Turn(
            rollCount = rollCount,
            currentDice = diceSet
        )
    }
    
    /**
     * 턴을 완료 상태로 변경합니다.
     * 
     * @return 완료된 턴 엔티티
     */
    fun complete(): TurnEntity {
        return this.copy(completedAt = LocalDateTime.now())
    }
}
