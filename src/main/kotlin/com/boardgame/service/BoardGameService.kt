package com.boardgame.service

import com.boardgame.domain.BoardGame
import com.boardgame.dto.request.BoardGameRequest
import com.boardgame.exception.BoardGameNotFoundException
import com.boardgame.repository.BoardGameRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BoardGameService(
    private val boardGameRepository: BoardGameRepository
) {
    
    fun getAllBoardGames(): List<BoardGame> {
        return boardGameRepository.findAll()
    }
    
    fun getBoardGameById(id: Long): BoardGame {
        return boardGameRepository.findById(id)
            ?: throw BoardGameNotFoundException("ID $id 에 해당하는 보드게임을 찾을 수 없습니다")
    }
    
    fun getBoardGamesByCategory(category: String): List<BoardGame> {
        return boardGameRepository.findByCategory(category)
    }
    
    fun createBoardGame(request: BoardGameRequest): BoardGame {
        val boardGame = BoardGame(
            id = 0L, // Repository에서 자동 생성
            name = request.name,
            description = request.description,
            minPlayers = request.minPlayers,
            maxPlayers = request.maxPlayers,
            playTime = request.playTime,
            difficulty = request.difficulty,
            category = request.category,
            publisher = request.publisher,
            releaseYear = request.releaseYear,
            rating = request.rating,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return boardGameRepository.save(boardGame)
    }
    
    fun updateBoardGame(id: Long, request: BoardGameRequest): BoardGame {
        val existingBoardGame = getBoardGameById(id)
        
        val updatedBoardGame = BoardGame(
            id = id,
            name = request.name,
            description = request.description,
            minPlayers = request.minPlayers,
            maxPlayers = request.maxPlayers,
            playTime = request.playTime,
            difficulty = request.difficulty,
            category = request.category,
            publisher = request.publisher,
            releaseYear = request.releaseYear,
            rating = request.rating,
            createdAt = existingBoardGame.createdAt,
            updatedAt = LocalDateTime.now()
        )
        
        return boardGameRepository.update(id, updatedBoardGame)
            ?: throw BoardGameNotFoundException("ID $id 에 해당하는 보드게임을 찾을 수 없습니다")
    }
    
    fun deleteBoardGame(id: Long) {
        if (!boardGameRepository.existsById(id)) {
            throw BoardGameNotFoundException("ID $id 에 해당하는 보드게임을 찾을 수 없습니다")
        }
        boardGameRepository.deleteById(id)
    }
}
