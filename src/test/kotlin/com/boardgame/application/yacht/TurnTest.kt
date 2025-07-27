package com.boardgame.application.yacht

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class TurnTest : DescribeSpec({
    
    describe("Turn 생성") {
        context("유효한 상태로 생성할 때") {
            it("초기 상태로 생성 가능하다") {
                val turn = Turn()
                turn.rollCount shouldBe 0
                turn.currentDice shouldBe null
            }
            
            it("주사위를 굴린 상태로 생성 가능하다") {
                val diceSet = DiceSet.of(1, 2, 3, 4, 5)
                val turn = Turn(1, diceSet)
                turn.rollCount shouldBe 1
                turn.currentDice shouldBe diceSet
            }
        }
        
        context("유효하지 않은 상태로 생성할 때") {
            it("굴린 횟수가 범위를 벗어나면 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    Turn(-1)
                }
                shouldThrow<IllegalArgumentException> {
                    Turn(4)
                }
            }
            
            it("굴린 횟수가 0보다 크면서 주사위가 없으면 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    Turn(1, null)
                }
            }
        }
    }
    
    describe("Turn.start()") {
        it("새로운 턴을 시작한다") {
            val turn = Turn.start()
            turn.rollCount shouldBe 0
            turn.currentDice shouldBe null
        }
    }
    
    describe("canRoll()") {
        it("3번 미만 굴렸으면 굴릴 수 있다") {
            val turn1 = Turn(0)
            val turn2 = Turn(2, DiceSet.of(1, 2, 3, 4, 5))
            
            turn1.canRoll() shouldBe true
            turn2.canRoll() shouldBe true
        }
        
        it("3번 굴렸으면 굴릴 수 없다") {
            val turn = Turn(3, DiceSet.of(1, 2, 3, 4, 5))
            turn.canRoll() shouldBe false
        }
    }
    
    describe("rollAll()") {
        it("모든 주사위를 굴린다") {
            val turn = Turn.start()
            val rolledTurn = turn.rollAll()
            
            rolledTurn.rollCount shouldBe 1
            rolledTurn.currentDice shouldNotBe null
            rolledTurn.currentDice!!.dice.size shouldBe 5
        }
        
        it("더 이상 굴릴 수 없으면 예외가 발생한다") {
            val turn = Turn(3, DiceSet.of(1, 2, 3, 4, 5))
            
            shouldThrow<IllegalArgumentException> {
                turn.rollAll()
            }
        }
    }
    
    describe("reroll()") {
        it("지정된 인덱스의 주사위만 다시 굴린다") {
            val originalDice = DiceSet.of(1, 2, 3, 4, 5)
            val turn = Turn(1, originalDice)
            
            val rerolledTurn = turn.reroll(setOf(0, 2))
            
            rerolledTurn.rollCount shouldBe 2
            rerolledTurn.currentDice shouldNotBe null
            // 인덱스 1, 3, 4는 변경되지 않음
            rerolledTurn.currentDice!!.dice[1].value shouldBe 2
            rerolledTurn.currentDice!!.dice[3].value shouldBe 4
            rerolledTurn.currentDice!!.dice[4].value shouldBe 5
        }
        
        it("주사위가 없으면 예외가 발생한다") {
            val turn = Turn.start()
            
            shouldThrow<IllegalArgumentException> {
                turn.reroll(setOf(0))
            }
        }
        
        it("더 이상 굴릴 수 없으면 예외가 발생한다") {
            val turn = Turn(3, DiceSet.of(1, 2, 3, 4, 5))
            
            shouldThrow<IllegalArgumentException> {
                turn.reroll(setOf(0))
            }
        }
    }
    
    describe("isComplete()") {
        it("3번 굴렸으면 완료") {
            val turn = Turn(3, DiceSet.of(1, 2, 3, 4, 5))
            turn.isComplete() shouldBe true
        }
        
        it("주사위가 있으면 완료") {
            val turn = Turn(1, DiceSet.of(1, 2, 3, 4, 5))
            turn.isComplete() shouldBe true
        }
        
        it("초기 상태면 미완료") {
            val turn = Turn.start()
            turn.isComplete() shouldBe false
        }
    }
    
    describe("canRecordScore()") {
        it("주사위를 최소 1번 굴렸으면 점수 기록 가능") {
            val turn = Turn(1, DiceSet.of(1, 2, 3, 4, 5))
            turn.canRecordScore() shouldBe true
        }
        
        it("주사위를 굴리지 않았으면 점수 기록 불가") {
            val turn = Turn.start()
            turn.canRecordScore() shouldBe false
        }
    }
    
    describe("calculateScore()") {
        it("현재 주사위로 점수를 계산한다") {
            val diceSet = DiceSet.of(1, 1, 1, 2, 3)
            val turn = Turn(1, diceSet)
            
            val score = turn.calculateScore(Category.ONES)
            score.points shouldBe 3
        }
        
        it("주사위가 없으면 예외가 발생한다") {
            val turn = Turn.start()
            
            shouldThrow<IllegalArgumentException> {
                turn.calculateScore(Category.ONES)
            }
        }
    }
})
