package com.boardgame.repository

import com.boardgame.domain.Review
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class ReviewRepository {
    
    private val reviews = ConcurrentHashMap<Long, Review>()
    private val idGenerator = AtomicLong(1)
    
    init {
        // 초기 샘플 데이터
        val sampleReviews = listOf(
            Review(1L, 1L, 1L, 5, "카탄은 정말 재미있는 게임입니다! 전략적 요소가 많아서 매번 다른 재미를 느낄 수 있어요."),
            Review(2L, 1L, 2L, 4, "친구들과 함께 하기 좋은 게임이에요. 다만 초보자에게는 조금 어려울 수 있습니다."),
            Review(3L, 2L, 3L, 5, "스플렌더는 간단하면서도 깊이 있는 게임입니다. 30분 정도로 적당한 플레이 타임이 좋아요."),
            Review(4L, 3L, 4L, 5, "윙스팬은 아름다운 아트워크와 함께 즐길 수 있는 훌륭한 엔진 빌딩 게임입니다."),
            Review(5L, 2L, 5L, 4, "가족과 함께 즐기기 좋은 게임이에요. 룰이 간단해서 누구나 쉽게 배울 수 있습니다.")
        )
        
        sampleReviews.forEach { review ->
            reviews[review.id] = review
            idGenerator.set(maxOf(idGenerator.get(), review.id + 1))
        }
    }
    
    fun findAll(): List<Review> {
        return reviews.values.toList()
    }
    
    fun findById(id: Long): Review? {
        return reviews[id]
    }
    
    fun findByBoardGameId(boardGameId: Long): List<Review> {
        return reviews.values.filter { it.boardGameId == boardGameId }
    }
    
    fun findByPlayerId(playerId: Long): List<Review> {
        return reviews.values.filter { it.playerId == playerId }
    }
    
    fun save(review: Review): Review {
        val id = if (review.id == 0L) idGenerator.getAndIncrement() else review.id
        val savedReview = review.copy(
            id = id,
            updatedAt = LocalDateTime.now()
        )
        reviews[id] = savedReview
        return savedReview
    }
    
    fun update(id: Long, review: Review): Review? {
        return if (reviews.containsKey(id)) {
            val updatedReview = review.copy(
                id = id,
                updatedAt = LocalDateTime.now()
            )
            reviews[id] = updatedReview
            updatedReview
        } else {
            null
        }
    }
    
    fun deleteById(id: Long): Boolean {
        return reviews.remove(id) != null
    }
    
    fun existsById(id: Long): Boolean {
        return reviews.containsKey(id)
    }
}
