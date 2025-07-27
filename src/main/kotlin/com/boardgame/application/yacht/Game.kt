package com.boardgame.application.yacht

/**
 * 요트다이스 게임 전체 상태를 나타내는 도메인 객체
 * 
 * @property id 게임 ID
 * @property player 플레이어
 * @property currentTurn 현재 턴
 * @property turnNumber 턴 번호
 * @property isCompleted 게임 완료 여부
 */
data class Game(
    val id: Long = 0,
    val player: Player,
    val currentTurn: Turn = Turn.start(),
    val turnNumber: Int = 1,
    val isCompleted: Boolean = false
) {
    
    companion object {
        /**
         * 새로운 게임을 생성합니다.
         * 
         * @param playerName 플레이어 이름
         * @return 새로운 Game 객체
         */
        fun create(playerName: String): Game {
            return Game(
                player = Player(playerName)
            )
        }
    }
    
    /**
     * 주사위를 굴립니다.
     * 
     * @param rerollIndices 다시 굴릴 주사위 인덱스들 (비어있으면 모든 주사위 굴리기)
     * @return 주사위가 굴려진 새로운 Game 객체
     */
    fun rollDice(rerollIndices: Set<Int> = emptySet()): Game {
        val newTurn = if (rerollIndices.isEmpty()) {
            currentTurn.rollAll()
        } else {
            currentTurn.reroll(rerollIndices)
        }
        
        return this.copy(currentTurn = newTurn)
    }
    
    /**
     * 점수를 기록하고 다음 턴으로 진행합니다.
     * 
     * @param category 점수를 기록할 카테고리
     * @return 점수가 기록되고 다음 턴으로 진행된 새로운 Game 객체
     */
    fun recordScoreAndNextTurn(category: Category): Game {
        require(currentTurn.canRecordScore()) { "점수를 기록할 수 없는 상태입니다." }
        
        val score = currentTurn.calculateScore(category)
        val newPlayer = player.recordScore(score)
        val gameCompleted = newPlayer.isGameComplete()
        
        return this.copy(
            player = newPlayer,
            currentTurn = if (gameCompleted) currentTurn else Turn.start(),
            turnNumber = if (gameCompleted) turnNumber else turnNumber + 1,
            isCompleted = gameCompleted
        )
    }
    
    /**
     * 현재 주사위로 특정 카테고리의 점수를 미리 계산합니다.
     * 
     * @param category 점수를 계산할 카테고리
     * @return 계산된 점수
     */
    fun previewScore(category: Category): Int {
        return if (currentTurn.canRecordScore()) {
            currentTurn.calculateScore(category).points
        } else {
            0
        }
    }
    
    /**
     * 사용 가능한 카테고리들을 반환합니다.
     * 
     * @return 아직 기록되지 않은 카테고리 리스트
     */
    fun getAvailableCategories(): List<Category> {
        return player.scoreCard.getAvailableCategories()
    }
}
