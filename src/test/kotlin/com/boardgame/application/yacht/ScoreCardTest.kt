package com.boardgame.application.yacht

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class ScoreCardTest : DescribeSpec({
    
    describe("점수 기록") {
        it("새로운 점수를 기록할 수 있다") {
            val scoreCard = ScoreCard()
            val score = Score(Category.ONES, 3)
            
            scoreCard.recordScore(score)
            
            scoreCard.isRecorded(Category.ONES) shouldBe true
            scoreCard.getScore(Category.ONES) shouldBe score
        }
        
        it("이미 기록된 카테고리에 다시 기록하면 예외가 발생한다") {
            val scoreCard = ScoreCard()
            val score1 = Score(Category.ONES, 3)
            val score2 = Score(Category.ONES, 5)
            
            scoreCard.recordScore(score1)
            
            shouldThrow<IllegalArgumentException> {
                scoreCard.recordScore(score2)
            }
        }
    }
    
    describe("점수 조회") {
        it("기록되지 않은 카테고리는 null을 반환한다") {
            val scoreCard = ScoreCard()
            
            scoreCard.getScore(Category.ONES) shouldBe null
            scoreCard.isRecorded(Category.ONES) shouldBe false
        }
    }
    
    describe("상위 섹션 총점 계산") {
        it("상위 섹션 점수들의 합을 계산한다") {
            val scoreCard = ScoreCard()
            scoreCard.recordScore(Score(Category.ONES, 3))
            scoreCard.recordScore(Score(Category.TWOS, 6))
            scoreCard.recordScore(Score(Category.THREES, 9))
            
            scoreCard.getUpperSectionTotal() shouldBe 18
        }
    }
    
    describe("하위 섹션 총점 계산") {
        it("하위 섹션 점수들의 합을 계산한다") {
            val scoreCard = ScoreCard()
            scoreCard.recordScore(Score(Category.YACHT, 50))
            scoreCard.recordScore(Score(Category.CHANCE, 25))
            
            scoreCard.getLowerSectionTotal() shouldBe 75
        }
    }
    
    describe("상위 섹션 보너스 계산") {
        it("상위 섹션 총점이 63점 이상이면 35점 보너스") {
            val scoreCard = ScoreCard()
            // 각 카테고리에 최대 점수 기록 (3*6 + 6*6 + 9*6 + 12*6 + 15*6 + 18*6 = 63)
            scoreCard.recordScore(Score(Category.ONES, 3))
            scoreCard.recordScore(Score(Category.TWOS, 6))
            scoreCard.recordScore(Score(Category.THREES, 9))
            scoreCard.recordScore(Score(Category.FOURS, 12))
            scoreCard.recordScore(Score(Category.FIVES, 15))
            scoreCard.recordScore(Score(Category.SIXES, 18))
            
            scoreCard.getUpperSectionBonus() shouldBe 35
        }
        
        it("상위 섹션 총점이 63점 미만이면 보너스 없음") {
            val scoreCard = ScoreCard()
            scoreCard.recordScore(Score(Category.ONES, 1))
            
            scoreCard.getUpperSectionBonus() shouldBe 0
        }
    }
    
    describe("총점 계산") {
        it("상위 섹션 + 보너스 + 하위 섹션 총점을 계산한다") {
            val scoreCard = ScoreCard()
            scoreCard.recordScore(Score(Category.ONES, 3))
            scoreCard.recordScore(Score(Category.YACHT, 50))
            
            val expectedTotal = 3 + 0 + 50 // 상위 + 보너스 + 하위
            scoreCard.getTotalScore() shouldBe expectedTotal
        }
    }
    
    describe("게임 완료 확인") {
        it("모든 카테고리에 점수가 기록되면 완료") {
            val scoreCard = ScoreCard()
            
            // 모든 카테고리에 점수 기록
            Category.values().forEach { category ->
                scoreCard.recordScore(Score(category, 0))
            }
            
            scoreCard.isComplete() shouldBe true
        }
        
        it("일부 카테고리만 기록되면 미완료") {
            val scoreCard = ScoreCard()
            scoreCard.recordScore(Score(Category.ONES, 3))
            
            scoreCard.isComplete() shouldBe false
        }
    }
    
    describe("사용 가능한 카테고리 조회") {
        it("아직 기록되지 않은 카테고리들을 반환한다") {
            val scoreCard = ScoreCard()
            scoreCard.recordScore(Score(Category.ONES, 3))
            
            val availableCategories = scoreCard.getAvailableCategories()
            
            availableCategories shouldNotContain Category.ONES
            availableCategories shouldContain Category.TWOS
        }
    }
})
