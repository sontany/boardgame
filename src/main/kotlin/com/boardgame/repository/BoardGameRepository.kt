package com.boardgame.repository

import com.boardgame.domain.BoardGame
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class BoardGameRepository {
    
    private val boardGames = ConcurrentHashMap<Long, BoardGame>()
    private val idGenerator = AtomicLong(1)
    
    init {
        // 초기 샘플 데이터
        val sampleBoardGames = listOf(
            BoardGame(1L, "카탄", "자원 수집 전략 게임", 3, 4, 90, 3, "전략", "코스모스", 1995, 4.5),
            BoardGame(2L, "스플렌더", "보석 상인 게임", 2, 4, 30, 2, "엔진빌딩", "스페이스 카우보이", 2014, 4.2),
            BoardGame(3L, "윙스팬", "조류 테마 엔진 빌딩", 1, 5, 70, 3, "엔진빌딩", "스톤마이어", 2019, 4.7),
            BoardGame(4L, "아줄", "타일 배치 게임", 2, 4, 45, 2, "추상전략", "플랜 B 게임즈", 2017, 4.3),
            BoardGame(5L, "티켓 투 라이드", "기차 노선 연결 게임", 2, 5, 60, 2, "가족게임", "데이즈 오브 원더", 2004, 4.4)
        )
        
        sampleBoardGames.forEach { boardGame ->
            boardGames[boardGame.id] = boardGame
            idGenerator.set(maxOf(idGenerator.get(), boardGame.id + 1))
        }
    }
    
    fun findAll(): List<BoardGame> {
        return boardGames.values.toList()
    }
    
    fun findById(id: Long): BoardGame? {
        return boardGames[id]
    }
    
    fun findByCategory(category: String): List<BoardGame> {
        return boardGames.values.filter { it.category.equals(category, ignoreCase = true) }
    }
    
    fun save(boardGame: BoardGame): BoardGame {
        val id = if (boardGame.id == 0L) idGenerator.getAndIncrement() else boardGame.id
        val savedBoardGame = boardGame.copy(
            id = id,
            updatedAt = LocalDateTime.now()
        )
        boardGames[id] = savedBoardGame
        return savedBoardGame
    }
    
    fun update(id: Long, boardGame: BoardGame): BoardGame? {
        return if (boardGames.containsKey(id)) {
            val updatedBoardGame = boardGame.copy(
                id = id,
                updatedAt = LocalDateTime.now()
            )
            boardGames[id] = updatedBoardGame
            updatedBoardGame
        } else {
            null
        }
    }
    
    fun deleteById(id: Long): Boolean {
        return boardGames.remove(id) != null
    }
    
    fun existsById(id: Long): Boolean {
        return boardGames.containsKey(id)
    }
}
