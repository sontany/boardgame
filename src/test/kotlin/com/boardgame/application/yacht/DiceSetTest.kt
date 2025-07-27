package com.boardgame.application.yacht

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class DiceSetTest : DescribeSpec({
    
    describe("DiceSet 생성") {
        context("유효한 주사위 개수로 생성할 때") {
            it("정확히 5개의 주사위로 생성 가능하다") {
                val dice = List(5) { Die(it + 1) }
                val diceSet = DiceSet(dice)
                diceSet.dice.size shouldBe 5
            }
        }
        
        context("유효하지 않은 주사위 개수로 생성할 때") {
            it("5개가 아닌 주사위로 생성하면 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    DiceSet(List(4) { Die(1) })
                }
                shouldThrow<IllegalArgumentException> {
                    DiceSet(List(6) { Die(1) })
                }
            }
        }
    }
    
    describe("DiceSet.rollAll()") {
        it("5개의 랜덤한 주사위를 생성한다") {
            val diceSet = DiceSet.rollAll()
            diceSet.dice.size shouldBe 5
            diceSet.dice.forEach { die ->
                (die.value >= 1 && die.value <= 6) shouldBe true
            }
        }
    }
    
    describe("DiceSet.of()") {
        it("지정된 값들로 DiceSet을 생성한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5)
            diceSet.values() shouldContainExactly listOf(1, 2, 3, 4, 5)
        }
        
        it("5개가 아닌 값으로 생성하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                DiceSet.of(1, 2, 3, 4)
            }
        }
    }
    
    describe("reroll()") {
        it("지정된 인덱스의 주사위만 다시 굴린다") {
            val originalDiceSet = DiceSet.of(1, 2, 3, 4, 5)
            val rerolledDiceSet = originalDiceSet.reroll(setOf(0, 2))
            
            // 인덱스 1, 3, 4는 변경되지 않음
            rerolledDiceSet.dice[1].value shouldBe 2
            rerolledDiceSet.dice[3].value shouldBe 4
            rerolledDiceSet.dice[4].value shouldBe 5
            
            // 인덱스 0, 2는 변경될 수 있음 (랜덤이므로 범위만 확인)
            (rerolledDiceSet.dice[0].value >= 1 && rerolledDiceSet.dice[0].value <= 6) shouldBe true
            (rerolledDiceSet.dice[2].value >= 1 && rerolledDiceSet.dice[2].value <= 6) shouldBe true
        }
        
        it("유효하지 않은 인덱스로 reroll하면 예외가 발생한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5)
            shouldThrow<IllegalArgumentException> {
                diceSet.reroll(setOf(-1))
            }
            shouldThrow<IllegalArgumentException> {
                diceSet.reroll(setOf(5))
            }
        }
    }
    
    describe("values()") {
        it("주사위 값들을 리스트로 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5)
            diceSet.values() shouldContainExactly listOf(1, 2, 3, 4, 5)
        }
    }
    
    describe("countOf()") {
        it("지정된 값의 주사위 개수를 반환한다") {
            val diceSet = DiceSet.of(1, 1, 2, 2, 2)
            diceSet.countOf(1) shouldBe 2
            diceSet.countOf(2) shouldBe 3
            diceSet.countOf(3) shouldBe 0
        }
    }
    
    describe("sum()") {
        it("모든 주사위 값의 합을 반환한다") {
            val diceSet = DiceSet.of(1, 2, 3, 4, 5)
            diceSet.sum() shouldBe 15
        }
    }
    
    describe("sortedValues()") {
        it("주사위 값들을 정렬하여 반환한다") {
            val diceSet = DiceSet.of(5, 1, 3, 2, 4)
            diceSet.sortedValues() shouldContainExactly listOf(1, 2, 3, 4, 5)
        }
    }
    
    describe("valueCounts()") {
        it("각 주사위 값의 개수를 맵으로 반환한다") {
            val diceSet = DiceSet.of(1, 1, 2, 2, 2)
            val counts = diceSet.valueCounts()
            counts[1] shouldBe 2
            counts[2] shouldBe 3
        }
    }
})
