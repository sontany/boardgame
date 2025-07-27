import React from 'react';
import { Game } from '../../types/game';

interface GameStatusProps {
  game: Game;
  rollCount: number;
  canRollMore: boolean;
  canRecordScore: boolean;
}

const GameStatus: React.FC<GameStatusProps> = ({
  game,
  rollCount,
  canRollMore,
  canRecordScore
}) => {
  
  const getStatusMessage = () => {
    if (game.status === 'COMPLETED') {
      return '🎉 게임 완료!';
    }
    
    if (rollCount === 0) {
      return '🎲 주사위를 굴려서 게임을 시작하세요';
    }
    
    if (canRollMore) {
      return `🎯 ${3 - rollCount}번 더 굴릴 수 있습니다`;
    }
    
    if (canRecordScore) {
      return '📝 점수를 기록해주세요';
    }
    
    return '⏳ 다음 턴을 준비 중...';
  };
  
  const getStatusColor = () => {
    if (game.status === 'COMPLETED') return 'text-green-600';
    if (rollCount === 0) return 'text-blue-600';
    if (canRollMore) return 'text-orange-600';
    if (canRecordScore) return 'text-purple-600';
    return 'text-gray-600';
  };
  
  return (
    <div className="game-card">
      <div className="text-center">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">
          요트 다이스 게임
        </h1>
        
        <div className="flex justify-center items-center space-x-6 mb-4">
          <div className="text-center">
            <div className="text-sm text-gray-500">플레이어</div>
            <div className="text-lg font-semibold text-gray-800">
              {game.playerName}
            </div>
          </div>
          
          <div className="w-px h-8 bg-gray-300"></div>
          
          <div className="text-center">
            <div className="text-sm text-gray-500">현재 턴</div>
            <div className="text-lg font-semibold text-gray-800">
              {game.currentTurn}/13
            </div>
          </div>
          
          <div className="w-px h-8 bg-gray-300"></div>
          
          <div className="text-center">
            <div className="text-sm text-gray-500">굴린 횟수</div>
            <div className="text-lg font-semibold text-gray-800">
              {rollCount}/3
            </div>
          </div>
        </div>
        
        <div className={`text-lg font-medium ${getStatusColor()}`}>
          {getStatusMessage()}
        </div>
        
        {game.status === 'COMPLETED' && (
          <div className="mt-4 p-3 bg-green-50 rounded-lg">
            <div className="text-sm text-green-700">
              게임이 완료되었습니다! 최종 점수를 확인해보세요.
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default GameStatus;
