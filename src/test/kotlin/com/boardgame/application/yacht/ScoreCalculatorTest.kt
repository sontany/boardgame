package com.boardgame.application.yacht

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ScoreCalculatorTest : DescribeSpec({
    
    describe("상위 섹션 점수 계산") {
        it("ONES 카테고리 점수를 계산한다") {
            val diceSet = DiceSet.of(1, 1, 2, 3, 1) // 1이 3개
            ScoreCalculator.calculateScore(diceSet, Category.ONES) shouldBe 3
        }
        
        it("TWOS 카테고리 점수를 계산한다") {
            val diceSet = DiceSet.of(2, 2, 2, 3, 4) // 2가 3개
            ScoreCalculator.calculateScore(diceSet, Category.TWOS) shouldBe 6
        }
        
        it("SIXES 카테고리 점수를 계산한다") {
            val diceSet = DiceSet.of(6, 6, 1, 2, 3) // 6이 2개
            ScoreCalculator.calculateScore(diceSet, Category.SIXES) shouldBe 12
        }
    }
    
    describe("Three of a Kind 점수 계산") {
        it("같은 숫자 3개 이상이면 모든 주사위 합을 반환한다") {
            val diceSet = DiceSet.of(3, 3, 3, 1, 2) // 3이 3개
            ScoreCalculator.calculateScore(diceSet, Category.THREE_OF_A_KIND) shouldBe 12
        }
        
        it("같은 숫자 3개 미만이면 0을 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5) // 모두 다름
            ScoreCalculator.calculateScore(diceSet, Category.THREE_OF_A_KIND) shouldBe 0
        }
    }
    
    describe("Four of a Kind 점수 계산") {
        it("같은 숫자 4개 이상이면 모든 주사위 합을 반환한다") {
            val diceSet = DiceSet.of(4, 4, 4, 4, 2) // 4가 4개
            ScoreCalculator.calculateScore(diceSet, Category.FOUR_OF_A_KIND) shouldBe 18
        }
        
        it("같은 숫자 4개 미만이면 0을 반환한다") {
            val diceSet = DiceSet.of(3, 3, 3, 1, 2) // 3이 3개만
            ScoreCalculator.calculateScore(diceSet, Category.FOUR_OF_A_KIND) shouldBe 0
        }
    }
    
    describe("Full House 점수 계산") {
        it("3개 + 2개 조합이면 25점을 반환한다") {
            val diceSet = DiceSet.of(3, 3, 3, 2, 2) // 3이 3개, 2가 2개
            ScoreCalculator.calculateScore(diceSet, Category.FULL_HOUSE) shouldBe 25
        }
        
        it("Full House가 아니면 0을 반환한다") {
            val diceSet = DiceSet.of(3, 3, 3, 3, 2) // 3이 4개, 2가 1개
            ScoreCalculator.calculateScore(diceSet, Category.FULL_HOUSE) shouldBe 0
        }
    }
    
    describe("Small Straight 점수 계산") {
        it("1,2,3,4 연속이면 30점을 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 6)
            ScoreCalculator.calculateScore(diceSet, Category.SMALL_STRAIGHT) shouldBe 30
        }
        
        it("2,3,4,5 연속이면 30점을 반환한다") {
            val diceSet = DiceSet.of(2, 3, 4, 5, 1)
            ScoreCalculator.calculateScore(diceSet, Category.SMALL_STRAIGHT) shouldBe 30
        }
        
        it("3,4,5,6 연속이면 30점을 반환한다") {
            val diceSet = DiceSet.of(3, 4, 5, 6, 1)
            ScoreCalculator.calculateScore(diceSet, Category.SMALL_STRAIGHT) shouldBe 30
        }
        
        it("연속된 4개가 없으면 0을 반환한다") {
            val diceSet = DiceSet.of(1, 3, 5, 6, 2)
            ScoreCalculator.calculateScore(diceSet, Category.SMALL_STRAIGHT) shouldBe 0
        }
    }
    
    describe("Large Straight 점수 계산") {
        it("1,2,3,4,5 연속이면 40점을 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5)
            ScoreCalculator.calculateScore(diceSet, Category.LARGE_STRAIGHT) shouldBe 40
        }
        
        it("2,3,4,5,6 연속이면 40점을 반환한다") {
            val diceSet = DiceSet.of(2, 3, 4, 5, 6)
            ScoreCalculator.calculateScore(diceSet, Category.LARGE_STRAIGHT) shouldBe 40
        }
        
        it("연속된 5개가 아니면 0을 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 6)
            ScoreCalculator.calculateScore(diceSet, Category.LARGE_STRAIGHT) shouldBe 0
        }
    }
    
    describe("Yacht 점수 계산") {
        it("같은 숫자 5개면 50점을 반환한다") {
            val diceSet = DiceSet.of(5, 5, 5, 5, 5)
            ScoreCalculator.calculateScore(diceSet, Category.YACHT) shouldBe 50
        }
        
        it("같은 숫자 5개가 아니면 0을 반환한다") {
            val diceSet = DiceSet.of(5, 5, 5, 5, 4)
            ScoreCalculator.calculateScore(diceSet, Category.YACHT) shouldBe 0
        }
    }
    
    describe("Chance 점수 계산") {
        it("모든 주사위의 합을 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5)
            ScoreCalculator.calculateScore(diceSet, Category.CHANCE) shouldBe 15
        }
    }
    
    describe("상위 섹션 보너스 계산") {
        it("63점 이상이면 35점 보너스를 반환한다") {
            ScoreCalculator.calculateUpperSectionBonus(63) shouldBe 35
            ScoreCalculator.calculateUpperSectionBonus(70) shouldBe 35
        }
        
        it("63점 미만이면 0점을 반환한다") {
            ScoreCalculator.calculateUpperSectionBonus(62) shouldBe 0
            ScoreCalculator.calculateUpperSectionBonus(50) shouldBe 0
        }
    }
})
