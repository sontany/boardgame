package com.boardgame.controller

import com.boardgame.dto.request.ReviewRequest
import com.boardgame.dto.response.ReviewResponse
import com.boardgame.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "리뷰", description = "보드게임 리뷰 관리 API")
@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {
    
    @Operation(summary = "리뷰 목록 조회", description = "등록된 모든 리뷰 목록을 조회합니다")
    @GetMapping
    fun getAllReviews(): ResponseEntity<List<ReviewResponse>> {
        val reviews = reviewService.getAllReviews()
        val responses = reviews.map { ReviewResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "리뷰 상세 조회", description = "특정 리뷰의 상세 정보를 조회합니다")
    @GetMapping("/{id}")
    fun getReviewById(
        @Parameter(description = "리뷰 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.getReviewById(id)
        return ResponseEntity.ok(ReviewResponse.from(review))
    }
    
    @Operation(summary = "보드게임별 리뷰 조회", description = "특정 보드게임의 모든 리뷰를 조회합니다")
    @GetMapping("/boardgame/{boardGameId}")
    fun getReviewsByBoardGame(
        @Parameter(description = "보드게임 ID", required = true)
        @PathVariable boardGameId: Long
    ): ResponseEntity<List<ReviewResponse>> {
        val reviews = reviewService.getReviewsByBoardGame(boardGameId)
        val responses = reviews.map { ReviewResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "플레이어별 리뷰 조회", description = "특정 플레이어가 작성한 모든 리뷰를 조회합니다")
    @GetMapping("/player/{playerId}")
    fun getReviewsByPlayer(
        @Parameter(description = "플레이어 ID", required = true)
        @PathVariable playerId: Long
    ): ResponseEntity<List<ReviewResponse>> {
        val reviews = reviewService.getReviewsByPlayer(playerId)
        val responses = reviews.map { ReviewResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다")
    @PostMapping
    fun createReview(
        @Valid @RequestBody request: ReviewRequest
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.createReview(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewResponse.from(review))
    }
    
    @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다")
    @PutMapping("/{id}")
    fun updateReview(
        @Parameter(description = "리뷰 ID", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody request: ReviewRequest
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.updateReview(id, request)
        return ResponseEntity.ok(ReviewResponse.from(review))
    }
    
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다")
    @DeleteMapping("/{id}")
    fun deleteReview(
        @Parameter(description = "리뷰 ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        reviewService.deleteReview(id)
        return ResponseEntity.noContent().build()
    }
}
