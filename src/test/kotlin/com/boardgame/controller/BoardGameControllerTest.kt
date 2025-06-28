package com.boardgame.controller

import com.boardgame.domain.BoardGame
import com.boardgame.dto.request.BoardGameRequest
import com.boardgame.service.BoardGameService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(BoardGameController::class)
class BoardGameControllerTest {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockBean
    private lateinit var boardGameService: BoardGameService
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    @Test
    fun `보드게임 목록 조회 API 테스트`() {
        // Given
        val boardGames = listOf(
            BoardGame(1L, "카탄", "전략 게임", 3, 4, 90, 3, "전략", "코스모스", 1995, 4.5),
            BoardGame(2L, "스플렌더", "보석 게임", 2, 4, 30, 2, "엔진빌딩", "스페이스 카우보이", 2014, 4.2)
        )
        whenever(boardGameService.getAllBoardGames()).thenReturn(boardGames)
        
        // When & Then
        mockMvc.perform(get("/api/boardgames"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("카탄"))
            .andExpect(jsonPath("$[1].name").value("스플렌더"))
    }
    
    @Test
    fun `보드게임 상세 조회 API 테스트`() {
        // Given
        val boardGame = BoardGame(1L, "카탄", "전략 게임", 3, 4, 90, 3, "전략", "코스모스", 1995, 4.5)
        whenever(boardGameService.getBoardGameById(1L)).thenReturn(boardGame)
        
        // When & Then
        mockMvc.perform(get("/api/boardgames/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("카탄"))
            .andExpect(jsonPath("$.category").value("전략"))
    }
    
    @Test
    fun `보드게임 생성 API 테스트`() {
        // Given
        val request = BoardGameRequest(
            name = "새 게임",
            description = "새로운 보드게임",
            minPlayers = 2,
            maxPlayers = 4,
            playTime = 60,
            difficulty = 3,
            category = "전략",
            publisher = "테스트 출판사",
            releaseYear = 2023,
            rating = 4.0
        )
        val createdBoardGame = BoardGame(
            1L, request.name, request.description, request.minPlayers, request.maxPlayers,
            request.playTime, request.difficulty, request.category, request.publisher,
            request.releaseYear, request.rating
        )
        whenever(boardGameService.createBoardGame(any())).thenReturn(createdBoardGame)
        
        // When & Then
        mockMvc.perform(
            post("/api/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("새 게임"))
    }
    
    @Test
    fun `보드게임 수정 API 테스트`() {
        // Given
        val request = BoardGameRequest(
            name = "수정된 게임",
            description = "수정된 설명",
            minPlayers = 3,
            maxPlayers = 5,
            playTime = 90,
            difficulty = 4,
            category = "전략",
            publisher = "수정된 출판사",
            releaseYear = 2023,
            rating = 4.5
        )
        val updatedBoardGame = BoardGame(
            1L, request.name, request.description, request.minPlayers, request.maxPlayers,
            request.playTime, request.difficulty, request.category, request.publisher,
            request.releaseYear, request.rating
        )
        whenever(boardGameService.updateBoardGame(eq(1L), any())).thenReturn(updatedBoardGame)
        
        // When & Then
        mockMvc.perform(
            put("/api/boardgames/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("수정된 게임"))
            .andExpect(jsonPath("$.rating").value(4.5))
    }
    
    @Test
    fun `보드게임 삭제 API 테스트`() {
        // Given
        doNothing().whenever(boardGameService).deleteBoardGame(1L)
        
        // When & Then
        mockMvc.perform(delete("/api/boardgames/1"))
            .andExpect(status().isNoContent)
        
        verify(boardGameService).deleteBoardGame(1L)
    }
    
    @Test
    fun `잘못된 요청 데이터로 보드게임 생성시 400 에러`() {
        // Given
        val invalidRequest = mapOf(
            "name" to "", // 빈 문자열
            "minPlayers" to 0, // 최소값 위반
            "difficulty" to 10 // 최대값 위반
        )
        
        // When & Then
        mockMvc.perform(
            post("/api/boardgames")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)
    }
}
