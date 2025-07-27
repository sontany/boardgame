package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 한 턴 상태를 나타내는 도메인 객체
 * 
 * @property rollCount 현재 주사위를 굴린 횟수 (1-3)
 * @property currentDice 현재 주사위 상태
 */
data class Turn(
    val rollCount: Int = 0,
    val currentDice: DiceSet? = null
) {
    
    init {
        require(rollCount in 0..3) { "굴린 횟수는 0부터 3까지만 가능합니다. 입력값: $rollCount" }
        if (rollCount > 0) {
            requireNotNull(currentDice) { "주사위를 굴렸다면 currentDice가 있어야 합니다." }
        }
    }
    
    companion object {
        /**
         * 새로운 턴을 시작합니다.
         * 
         * @return 초기 상태의 Turn 객체
         */
        fun start(): Turn {
            return Turn()
        }
        
        /**
         * 최대 굴릴 수 있는 횟수
         */
        const val MAX_ROLLS = 3
    }
    
    /**
     * 주사위를 굴릴 수 있는지 확인합니다.
     * 
     * @return 굴릴 수 있으면 true, 아니면 false
     */
    fun canRoll(): Boolean {
        return rollCount < MAX_ROLLS
    }
    
    /**
     * 모든 주사위를 굴립니다.
     * 
     * @return 주사위가 굴려진 새로운 Turn 객체
     * @throws IllegalStateException 더 이상 굴릴 수 없는 경우
     */
    fun rollAll(): Turn {
        require(canRoll()) { "더 이상 주사위를 굴릴 수 없습니다. 현재 굴린 횟수: $rollCount" }
        
        return Turn(
            rollCount = rollCount + 1,
            currentDice = DiceSet.rollAll()
        )
    }
    
    /**
     * 지정된 인덱스의 주사위들을 다시 굴립니다.
     * 
     * @param indices 다시 굴릴 주사위의 인덱스들
     * @return 지정된 주사위들이 다시 굴려진 새로운 Turn 객체
     * @throws IllegalStateException 더 이상 굴릴 수 없거나 주사위가 없는 경우
     */
    fun reroll(indices: Set<Int>): Turn {
        require(canRoll()) { "더 이상 주사위를 굴릴 수 없습니다. 현재 굴린 횟수: $rollCount" }
        requireNotNull(currentDice) { "주사위를 먼저 굴려야 합니다." }
        
        return Turn(
            rollCount = rollCount + 1,
            currentDice = currentDice.reroll(indices)
        )
    }
    
    /**
     * 턴이 완료되었는지 확인합니다.
     * 
     * @return 3번 굴렸거나 주사위가 있으면 true
     */
    fun isComplete(): Boolean {
        return rollCount >= MAX_ROLLS || currentDice != null
    }
    
    /**
     * 점수를 기록할 수 있는지 확인합니다.
     * 
     * @return 주사위를 최소 1번 굴렸으면 true
     */
    fun canRecordScore(): Boolean {
        return rollCount > 0 && currentDice != null
    }
    
    /**
     * 현재 주사위로 특정 카테고리의 점수를 계산합니다.
     * 
     * @param category 점수를 계산할 카테고리
     * @return 계산된 점수
     * @throws IllegalStateException 주사위가 없는 경우
     */
    fun calculateScore(category: Category): Score {
        requireNotNull(currentDice) { "주사위를 먼저 굴려야 점수를 계산할 수 있습니다." }
        return Score.calculate(currentDice, category)
    }
}
