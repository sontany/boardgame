package com.boardgame.service

import com.boardgame.domain.BoardGame
import com.boardgame.dto.request.BoardGameRequest
import com.boardgame.exception.BoardGameNotFoundException
import com.boardgame.repository.BoardGameRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDateTime

class BoardGameServiceTest {
    
    private lateinit var boardGameRepository: BoardGameRepository
    private lateinit var boardGameService: BoardGameService
    
    @BeforeEach
    fun setUp() {
        boardGameRepository = mock()
        boardGameService = BoardGameService(boardGameRepository)
    }
    
    @Test
    fun `모든 보드게임 조회 성공`() {
        // Given
        val boardGames = listOf(
            BoardGame(1L, "카탄", "전략 게임", 3, 4, 90, 3, "전략", "코스모스", 1995, 4.5),
            BoardGame(2L, "스플렌더", "보석 게임", 2, 4, 30, 2, "엔진빌딩", "스페이스 카우보이", 2014, 4.2)
        )
        whenever(boardGameRepository.findAll()).thenReturn(boardGames)
        
        // When
        val result = boardGameService.getAllBoardGames()
        
        // Then
        assertEquals(2, result.size)
        assertEquals("카탄", result[0].name)
        assertEquals("스플렌더", result[1].name)
    }
    
    @Test
    fun `ID로 보드게임 조회 성공`() {
        // Given
        val boardGame = BoardGame(1L, "카탄", "전략 게임", 3, 4, 90, 3, "전략", "코스모스", 1995, 4.5)
        whenever(boardGameRepository.findById(1L)).thenReturn(boardGame)
        
        // When
        val result = boardGameService.getBoardGameById(1L)
        
        // Then
        assertEquals("카탄", result.name)
        assertEquals(1L, result.id)
    }
    
    @Test
    fun `존재하지 않는 ID로 보드게임 조회시 예외 발생`() {
        // Given
        whenever(boardGameRepository.findById(999L)).thenReturn(null)
        
        // When & Then
        assertThrows<BoardGameNotFoundException> {
            boardGameService.getBoardGameById(999L)
        }
    }
    
    @Test
    fun `보드게임 생성 성공`() {
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
        val savedBoardGame = BoardGame(
            1L, request.name, request.description, request.minPlayers, request.maxPlayers,
            request.playTime, request.difficulty, request.category, request.publisher,
            request.releaseYear, request.rating
        )
        whenever(boardGameRepository.save(any())).thenReturn(savedBoardGame)
        
        // When
        val result = boardGameService.createBoardGame(request)
        
        // Then
        assertEquals("새 게임", result.name)
        assertEquals(1L, result.id)
        verify(boardGameRepository).save(any())
    }
    
    @Test
    fun `보드게임 수정 성공`() {
        // Given
        val existingBoardGame = BoardGame(1L, "기존 게임", "기존 설명", 2, 4, 60, 2, "전략", "기존 출판사", 2020, 3.5)
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
            request.releaseYear, request.rating, existingBoardGame.createdAt, LocalDateTime.now()
        )
        
        whenever(boardGameRepository.findById(1L)).thenReturn(existingBoardGame)
        whenever(boardGameRepository.update(eq(1L), any())).thenReturn(updatedBoardGame)
        
        // When
        val result = boardGameService.updateBoardGame(1L, request)
        
        // Then
        assertEquals("수정된 게임", result.name)
        assertEquals(4.5, result.rating)
        verify(boardGameRepository).update(eq(1L), any())
    }
    
    @Test
    fun `보드게임 삭제 성공`() {
        // Given
        whenever(boardGameRepository.existsById(1L)).thenReturn(true)
        whenever(boardGameRepository.deleteById(1L)).thenReturn(true)
        
        // When
        boardGameService.deleteBoardGame(1L)
        
        // Then
        verify(boardGameRepository).deleteById(1L)
    }
    
    @Test
    fun `존재하지 않는 보드게임 삭제시 예외 발생`() {
        // Given
        whenever(boardGameRepository.existsById(999L)).thenReturn(false)
        
        // When & Then
        assertThrows<BoardGameNotFoundException> {
            boardGameService.deleteBoardGame(999L)
        }
    }
}
