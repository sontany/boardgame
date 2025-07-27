package com.boardgame.application.yacht

/**
 * 요트다이스 게임의 점수판을 나타내는 도메인 객체
 * 
 * @property scores 기록된 점수들의 맵 (카테고리 -> 점수)
 */
data class ScoreCard(
    private val scores: MutableMap<Category, Score> = mutableMapOf()
) {
    
    /**
     * 점수를 기록합니다.
     * 
     * @param score 기록할 점수
     * @throws IllegalStateException 이미 해당 카테고리에 점수가 기록된 경우
     */
    fun recordScore(score: Score) {
        require(!isRecorded(score.category)) { 
            "이미 ${score.category.displayName} 카테고리에 점수가 기록되었습니다." 
        }
        scores[score.category] = score
    }
    
    /**
     * 특정 카테고리에 점수가 기록되었는지 확인합니다.
     * 
     * @param category 확인할 카테고리
     * @return 기록되었으면 true, 아니면 false
     */
    fun isRecorded(category: Category): Boolean {
        return scores.containsKey(category)
    }
    
    /**
     * 특정 카테고리의 점수를 반환합니다.
     * 
     * @param category 조회할 카테고리
     * @return 기록된 점수, 없으면 null
     */
    fun getScore(category: Category): Score? {
        return scores[category]
    }
    
    /**
     * 상위 섹션 총점을 계산합니다.
     * 
     * @return 상위 섹션 총점
     */
    fun getUpperSectionTotal(): Int {
        return Category.upperSectionCategories()
            .mapNotNull { scores[it] }
            .sumOf { it.points }
    }
    
    /**
     * 하위 섹션 총점을 계산합니다.
     * 
     * @return 하위 섹션 총점
     */
    fun getLowerSectionTotal(): Int {
        return Category.lowerSectionCategories()
            .mapNotNull { scores[it] }
            .sumOf { it.points }
    }
    
    /**
     * 상위 섹션 보너스 점수를 계산합니다.
     * 
     * @return 보너스 점수 (35점 또는 0점)
     */
    fun getUpperSectionBonus(): Int {
        return ScoreCalculator.calculateUpperSectionBonus(getUpperSectionTotal())
    }
    
    /**
     * 총점을 계산합니다.
     * 
     * @return 상위 섹션 + 보너스 + 하위 섹션 총점
     */
    fun getTotalScore(): Int {
        return getUpperSectionTotal() + getUpperSectionBonus() + getLowerSectionTotal()
    }
    
    /**
     * 게임이 완료되었는지 확인합니다.
     * 
     * @return 모든 카테고리에 점수가 기록되었으면 true
     */
    fun isComplete(): Boolean {
        return Category.values().all { isRecorded(it) }
    }
    
    /**
     * 아직 기록되지 않은 카테고리들을 반환합니다.
     * 
     * @return 기록되지 않은 카테고리 리스트
     */
    fun getAvailableCategories(): List<Category> {
        return Category.values().filter { !isRecorded(it) }
    }
    
    /**
     * 기록된 모든 점수를 반환합니다.
     * 
     * @return 점수 맵의 복사본
     */
    fun getAllScores(): Map<Category, Score> {
        return scores.toMap()
    }
}
