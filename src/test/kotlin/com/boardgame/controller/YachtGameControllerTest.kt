package com.boardgame.controller

import com.boardgame.controller.dto.CreateGameRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class YachtGameControllerTest : DescribeSpec({
    
    describe("CreateGameRequest") {
        it("유효한 플레이어 이름으로 생성 가능하다") {
            val request = CreateGameRequest("테스트플레이어")
            request.playerName shouldBe "테스트플레이어"
        }
        
        it("빈 플레이어 이름으로 생성하면 예외가 발생한다") {
            shouldThrow<IllegalArgumentException> {
                CreateGameRequest("")
            }
            
            shouldThrow<IllegalArgumentException> {
                CreateGameRequest("   ")
            }
        }
    }
})
