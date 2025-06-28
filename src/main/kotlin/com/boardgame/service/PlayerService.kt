package com.boardgame.service

import com.boardgame.domain.Player
import com.boardgame.dto.request.PlayerRequest
import com.boardgame.exception.DuplicateEmailException
import com.boardgame.exception.PlayerNotFoundException
import com.boardgame.repository.PlayerRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PlayerService(
    private val playerRepository: PlayerRepository
) {
    
    fun getAllPlayers(): List<Player> {
        return playerRepository.findAll()
    }
    
    fun getPlayerById(id: Long): Player {
        return playerRepository.findById(id)
            ?: throw PlayerNotFoundException("ID $id 에 해당하는 플레이어를 찾을 수 없습니다")
    }
    
    fun createPlayer(request: PlayerRequest): Player {
        // 이메일 중복 체크
        if (playerRepository.existsByEmail(request.email)) {
            throw DuplicateEmailException("이미 존재하는 이메일입니다: ${request.email}")
        }
        
        val player = Player(
            id = 0L, // Repository에서 자동 생성
            nickname = request.nickname,
            email = request.email,
            level = request.level,
            totalGamesPlayed = request.totalGamesPlayed,
            favoriteCategory = request.favoriteCategory,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return playerRepository.save(player)
    }
    
    fun updatePlayer(id: Long, request: PlayerRequest): Player {
        val existingPlayer = getPlayerById(id)
        
        // 이메일이 변경되었고, 다른 플레이어가 이미 사용 중인지 체크
        if (existingPlayer.email != request.email && playerRepository.existsByEmail(request.email)) {
            throw DuplicateEmailException("이미 존재하는 이메일입니다: ${request.email}")
        }
        
        val updatedPlayer = Player(
            id = id,
            nickname = request.nickname,
            email = request.email,
            level = request.level,
            totalGamesPlayed = request.totalGamesPlayed,
            favoriteCategory = request.favoriteCategory,
            createdAt = existingPlayer.createdAt,
            updatedAt = LocalDateTime.now()
        )
        
        return playerRepository.update(id, updatedPlayer)
            ?: throw PlayerNotFoundException("ID $id 에 해당하는 플레이어를 찾을 수 없습니다")
    }
    
    fun deletePlayer(id: Long) {
        if (!playerRepository.existsById(id)) {
            throw PlayerNotFoundException("ID $id 에 해당하는 플레이어를 찾을 수 없습니다")
        }
        playerRepository.deleteById(id)
    }
}
