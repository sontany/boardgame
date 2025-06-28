package com.boardgame.repository

import com.boardgame.domain.GameSession
import com.boardgame.domain.GameStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class GameSessionRepository {
    
    private val gameSessions = ConcurrentHashMap<Long, GameSession>()
    private val idGenerator = AtomicLong(1)
    
    init {
        // 초기 샘플 데이터
        val sampleGameSessions = listOf(
            GameSession(
                1L, 1L, listOf(1L, 2L, 3L), 
                LocalDateTime.now().minusHours(2), 
                LocalDateTime.now().minusHours(1), 
                1L, GameStatus.FINISHED
            ),
            GameSession(
                2L, 2L, listOf(2L, 4L), 
                LocalDateTime.now().minusMinutes(30), 
                null, 
                null, GameStatus.PLAYING
            ),
            GameSession(
                3L, 3L, listOf(1L, 3L, 5L), 
                LocalDateTime.now().plusMinutes(30), 
                null, 
                null, GameStatus.WAITING
            )
        )
        
        sampleGameSessions.forEach { gameSession ->
            gameSessions[gameSession.id] = gameSession
            idGenerator.set(maxOf(idGenerator.get(), gameSession.id + 1))
        }
    }
    
    fun findAll(): List<GameSession> {
        return gameSessions.values.toList()
    }
    
    fun findById(id: Long): GameSession? {
        return gameSessions[id]
    }
    
    fun findByBoardGameId(boardGameId: Long): List<GameSession> {
        return gameSessions.values.filter { it.boardGameId == boardGameId }
    }
    
    fun findByPlayerId(playerId: Long): List<GameSession> {
        return gameSessions.values.filter { playerId in it.playerIds }
    }
    
    fun findByStatus(status: GameStatus): List<GameSession> {
        return gameSessions.values.filter { it.status == status }
    }
    
    fun save(gameSession: GameSession): GameSession {
        val id = if (gameSession.id == 0L) idGenerator.getAndIncrement() else gameSession.id
        val savedGameSession = gameSession.copy(
            id = id,
            updatedAt = LocalDateTime.now()
        )
        gameSessions[id] = savedGameSession
        return savedGameSession
    }
    
    fun update(id: Long, gameSession: GameSession): GameSession? {
        return if (gameSessions.containsKey(id)) {
            val updatedGameSession = gameSession.copy(
                id = id,
                updatedAt = LocalDateTime.now()
            )
            gameSessions[id] = updatedGameSession
            updatedGameSession
        } else {
            null
        }
    }
    
    fun deleteById(id: Long): Boolean {
        return gameSessions.remove(id) != null
    }
    
    fun existsById(id: Long): Boolean {
        return gameSessions.containsKey(id)
    }
}
