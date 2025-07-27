import React, { useState, useEffect } from 'react';
import { Category, CATEGORY_NAMES } from '../../types/game';
import GameService from '../../services/gameService';

interface ScoreCategoryProps {
  category: Category;
  score?: number;
  isFilled: boolean;
  isAvailable: boolean;
  gameId: number;
  currentDice: number[];
  onScoreRecord: (category: Category) => void;
  canRecordScore: boolean;
}

const ScoreCategory: React.FC<ScoreCategoryProps> = ({
  category,
  score,
  isFilled,
  isAvailable,
  gameId,
  currentDice,
  onScoreRecord,
  canRecordScore
}) => {
  const [previewScore, setPreviewScore] = useState<number | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  
  // 점수 미리보기 로드
  useEffect(() => {
    if (isAvailable && canRecordScore && currentDice.length > 0) {
      loadPreviewScore();
    } else {
      setPreviewScore(null);
    }
  }, [isAvailable, canRecordScore, currentDice, category, gameId]);
  
  const loadPreviewScore = async () => {
    try {
      setIsLoading(true);
      const preview = await GameService.previewScore(gameId, category);
      setPreviewScore(preview);
    } catch (error) {
      console.error('점수 미리보기 로드 실패:', error);
      setPreviewScore(null);
    } finally {
      setIsLoading(false);
    }
  };
  
  const handleClick = () => {
    if (isAvailable && canRecordScore) {
      onScoreRecord(category);
    }
  };
  
  const getCategoryDescription = (category: Category): string => {
    const descriptions: Record<Category, string> = {
      [Category.ONES]: '1의 합계',
      [Category.TWOS]: '2의 합계',
      [Category.THREES]: '3의 합계',
      [Category.FOURS]: '4의 합계',
      [Category.FIVES]: '5의 합계',
      [Category.SIXES]: '6의 합계',
      [Category.THREE_OF_A_KIND]: '같은 숫자 3개 이상 - 모든 주사위 합',
      [Category.FOUR_OF_A_KIND]: '같은 숫자 4개 이상 - 모든 주사위 합',
      [Category.FULL_HOUSE]: '3개 + 2개 조합 - 25점',
      [Category.SMALL_STRAIGHT]: '연속된 4개 숫자 - 30점',
      [Category.LARGE_STRAIGHT]: '연속된 5개 숫자 - 40점',
      [Category.YACHT]: '같은 숫자 5개 - 50점',
      [Category.CHANCE]: '모든 주사위의 합'
    };
    return descriptions[category];
  };
  
  const displayScore = isFilled ? score : (previewScore !== null ? previewScore : '-');
  const isClickable = isAvailable && canRecordScore && !isLoading;
  
  return (
    <div
      onClick={handleClick}
      className={`
        score-category
        ${isFilled ? 'filled' : ''}
        ${isAvailable ? 'available' : ''}
        ${isClickable ? 'hover:shadow-md' : ''}
        ${previewScore !== null && !isFilled ? 'border-blue-300 bg-blue-50' : ''}
      `}
    >
      <div className="flex justify-between items-center">
        <div className="flex-1">
          <div className="font-semibold text-gray-800">
            {CATEGORY_NAMES[category]}
          </div>
          <div className="text-xs text-gray-500 mt-1">
            {getCategoryDescription(category)}
          </div>
        </div>
        
        <div className="text-right">
          <div className={`
            text-lg font-bold
            ${isFilled ? 'text-gray-800' : 'text-blue-600'}
            ${isLoading ? 'animate-pulse' : ''}
          `}>
            {isLoading ? '...' : displayScore}
          </div>
          
          {previewScore !== null && !isFilled && (
            <div className="text-xs text-blue-500">
              예상 점수
            </div>
          )}
        </div>
      </div>
      
      {isClickable && previewScore !== null && (
        <div className="mt-2 text-xs text-blue-600 text-center">
          클릭하여 점수 기록
        </div>
      )}
    </div>
  );
};

export default ScoreCategory;
