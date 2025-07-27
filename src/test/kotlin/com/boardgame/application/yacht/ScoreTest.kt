package com.boardgame.application.yacht

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ScoreTest : DescribeSpec({
    
    describe("Score 생성") {
        context("유효한 점수로 생성할 때") {
            it("0 이상의 점수로 생성 가능하다") {
                val score = Score(Category.ONES, 5)
                score.category shouldBe Category.ONES
                score.points shouldBe 5
                score.recordedAt shouldNotBe null
            }
        }
        
        context("유효하지 않은 점수로 생성할 때") {
            it("음수 점수로 생성하면 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    Score(Category.ONES, -1)
                }
            }
        }
    }
    
    describe("Score.calculate()") {
        it("주사위 집합과 카테고리로 점수를 계산한다") {
            val diceSet = DiceSet.of(1, 1, 1, 2, 3)
            val score = Score.calculate(diceSet, Category.ONES)
            
            score.category shouldBe Category.ONES
            score.points shouldBe 3 // 1이 3개
        }
    }
    
    describe("Score.zero()") {
        it("0점 점수를 생성한다") {
            val score = Score.zero(Category.YACHT)
            
            score.category shouldBe Category.YACHT
            score.points shouldBe 0
        }
    }
    
    describe("isZero()") {
        it("0점인지 확인한다") {
            val zeroScore = Score(Category.ONES, 0)
            val nonZeroScore = Score(Category.ONES, 5)
            
            zeroScore.isZero() shouldBe true
            nonZeroScore.isZero() shouldBe false
        }
    }
    
    describe("isUpperSection()") {
        it("상위 섹션 점수인지 확인한다") {
            val upperScore = Score(Category.ONES, 3)
            val lowerScore = Score(Category.YACHT, 50)
            
            upperScore.isUpperSection() shouldBe true
            lowerScore.isUpperSection() shouldBe false
        }
    }
})
