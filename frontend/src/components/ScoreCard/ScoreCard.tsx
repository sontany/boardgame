import React from 'react';
import ScoreCategory from './ScoreCategory';
import { 
  ScoreCardResponse, 
  Category, 
  UPPER_SECTION_CATEGORIES, 
  LOWER_SECTION_CATEGORIES 
} from '../../types/game';

interface ScoreCardProps {
  scoreCard: ScoreCardResponse;
  gameId: number;
  currentDice: number[];
  onScoreRecord: (category: Category) => void;
  canRecordScore: boolean;
}

const ScoreCard: React.FC<ScoreCardProps> = ({
  scoreCard,
  gameId,
  currentDice,
  onScoreRecord,
  canRecordScore
}) => {
  
  const renderSection = (title: string, categories: Category[], showBonus = false) => (
    <div className="mb-6">
      <h3 className="text-lg font-bold text-gray-800 mb-3 border-b pb-2">
        {title}
      </h3>
      
      <div className="space-y-2">
        {categories.map(category => (
          <ScoreCategory
            key={category}
            category={category}
            score={scoreCard.scores[category]?.points}
            isFilled={!!scoreCard.scores[category]}
            isAvailable={scoreCard.availableCategories.includes(category)}
            gameId={gameId}
            currentDice={currentDice}
            onScoreRecord={onScoreRecord}
            canRecordScore={canRecordScore}
          />
        ))}
      </div>
      
      {showBonus && (
        <div className="mt-4 pt-3 border-t border-gray-200">
          <div className="flex justify-between items-center p-2 bg-yellow-50 rounded">
            <div>
              <div className="font-semibold text-gray-800">상위 섹션 소계</div>
              <div className="text-xs text-gray-500">63점 이상 시 35점 보너스</div>
            </div>
            <div className="text-right">
              <div className="text-lg font-bold text-gray-800">
                {scoreCard.upperSectionTotal}
              </div>
            </div>
          </div>
          
          <div className="flex justify-between items-center p-2 bg-green-50 rounded mt-2">
            <div className="font-semibold text-gray-800">보너스</div>
            <div className="text-lg font-bold text-green-600">
              {scoreCard.upperSectionBonus}
            </div>
          </div>
        </div>
      )}
    </div>
  );
  
  return (
    <div className="game-card">
      <div className="text-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">점수판</h2>
        <div className="text-sm text-gray-600 mt-1">
          {scoreCard.isComplete ? '게임 완료!' : `남은 카테고리: ${scoreCard.availableCategories.length}개`}
        </div>
      </div>
      
      {/* 상위 섹션 */}
      {renderSection('상위 섹션 (Upper Section)', UPPER_SECTION_CATEGORIES, true)}
      
      {/* 하위 섹션 */}
      {renderSection('하위 섹션 (Lower Section)', LOWER_SECTION_CATEGORIES)}
      
      {/* 총점 */}
      <div className="mt-6 pt-4 border-t-2 border-gray-300">
        <div className="flex justify-between items-center p-4 bg-blue-50 rounded-lg">
          <div>
            <div className="text-xl font-bold text-gray-800">총점</div>
            <div className="text-sm text-gray-600">
              상위: {scoreCard.upperSectionTotal} + 보너스: {scoreCard.upperSectionBonus} + 하위: {scoreCard.lowerSectionTotal}
            </div>
          </div>
          <div className="text-3xl font-bold text-blue-600">
            {scoreCard.totalScore}
          </div>
        </div>
      </div>
      
      {!canRecordScore && currentDice.length === 0 && (
        <div className="mt-4 text-center text-gray-500 text-sm">
          주사위를 굴린 후 점수를 기록할 수 있습니다
        </div>
      )}
    </div>
  );
};

export default ScoreCard;
