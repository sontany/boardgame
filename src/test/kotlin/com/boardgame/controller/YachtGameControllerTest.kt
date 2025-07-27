package com.boardgame.controller

import com.boardgame.application.yacht.Category
import com.boardgame.controller.dto.CreateGameRequest
import com.boardgame.controller.dto.RecordScoreRequest
import com.boardgame.controller.dto.RollDiceRequest
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureWebMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class YachtGameControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : DescribeSpec({
    
    describe("POST /api/yacht/games") {
        it("새 게임을 생성한다") {
            val request = CreateGameRequest("테스트플레이어")
            val requestJson = objectMapper.writeValueAsString(request)
            
            val result = mockMvc.perform(
                post("/api/yacht/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.playerName").value("테스트플레이어"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.currentTurn").value(1))
                .andReturn()
            
            val responseJson = result.response.contentAsString
            val gameId = objectMapper.readTree(responseJson).get("gameId").asLong()
            gameId shouldNotBe 0
        }
    }
    
    describe("GET /api/yacht/games/{gameId}") {
        it("게임 상태를 조회한다") {
            // 게임 생성
            val createRequest = CreateGameRequest("테스트플레이어")
            val createRequestJson = objectMapper.writeValueAsString(createRequest)
            
            val createResult = mockMvc.perform(
                post("/api/yacht/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createRequestJson)
            ).andReturn()
            
            val gameId = objectMapper.readTree(createResult.response.contentAsString)
                .get("gameId").asLong()
            
            // 게임 조회
            mockMvc.perform(get("/api/yacht/games/$gameId"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.gameId").value(gameId))
                .andExpect(jsonPath("$.playerName").value("테스트플레이어"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
        }
    }
    
    describe("POST /api/yacht/games/{gameId}/roll") {
        it("주사위를 굴린다") {
            // 게임 생성
            val createRequest = CreateGameRequest("테스트플레이어")
            val createRequestJson = objectMapper.writeValueAsString(createRequest)
            
            val createResult = mockMvc.perform(
                post("/api/yacht/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createRequestJson)
            ).andReturn()
            
            val gameId = objectMapper.readTree(createResult.response.contentAsString)
                .get("gameId").asLong()
            
            // 주사위 굴리기
            val rollRequest = RollDiceRequest()
            val rollRequestJson = objectMapper.writeValueAsString(rollRequest)
            
            mockMvc.perform(
                post("/api/yacht/games/$gameId/roll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rollRequestJson)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.diceValues").isArray)
                .andExpect(jsonPath("$.diceValues.length()").value(5))
                .andExpect(jsonPath("$.rollCount").value(1))
                .andExpect(jsonPath("$.canRollMore").value(true))
                .andExpect(jsonPath("$.canRecordScore").value(true))
        }
    }
    
    describe("GET /api/yacht/games/{gameId}/scorecard") {
        it("점수판을 조회한다") {
            // 게임 생성
            val createRequest = CreateGameRequest("테스트플레이어")
            val createRequestJson = objectMapper.writeValueAsString(createRequest)
            
            val createResult = mockMvc.perform(
                post("/api/yacht/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createRequestJson)
            ).andReturn()
            
            val gameId = objectMapper.readTree(createResult.response.contentAsString)
                .get("gameId").asLong()
            
            // 점수판 조회
            mockMvc.perform(get("/api/yacht/games/$gameId/scorecard"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.scores").isMap)
                .andExpect(jsonPath("$.upperSectionTotal").value(0))
                .andExpect(jsonPath("$.upperSectionBonus").value(0))
                .andExpect(jsonPath("$.lowerSectionTotal").value(0))
                .andExpect(jsonPath("$.totalScore").value(0))
                .andExpect(jsonPath("$.isComplete").value(false))
                .andExpect(jsonPath("$.availableCategories").isArray)
                .andExpect(jsonPath("$.availableCategories.length()").value(13))
        }
    }
    
    describe("전체 게임 플로우") {
        it("게임 생성 -> 주사위 굴리기 -> 점수 기록 순서로 진행된다") {
            // 1. 게임 생성
            val createRequest = CreateGameRequest("테스트플레이어")
            val createRequestJson = objectMapper.writeValueAsString(createRequest)
            
            val createResult = mockMvc.perform(
                post("/api/yacht/games")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createRequestJson)
            ).andReturn()
            
            val gameId = objectMapper.readTree(createResult.response.contentAsString)
                .get("gameId").asLong()
            
            // 2. 주사위 굴리기
            val rollRequest = RollDiceRequest()
            val rollRequestJson = objectMapper.writeValueAsString(rollRequest)
            
            mockMvc.perform(
                post("/api/yacht/games/$gameId/roll")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(rollRequestJson)
            ).andExpect(status().isOk)
            
            // 3. 점수 기록
            val scoreRequest = RecordScoreRequest(Category.CHANCE)
            val scoreRequestJson = objectMapper.writeValueAsString(scoreRequest)
            
            mockMvc.perform(
                post("/api/yacht/games/$gameId/score")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(scoreRequestJson)
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.scores.CHANCE").exists())
                .andExpect(jsonPath("$.availableCategories.length()").value(12))
        }
    }
})
