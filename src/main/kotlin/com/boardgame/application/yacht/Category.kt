package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 점수 카테고리를 나타내는 enum
 */
enum class Category(
    val displayName: String,
    val description: String,
    val isUpperSection: Boolean = false
) {
    // 상위 섹션 (1-6)
    ONES("Ones", "1의 개수 × 1점", true),
    TWOS("Twos", "2의 개수 × 2점", true),
    THREES("Threes", "3의 개수 × 3점", true),
    FOURS("Fours", "4의 개수 × 4점", true),
    FIVES("Fives", "5의 개수 × 5점", true),
    SIXES("Sixes", "6의 개수 × 6점", true),
    
    // 하위 섹션
    THREE_OF_A_KIND("Three of a Kind", "같은 숫자 3개 이상 - 모든 주사위 합"),
    FOUR_OF_A_KIND("Four of a Kind", "같은 숫자 4개 이상 - 모든 주사위 합"),
    FULL_HOUSE("Full House", "3개 + 2개 조합 - 25점"),
    SMALL_STRAIGHT("Small Straight", "연속된 4개 숫자 - 30점"),
    LARGE_STRAIGHT("Large Straight", "연속된 5개 숫자 - 40점"),
    YACHT("Yacht", "같은 숫자 5개 - 50점"),
    CHANCE("Chance", "모든 주사위 합");
    
    companion object {
        /**
         * 상위 섹션 카테고리들을 반환합니다.
         * 
         * @return 상위 섹션 카테고리 리스트
         */
        fun upperSectionCategories(): List<Category> {
            return values().filter { it.isUpperSection }
        }
        
        /**
         * 하위 섹션 카테고리들을 반환합니다.
         * 
         * @return 하위 섹션 카테고리 리스트
         */
        fun lowerSectionCategories(): List<Category> {
            return values().filter { !it.isUpperSection }
        }
        
        /**
         * 상위 섹션 보너스를 받기 위한 최소 점수
         */
        const val UPPER_SECTION_BONUS_THRESHOLD = 63
        
        /**
         * 상위 섹션 보너스 점수
         */
        const val UPPER_SECTION_BONUS_SCORE = 35
    }
    
    /**
     * 카테고리에 해당하는 숫자 값을 반환합니다. (상위 섹션만)
     * 
     * @return 카테고리 숫자 값 (1-6), 하위 섹션인 경우 null
     */
    fun getNumber(): Int? {
        return when (this) {
            ONES -> 1
            TWOS -> 2
            THREES -> 3
            FOURS -> 4
            FIVES -> 5
            SIXES -> 6
            else -> null
        }
    }
}
