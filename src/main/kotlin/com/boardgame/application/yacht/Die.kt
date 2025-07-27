package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 개별 주사위를 나타내는 도메인 객체
 * 
 * @property value 주사위 값 (1-6)
 */
data class Die(val value: Int) {
    
    init {
        require(value in 1..6) { "주사위 값은 1부터 6까지만 가능합니다. 입력값: $value" }
    }
    
    companion object {
        /**
         * 랜덤한 주사위 값으로 Die 객체를 생성합니다.
         * 
         * @return 1-6 사이의 랜덤한 값을 가진 Die 객체
         */
        fun roll(): Die {
            return Die((1..6).random())
        }
        
        /**
         * 지정된 값으로 Die 객체를 생성합니다.
         * 
         * @param value 주사위 값 (1-6)
         * @return 지정된 값을 가진 Die 객체
         */
        fun of(value: Int): Die {
            return Die(value)
        }
    }
    
    /**
     * 주사위 값이 지정된 값과 같은지 확인합니다.
     * 
     * @param target 비교할 값
     * @return 같으면 true, 다르면 false
     */
    fun isValue(target: Int): Boolean {
        return value == target
    }
    
    /**
     * 주사위를 다시 굴립니다.
     * 
     * @return 새로운 랜덤 값을 가진 Die 객체
     */
    fun reroll(): Die {
        return roll()
    }
}
