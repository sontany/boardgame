package com.boardgame.repository

import com.boardgame.domain.Player
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class PlayerRepository {
    
    private val players = ConcurrentHashMap<Long, Player>()
    private val idGenerator = AtomicLong(1)
    
    init {
        // 초기 샘플 데이터
        val samplePlayers = listOf(
            Player(1L, "게임마스터", "gamemaster@example.com", 10, 150, "전략"),
            Player(2L, "보드게임러버", "boardgamelover@example.com", 7, 89, "가족게임"),
            Player(3L, "전략왕", "strategy@example.com", 8, 120, "전략"),
            Player(4L, "캐주얼플레이어", "casual@example.com", 3, 25, "파티게임"),
            Player(5L, "프로게이머", "progamer@example.com", 15, 300, "엔진빌딩")
        )
        
        samplePlayers.forEach { player ->
            players[player.id] = player
            idGenerator.set(maxOf(idGenerator.get(), player.id + 1))
        }
    }
    
    fun findAll(): List<Player> {
        return players.values.toList()
    }
    
    fun findById(id: Long): Player? {
        return players[id]
    }
    
    fun findByEmail(email: String): Player? {
        return players.values.find { it.email == email }
    }
    
    fun save(player: Player): Player {
        val id = if (player.id == 0L) idGenerator.getAndIncrement() else player.id
        val savedPlayer = player.copy(
            id = id,
            updatedAt = LocalDateTime.now()
        )
        players[id] = savedPlayer
        return savedPlayer
    }
    
    fun update(id: Long, player: Player): Player? {
        return if (players.containsKey(id)) {
            val updatedPlayer = player.copy(
                id = id,
                updatedAt = LocalDateTime.now()
            )
            players[id] = updatedPlayer
            updatedPlayer
        } else {
            null
        }
    }
    
    fun deleteById(id: Long): Boolean {
        return players.remove(id) != null
    }
    
    fun existsById(id: Long): Boolean {
        return players.containsKey(id)
    }
    
    fun existsByEmail(email: String): Boolean {
        return players.values.any { it.email == email }
    }
}
