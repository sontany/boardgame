package com.boardgame.application.yacht

import java.time.LocalDateTime

/**
 * 요트다이스 게임의 개별 점수 정보를 나타내는 도메인 객체
 * 
 * @property category 점수 카테고리
 * @property points 획득한 점수
 * @property recordedAt 점수 기록 시간
 */
data class Score(
    val category: Category,
    val points: Int,
    val recordedAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(points >= 0) { "점수는 0 이상이어야 합니다. 입력값: $points" }
    }
    
    companion object {
        /**
         * 주사위 집합과 카테고리로 점수를 생성합니다.
         * 
         * @param diceSet 점수를 계산할 주사위 집합
         * @param category 점수 카테고리
         * @return 계산된 점수 객체
         */
        fun calculate(diceSet: DiceSet, category: Category): Score {
            val points = ScoreCalculator.calculateScore(diceSet, category)
            return Score(category, points)
        }
        
        /**
         * 0점 점수를 생성합니다.
         * 
         * @param category 점수 카테고리
         * @return 0점 점수 객체
         */
        fun zero(category: Category): Score {
            return Score(category, 0)
        }
    }
    
    /**
     * 점수가 0점인지 확인합니다.
     * 
     * @return 0점이면 true, 아니면 false
     */
    fun isZero(): Boolean {
        return points == 0
    }
    
    /**
     * 상위 섹션 점수인지 확인합니다.
     * 
     * @return 상위 섹션이면 true, 아니면 false
     */
    fun isUpperSection(): Boolean {
        return category.isUpperSection
    }
}
