package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 점수 계산을 담당하는 도메인 서비스
 */
object ScoreCalculator {
    
    /**
     * 주어진 주사위 집합과 카테고리에 대한 점수를 계산합니다.
     * 
     * @param diceSet 점수를 계산할 주사위 집합
     * @param category 점수 카테고리
     * @return 계산된 점수
     */
    fun calculateScore(diceSet: DiceSet, category: Category): Int {
        return when (category) {
            Category.ONES -> calculateUpperSection(diceSet, 1)
            Category.TWOS -> calculateUpperSection(diceSet, 2)
            Category.THREES -> calculateUpperSection(diceSet, 3)
            Category.FOURS -> calculateUpperSection(diceSet, 4)
            Category.FIVES -> calculateUpperSection(diceSet, 5)
            Category.SIXES -> calculateUpperSection(diceSet, 6)
            Category.THREE_OF_A_KIND -> calculateThreeOfAKind(diceSet)
            Category.FOUR_OF_A_KIND -> calculateFourOfAKind(diceSet)
            Category.FULL_HOUSE -> calculateFullHouse(diceSet)
            Category.SMALL_STRAIGHT -> calculateSmallStraight(diceSet)
            Category.LARGE_STRAIGHT -> calculateLargeStraight(diceSet)
            Category.YACHT -> calculateYacht(diceSet)
            Category.CHANCE -> calculateChance(diceSet)
        }
    }
    
    /**
     * 상위 섹션 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @param number 찾을 숫자 (1-6)
     * @return 해당 숫자의 개수 × 숫자 값
     */
    private fun calculateUpperSection(diceSet: DiceSet, number: Int): Int {
        return diceSet.countOf(number) * number
    }
    
    /**
     * Three of a Kind 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 같은 숫자 3개 이상이면 모든 주사위 합, 아니면 0
     */
    private fun calculateThreeOfAKind(diceSet: DiceSet): Int {
        val counts = diceSet.valueCounts()
        return if (counts.values.any { it >= 3 }) {
            diceSet.sum()
        } else {
            0
        }
    }
    
    /**
     * Four of a Kind 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 같은 숫자 4개 이상이면 모든 주사위 합, 아니면 0
     */
    private fun calculateFourOfAKind(diceSet: DiceSet): Int {
        val counts = diceSet.valueCounts()
        return if (counts.values.any { it >= 4 }) {
            diceSet.sum()
        } else {
            0
        }
    }
    
    /**
     * Full House 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 3개 + 2개 조합이면 25점, 아니면 0
     */
    private fun calculateFullHouse(diceSet: DiceSet): Int {
        val counts = diceSet.valueCounts().values.sorted()
        return if (counts == listOf(2, 3)) {
            25
        } else {
            0
        }
    }
    
    /**
     * Small Straight 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 연속된 4개 숫자가 있으면 30점, 아니면 0
     */
    private fun calculateSmallStraight(diceSet: DiceSet): Int {
        val uniqueValues = diceSet.values().toSet().sorted()
        
        // 가능한 Small Straight 패턴들
        val smallStraightPatterns = listOf(
            listOf(1, 2, 3, 4),
            listOf(2, 3, 4, 5),
            listOf(3, 4, 5, 6)
        )
        
        return if (smallStraightPatterns.any { pattern ->
            pattern.all { it in uniqueValues }
        }) {
            30
        } else {
            0
        }
    }
    
    /**
     * Large Straight 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 연속된 5개 숫자면 40점, 아니면 0
     */
    private fun calculateLargeStraight(diceSet: DiceSet): Int {
        val sortedValues = diceSet.sortedValues()
        
        // 가능한 Large Straight 패턴들
        val largeStraightPatterns = listOf(
            listOf(1, 2, 3, 4, 5),
            listOf(2, 3, 4, 5, 6)
        )
        
        return if (largeStraightPatterns.any { it == sortedValues }) {
            40
        } else {
            0
        }
    }
    
    /**
     * Yacht 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 같은 숫자 5개면 50점, 아니면 0
     */
    private fun calculateYacht(diceSet: DiceSet): Int {
        val counts = diceSet.valueCounts()
        return if (counts.values.any { it == 5 }) {
            50
        } else {
            0
        }
    }
    
    /**
     * Chance 점수를 계산합니다.
     * 
     * @param diceSet 주사위 집합
     * @return 모든 주사위의 합
     */
    private fun calculateChance(diceSet: DiceSet): Int {
        return diceSet.sum()
    }
    
    /**
     * 상위 섹션 보너스 점수를 계산합니다.
     * 
     * @param upperSectionTotal 상위 섹션 총점
     * @return 63점 이상이면 35점, 아니면 0점
     */
    fun calculateUpperSectionBonus(upperSectionTotal: Int): Int {
        return if (upperSectionTotal >= Category.UPPER_SECTION_BONUS_THRESHOLD) {
            Category.UPPER_SECTION_BONUS_SCORE
        } else {
            0
        }
    }
}
