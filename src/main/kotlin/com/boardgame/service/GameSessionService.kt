package com.boardgame.service

import com.boardgame.domain.GameSession
import com.boardgame.domain.GameStatus
import com.boardgame.dto.request.GameSessionRequest
import com.boardgame.exception.BoardGameNotFoundException
import com.boardgame.exception.GameSessionNotFoundException
import com.boardgame.exception.InvalidGameSessionStateException
import com.boardgame.exception.PlayerNotFoundException
import com.boardgame.repository.BoardGameRepository
import com.boardgame.repository.GameSessionRepository
import com.boardgame.repository.PlayerRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GameSessionService(
    private val gameSessionRepository: GameSessionRepository,
    private val boardGameRepository: BoardGameRepository,
    private val playerRepository: PlayerRepository
) {
    
    fun getAllGameSessions(): List<GameSession> {
        return gameSessionRepository.findAll()
    }
    
    fun getGameSessionById(id: Long): GameSession {
        return gameSessionRepository.findById(id)
            ?: throw GameSessionNotFoundException("ID $id 에 해당하는 게임 세션을 찾을 수 없습니다")
    }
    
    fun createGameSession(request: GameSessionRequest): GameSession {
        // 보드게임 존재 여부 확인
        if (!boardGameRepository.existsById(request.boardGameId)) {
            throw BoardGameNotFoundException("ID ${request.boardGameId} 에 해당하는 보드게임을 찾을 수 없습니다")
        }
        
        // 모든 플레이어 존재 여부 확인
        request.playerIds.forEach { playerId ->
            if (!playerRepository.existsById(playerId)) {
                throw PlayerNotFoundException("ID $playerId 에 해당하는 플레이어를 찾을 수 없습니다")
            }
        }
        
        val gameSession = GameSession(
            id = 0L, // Repository에서 자동 생성
            boardGameId = request.boardGameId,
            playerIds = request.playerIds,
            startTime = LocalDateTime.now(),
            endTime = null,
            winnerId = null,
            status = GameStatus.WAITING,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return gameSessionRepository.save(gameSession)
    }
    
    fun startGameSession(id: Long): GameSession {
        val gameSession = getGameSessionById(id)
        
        if (gameSession.status != GameStatus.WAITING) {
            throw InvalidGameSessionStateException("게임 세션을 시작할 수 없습니다. 현재 상태: ${gameSession.status}")
        }
        
        val updatedGameSession = gameSession.copy(
            status = GameStatus.PLAYING,
            startTime = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return gameSessionRepository.update(id, updatedGameSession)
            ?: throw GameSessionNotFoundException("ID $id 에 해당하는 게임 세션을 찾을 수 없습니다")
    }
    
    fun finishGameSession(id: Long, winnerId: Long?): GameSession {
        val gameSession = getGameSessionById(id)
        
        if (gameSession.status != GameStatus.PLAYING) {
            throw InvalidGameSessionStateException("게임 세션을 종료할 수 없습니다. 현재 상태: ${gameSession.status}")
        }
        
        // 승자가 지정된 경우, 해당 플레이어가 게임 참가자인지 확인
        winnerId?.let { winner ->
            if (winner !in gameSession.playerIds) {
                throw InvalidGameSessionStateException("승자는 게임 참가자 중 한 명이어야 합니다")
            }
        }
        
        val updatedGameSession = gameSession.copy(
            status = GameStatus.FINISHED,
            endTime = LocalDateTime.now(),
            winnerId = winnerId,
            updatedAt = LocalDateTime.now()
        )
        
        return gameSessionRepository.update(id, updatedGameSession)
            ?: throw GameSessionNotFoundException("ID $id 에 해당하는 게임 세션을 찾을 수 없습니다")
    }
    
    fun deleteGameSession(id: Long) {
        if (!gameSessionRepository.existsById(id)) {
            throw GameSessionNotFoundException("ID $id 에 해당하는 게임 세션을 찾을 수 없습니다")
        }
        gameSessionRepository.deleteById(id)
    }
    
    fun getGameSessionsByBoardGame(boardGameId: Long): List<GameSession> {
        return gameSessionRepository.findByBoardGameId(boardGameId)
    }
    
    fun getGameSessionsByPlayer(playerId: Long): List<GameSession> {
        return gameSessionRepository.findByPlayerId(playerId)
    }
}
