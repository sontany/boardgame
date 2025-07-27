package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 5개 주사위 집합을 나타내는 도메인 객체
 * 
 * @property dice 5개의 주사위 리스트
 */
data class DiceSet(val dice: List<Die>) {
    
    init {
        require(dice.size == 5) { "주사위는 정확히 5개여야 합니다. 현재 개수: ${dice.size}" }
    }
    
    companion object {
        /**
         * 5개의 주사위를 모두 굴려서 DiceSet을 생성합니다.
         * 
         * @return 랜덤한 값들을 가진 DiceSet
         */
        fun rollAll(): DiceSet {
            return DiceSet(List(5) { Die.roll() })
        }
        
        /**
         * 지정된 값들로 DiceSet을 생성합니다.
         * 
         * @param values 5개의 주사위 값 배열
         * @return 지정된 값들을 가진 DiceSet
         */
        fun of(vararg values: Int): DiceSet {
            require(values.size == 5) { "정확히 5개의 값이 필요합니다. 입력된 개수: ${values.size}" }
            return DiceSet(values.map { Die.of(it) })
        }
    }
    
    /**
     * 지정된 인덱스의 주사위들을 다시 굴립니다.
     * 
     * @param indices 다시 굴릴 주사위의 인덱스들 (0-4)
     * @return 지정된 주사위들이 다시 굴려진 새로운 DiceSet
     */
    fun reroll(indices: Set<Int>): DiceSet {
        require(indices.all { it in 0..4 }) { "인덱스는 0부터 4까지만 가능합니다." }
        
        val newDice = dice.mapIndexed { index, die ->
            if (index in indices) die.reroll() else die
        }
        return DiceSet(newDice)
    }
    
    /**
     * 모든 주사위를 다시 굴립니다.
     * 
     * @return 모든 주사위가 다시 굴려진 새로운 DiceSet
     */
    fun rerollAll(): DiceSet {
        return rollAll()
    }
    
    /**
     * 주사위 값들을 리스트로 반환합니다.
     * 
     * @return 주사위 값들의 리스트
     */
    fun values(): List<Int> {
        return dice.map { it.value }
    }
    
    /**
     * 지정된 값의 주사위 개수를 반환합니다.
     * 
     * @param value 찾을 주사위 값
     * @return 해당 값을 가진 주사위의 개수
     */
    fun countOf(value: Int): Int {
        return dice.count { it.isValue(value) }
    }
    
    /**
     * 모든 주사위 값의 합을 반환합니다.
     * 
     * @return 주사위 값들의 총합
     */
    fun sum(): Int {
        return dice.sumOf { it.value }
    }
    
    /**
     * 주사위 값들을 정렬된 리스트로 반환합니다.
     * 
     * @return 정렬된 주사위 값들의 리스트
     */
    fun sortedValues(): List<Int> {
        return values().sorted()
    }
    
    /**
     * 각 주사위 값의 개수를 맵으로 반환합니다.
     * 
     * @return 주사위 값을 키로, 개수를 값으로 하는 맵
     */
    fun valueCounts(): Map<Int, Int> {
        return values().groupingBy { it }.eachCount()
    }
}
