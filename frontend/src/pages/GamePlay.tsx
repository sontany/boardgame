import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import GameStatus from '../components/Game/GameStatus';
import DiceRoller from '../components/Dice/DiceRoller';
import ScoreCard from '../components/ScoreCard/ScoreCard';
import GameService from '../services/gameService';
import { Game, ScoreCardResponse, Category } from '../types/game';

const GamePlay: React.FC = () => {
  const { gameId } = useParams<{ gameId: string }>();
  const navigate = useNavigate();
  
  // 게임 상태
  const [game, setGame] = useState<Game | null>(null);
  const [scoreCard, setScoreCard] = useState<ScoreCardResponse | null>(null);
  const [currentDice, setCurrentDice] = useState<number[]>([]);
  const [rollCount, setRollCount] = useState(0);
  const [canRollMore, setCanRollMore] = useState(true);
  const [canRecordScore, setCanRecordScore] = useState(false);
  const [selectedDice, setSelectedDice] = useState<Set<number>>(new Set());
  
  // UI 상태
  const [isRolling, setIsRolling] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  const gameIdNum = gameId ? parseInt(gameId) : 0;
  
  // 초기 데이터 로드
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
      
      // 게임이 완료된 경우 완료 페이지로 이동
      if (gameData.status === 'COMPLETED') {
        navigate(`/game/${gameIdNum}/complete`);
      }
    } catch (error) {
      console.error('게임 데이터 로드 실패:', error);
      setError(error instanceof Error ? error.message : '게임 데이터를 불러올 수 없습니다.');
    } finally {
      setIsLoading(false);
    }
  };
  
  const handleRollDice = async (rerollIndices: number[]) => {
    if (!game || isRolling) return;
    
    try {
      setIsRolling(true);
      setError(null);
      
      // 롤링 애니메이션을 위한 지연
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      const rollResponse = await GameService.rollDice(gameIdNum, { rerollIndices });
      
      setCurrentDice(rollResponse.diceValues);
      setRollCount(rollResponse.rollCount);
      setCanRollMore(rollResponse.canRollMore);
      setCanRecordScore(rollResponse.canRecordScore);
      setSelectedDice(new Set());
      
    } catch (error) {
      console.error('주사위 굴리기 실패:', error);
      setError(error instanceof Error ? error.message : '주사위 굴리기에 실패했습니다.');
    } finally {
      setIsRolling(false);
    }
  };
  
  const handleDiceSelect = (index: number) => {
    if (!canRollMore || isRolling) return;
    
    const newSelected = new Set(selectedDice);
    if (newSelected.has(index)) {
      newSelected.delete(index);
    } else {
      newSelected.add(index);
    }
    setSelectedDice(newSelected);
  };
  
  const handleClearSelection = () => {
    setSelectedDice(new Set());
  };
  
  const handleScoreRecord = async (category: Category) => {
    if (!game || !canRecordScore) return;
    
    try {
      setError(null);
      
      const updatedScoreCard = await GameService.recordScore(gameIdNum, { category });
      setScoreCard(updatedScoreCard);
      
      // 게임 상태 업데이트
      const updatedGame = await GameService.getGame(gameIdNum);
      setGame(updatedGame);
      
      // 다음 턴 준비
      setCurrentDice([]);
      setRollCount(0);
      setCanRollMore(true);
      setCanRecordScore(false);
      setSelectedDice(new Set());
      
      // 게임 완료 확인
      if (updatedScoreCard.isComplete) {
        navigate(`/game/${gameIdNum}/complete`);
      }
      
    } catch (error) {
      console.error('점수 기록 실패:', error);
      setError(error instanceof Error ? error.message : '점수 기록에 실패했습니다.');
    }
  };
  
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin text-4xl mb-4">🎲</div>
          <div className="text-lg text-gray-600">게임을 불러오는 중...</div>
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
          <p className="text-gray-600 mb-4">{error || '게임을 불러올 수 없습니다.'}</p>
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
  
  return (
    <div className="min-h-screen p-4">
      <div className="max-w-7xl mx-auto">
        {/* 헤더 */}
        <div className="mb-6">
          <GameStatus
            game={game}
            rollCount={rollCount}
            canRollMore={canRollMore}
            canRecordScore={canRecordScore}
          />
        </div>
        
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-red-600">{error}</p>
          </div>
        )}
        
        {/* 메인 게임 영역 */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* 주사위 영역 */}
          <div>
            <DiceRoller
              diceValues={currentDice}
              rollCount={rollCount}
              canRollMore={canRollMore}
              canRecordScore={canRecordScore}
              selectedDice={selectedDice}
              isRolling={isRolling}
              onRollDice={handleRollDice}
              onDiceSelect={handleDiceSelect}
              onClearSelection={handleClearSelection}
            />
          </div>
          
          {/* 점수판 영역 */}
          <div>
            <ScoreCard
              scoreCard={scoreCard}
              gameId={gameIdNum}
              currentDice={currentDice}
              onScoreRecord={handleScoreRecord}
              canRecordScore={canRecordScore}
            />
          </div>
        </div>
        
        {/* 하단 네비게이션 */}
        <div className="mt-8 text-center">
          <button
            onClick={() => navigate('/')}
            className="btn-secondary"
          >
            새 게임 시작
          </button>
        </div>
      </div>
    </div>
  );
};

export default GamePlay;
