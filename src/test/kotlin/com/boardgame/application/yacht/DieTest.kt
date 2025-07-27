package com.boardgame.application.yacht

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DieTest : DescribeSpec({
    
    describe("Die 생성") {
        context("유효한 값으로 생성할 때") {
            it("1부터 6까지의 값으로 생성 가능하다") {
                (1..6).forEach { value ->
                    val die = Die(value)
                    die.value shouldBe value
                }
            }
        }
        
        context("유효하지 않은 값으로 생성할 때") {
            it("0 이하의 값으로 생성하면 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    Die(0)
                }
                shouldThrow<IllegalArgumentException> {
                    Die(-1)
                }
            }
            
            it("7 이상의 값으로 생성하면 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    Die(7)
                }
                shouldThrow<IllegalArgumentException> {
                    Die(10)
                }
            }
        }
    }
    
    describe("Die.roll()") {
        it("1부터 6까지의 랜덤한 값을 생성한다") {
            repeat(100) {
                val die = Die.roll()
                (die.value >= 1 && die.value <= 6) shouldBe true
            }
        }
    }
    
    describe("Die.of()") {
        it("지정된 값으로 Die를 생성한다") {
            val die = Die.of(3)
            die.value shouldBe 3
        }
    }
    
    describe("isValue()") {
        it("주사위 값이 지정된 값과 같은지 확인한다") {
            val die = Die(4)
            die.isValue(4) shouldBe true
            die.isValue(3) shouldBe false
        }
    }
    
    describe("reroll()") {
        it("새로운 랜덤 값을 가진 Die를 반환한다") {
            val originalDie = Die(1)
            val rerolledDie = originalDie.reroll()
            
            (rerolledDie.value >= 1 && rerolledDie.value <= 6) shouldBe true
            // 원본은 변경되지 않음
            originalDie.value shouldBe 1
        }
    }
})
