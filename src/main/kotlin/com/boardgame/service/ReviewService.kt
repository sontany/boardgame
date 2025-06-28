package com.boardgame.service

import com.boardgame.domain.Review
import com.boardgame.dto.request.ReviewRequest
import com.boardgame.exception.BoardGameNotFoundException
import com.boardgame.exception.PlayerNotFoundException
import com.boardgame.exception.ReviewNotFoundException
import com.boardgame.repository.BoardGameRepository
import com.boardgame.repository.PlayerRepository
import com.boardgame.repository.ReviewRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val boardGameRepository: BoardGameRepository,
    private val playerRepository: PlayerRepository
) {
    
    fun getAllReviews(): List<Review> {
        return reviewRepository.findAll()
    }
    
    fun getReviewById(id: Long): Review {
        return reviewRepository.findById(id)
            ?: throw ReviewNotFoundException("ID $id 에 해당하는 리뷰를 찾을 수 없습니다")
    }
    
    fun getReviewsByBoardGame(boardGameId: Long): List<Review> {
        return reviewRepository.findByBoardGameId(boardGameId)
    }
    
    fun getReviewsByPlayer(playerId: Long): List<Review> {
        return reviewRepository.findByPlayerId(playerId)
    }
    
    fun createReview(request: ReviewRequest): Review {
        // 보드게임 존재 여부 확인
        if (!boardGameRepository.existsById(request.boardGameId)) {
            throw BoardGameNotFoundException("ID ${request.boardGameId} 에 해당하는 보드게임을 찾을 수 없습니다")
        }
        
        // 플레이어 존재 여부 확인
        if (!playerRepository.existsById(request.playerId)) {
            throw PlayerNotFoundException("ID ${request.playerId} 에 해당하는 플레이어를 찾을 수 없습니다")
        }
        
        val review = Review(
            id = 0L, // Repository에서 자동 생성
            boardGameId = request.boardGameId,
            playerId = request.playerId,
            rating = request.rating,
            comment = request.comment,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        return reviewRepository.save(review)
    }
    
    fun updateReview(id: Long, request: ReviewRequest): Review {
        val existingReview = getReviewById(id)
        
        // 보드게임 존재 여부 확인
        if (!boardGameRepository.existsById(request.boardGameId)) {
            throw BoardGameNotFoundException("ID ${request.boardGameId} 에 해당하는 보드게임을 찾을 수 없습니다")
        }
        
        // 플레이어 존재 여부 확인
        if (!playerRepository.existsById(request.playerId)) {
            throw PlayerNotFoundException("ID ${request.playerId} 에 해당하는 플레이어를 찾을 수 없습니다")
        }
        
        val updatedReview = Review(
            id = id,
            boardGameId = request.boardGameId,
            playerId = request.playerId,
            rating = request.rating,
            comment = request.comment,
            createdAt = existingReview.createdAt,
            updatedAt = LocalDateTime.now()
        )
        
        return reviewRepository.update(id, updatedReview)
            ?: throw ReviewNotFoundException("ID $id 에 해당하는 리뷰를 찾을 수 없습니다")
    }
    
    fun deleteReview(id: Long) {
        if (!reviewRepository.existsById(id)) {
            throw ReviewNotFoundException("ID $id 에 해당하는 리뷰를 찾을 수 없습니다")
        }
        reviewRepository.deleteById(id)
    }
}
