package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 플레이어를 나타내는 도메인 객체
 * 
 * @property name 플레이어 이름
 * @property scoreCard 점수판
 */
data class Player(
    val name: String,
    val scoreCard: ScoreCard = ScoreCard()
) {
    
    init {
        require(name.isNotBlank()) { "플레이어 이름은 비어있을 수 없습니다." }
    }
    
    /**
     * 점수를 기록합니다.
     * 
     * @param score 기록할 점수
     * @return 점수가 기록된 새로운 Player 객체
     */
    fun recordScore(score: Score): Player {
        val newScoreCard = ScoreCard(scoreCard.getAllScores().toMutableMap())
        newScoreCard.recordScore(score)
        return this.copy(scoreCard = newScoreCard)
    }
    
    /**
     * 게임이 완료되었는지 확인합니다.
     * 
     * @return 모든 카테고리에 점수가 기록되었으면 true
     */
    fun isGameComplete(): Boolean {
        return scoreCard.isComplete()
    }
    
    /**
     * 총점을 반환합니다.
     * 
     * @return 현재 총점
     */
    fun getTotalScore(): Int {
        return scoreCard.getTotalScore()
    }
}
