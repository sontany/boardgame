import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import GameService from '../services/gameService';
import { Game, ScoreCardResponse, UPPER_SECTION_CATEGORIES, LOWER_SECTION_CATEGORIES } from '../types/game';

const GameComplete: React.FC = () => {
  const { gameId } = useParams<{ gameId: string }>();
  const navigate = useNavigate();
  
  const [game, setGame] = useState<Game | null>(null);
  const [scoreCard, setScoreCard] = useState<ScoreCardResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const gameIdNum = gameId ? parseInt(gameId) : 0;
  
  useEffect(() => {
    if (gameIdNum) {
      loadGameData();
    } else {
      navigate('/');
    }
  }, [gameIdNum]);
  
  const loadGameData = async () => {
    try {
      setIsLoading(true);
      setError(null);
      
      const [gameData, scoreCardData] = await Promise.all([
        GameService.getGame(gameIdNum),
        GameService.getScoreCard(gameIdNum)
      ]);
      
      setGame(gameData);
      setScoreCard(scoreCardData);
      
    } catch (error) {
      console.error('게임 데이터 로드 실패:', error);
      setError(error instanceof Error ? error.message : '게임 데이터를 불러올 수 없습니다.');
    } finally {
      setIsLoading(false);
    }
  };
  
  const getScoreAnalysis = () => {
    if (!scoreCard) return null;
    
    const totalScore = scoreCard.totalScore;
    let rating = '';
    let message = '';
    let emoji = '';
    
    if (totalScore >= 300) {
      rating = '전설적인';
      message = '완벽한 게임입니다! 요트 다이스 마스터!';
      emoji = '🏆';
    } else if (totalScore >= 250) {
      rating = '훌륭한';
      message = '정말 잘하셨습니다! 고수의 실력이네요!';
      emoji = '🥇';
    } else if (totalScore >= 200) {
      rating = '좋은';
      message = '좋은 점수입니다! 계속 연습하면 더 잘할 수 있어요!';
      emoji = '🥈';
    } else if (totalScore >= 150) {
      rating = '괜찮은';
      message = '나쁘지 않은 점수네요! 다음엔 더 잘할 수 있을 거예요!';
      emoji = '🥉';
    } else {
      rating = '아쉬운';
      message = '다음 게임에서는 더 좋은 결과가 있을 거예요!';
      emoji = '💪';
    }
    
    return { rating, message, emoji };
  };
  
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin text-4xl mb-4">🎲</div>
          <div className="text-lg text-gray-600">결과를 불러오는 중...</div>
        </div>
      </div>
    );
  }
  
  if (error || !game || !scoreCard) {
    return (
      <div className="min-h-screen flex items-center justify-center p-4">
        <div className="game-card text-center max-w-md">
          <div className="text-red-500 text-6xl mb-4">⚠️</div>
          <h2 className="text-xl font-bold text-gray-800 mb-2">오류 발생</h2>
          <p className="text-gray-600 mb-4">{error || '게임 결과를 불러올 수 없습니다.'}</p>
          <button
            onClick={() => navigate('/')}
            className="btn-primary"
          >
            홈으로 돌아가기
          </button>
        </div>
      </div>
    );
  }
  
  const analysis = getScoreAnalysis();
  
  return (
    <div className="min-h-screen p-4">
      <div className="max-w-4xl mx-auto">
        {/* 게임 완료 헤더 */}
        <div className="game-card text-center mb-6">
          <div className="text-6xl mb-4">{analysis?.emoji}</div>
          <h1 className="text-3xl font-bold text-gray-800 mb-2">
            게임 완료!
          </h1>
          <p className="text-lg text-gray-600 mb-4">
            {game.playerName}님의 {analysis?.rating} 결과
          </p>
          <p className="text-gray-700">
            {analysis?.message}
          </p>
        </div>
        
        {/* 최종 점수 */}
        <div className="game-card mb-6">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">최종 점수</h2>
            <div className="text-6xl font-bold text-blue-600 mb-4">
              {scoreCard.totalScore}
            </div>
            <div className="grid grid-cols-3 gap-4 text-center">
              <div className="p-3 bg-blue-50 rounded-lg">
                <div className="text-sm text-gray-600">상위 섹션</div>
                <div className="text-xl font-bold text-blue-600">
                  {scoreCard.upperSectionTotal}
                </div>
              </div>
              <div className="p-3 bg-green-50 rounded-lg">
                <div className="text-sm text-gray-600">보너스</div>
                <div className="text-xl font-bold text-green-600">
                  {scoreCard.upperSectionBonus}
                </div>
              </div>
              <div className="p-3 bg-purple-50 rounded-lg">
                <div className="text-sm text-gray-600">하위 섹션</div>
                <div className="text-xl font-bold text-purple-600">
                  {scoreCard.lowerSectionTotal}
                </div>
              </div>
            </div>
          </div>
        </div>
        
        {/* 상세 점수 */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
          {/* 상위 섹션 */}
          <div className="game-card">
            <h3 className="text-lg font-bold text-gray-800 mb-4">상위 섹션</h3>
            <div className="space-y-2">
              {UPPER_SECTION_CATEGORIES.map(category => {
                const score = scoreCard.scores[category];
                return (
                  <div key={category} className="flex justify-between items-center p-2 border-b">
                    <span className="text-gray-700">{category}</span>
                    <span className="font-semibold">{score?.points || 0}</span>
                  </div>
                );
              })}
              <div className="flex justify-between items-center p-2 bg-yellow-50 rounded mt-2">
                <span className="font-semibold">소계</span>
                <span className="font-bold">{scoreCard.upperSectionTotal}</span>
              </div>
              <div className="flex justify-between items-center p-2 bg-green-50 rounded">
                <span className="font-semibold">보너스</span>
                <span className="font-bold text-green-600">{scoreCard.upperSectionBonus}</span>
              </div>
            </div>
          </div>
          
          {/* 하위 섹션 */}
          <div className="game-card">
            <h3 className="text-lg font-bold text-gray-800 mb-4">하위 섹션</h3>
            <div className="space-y-2">
              {LOWER_SECTION_CATEGORIES.map(category => {
                const score = scoreCard.scores[category];
                return (
                  <div key={category} className="flex justify-between items-center p-2 border-b">
                    <span className="text-gray-700">{category}</span>
                    <span className="font-semibold">{score?.points || 0}</span>
                  </div>
                );
              })}
              <div className="flex justify-between items-center p-2 bg-purple-50 rounded mt-2">
                <span className="font-semibold">소계</span>
                <span className="font-bold">{scoreCard.lowerSectionTotal}</span>
              </div>
            </div>
          </div>
        </div>
        
        {/* 게임 정보 */}
        <div className="game-card mb-6">
          <h3 className="text-lg font-bold text-gray-800 mb-4">게임 정보</h3>
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span className="text-gray-600">플레이어:</span>
              <span className="ml-2 font-semibold">{game.playerName}</span>
            </div>
            <div>
              <span className="text-gray-600">완료 턴:</span>
              <span className="ml-2 font-semibold">{game.currentTurn}</span>
            </div>
            <div>
              <span className="text-gray-600">시작 시간:</span>
              <span className="ml-2 font-semibold">
                {new Date(game.createdAt).toLocaleString()}
              </span>
            </div>
            <div>
              <span className="text-gray-600">완료 시간:</span>
              <span className="ml-2 font-semibold">
                {game.completedAt ? new Date(game.completedAt).toLocaleString() : '-'}
              </span>
            </div>
          </div>
        </div>
        
        {/* 액션 버튼 */}
        <div className="text-center space-x-4">
          <button
            onClick={() => navigate('/')}
            className="btn-primary"
          >
            새 게임 시작
          </button>
          <button
            onClick={() => navigate(`/game/${gameIdNum}`)}
            className="btn-secondary"
          >
            게임 다시 보기
          </button>
        </div>
      </div>
    </div>
  );
};

export default GameComplete;
