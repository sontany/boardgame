import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import GameService from '../services/gameService';

const GameStart: React.FC = () => {
  const [playerName, setPlayerName] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!playerName.trim()) {
      setError('플레이어 이름을 입력해주세요.');
      return;
    }
    
    try {
      setIsLoading(true);
      setError(null);
      
      const game = await GameService.createGame({ playerName: playerName.trim() });
      navigate(`/game/${game.gameId}`);
    } catch (error) {
      console.error('게임 생성 실패:', error);
      setError(error instanceof Error ? error.message : '게임 생성에 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };
  
  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="max-w-md w-full">
        <div className="game-card text-center">
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-gray-800 mb-2 font-game">
              🎲 YACHT DICE
            </h1>
            <p className="text-gray-600">
              클래식 요트 다이스 게임에 오신 것을 환영합니다!
            </p>
          </div>
          
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="playerName" className="block text-sm font-medium text-gray-700 mb-2">
                플레이어 이름
              </label>
              <input
                type="text"
                id="playerName"
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                placeholder="이름을 입력하세요"
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                maxLength={20}
                disabled={isLoading}
              />
            </div>
            
            {error && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg">
                <p className="text-red-600 text-sm">{error}</p>
              </div>
            )}
            
            <button
              type="submit"
              disabled={isLoading || !playerName.trim()}
              className="w-full btn-primary py-3 text-lg"
            >
              {isLoading ? (
                <span className="flex items-center justify-center">
                  <div className="animate-spin mr-2">⏳</div>
                  게임 생성 중...
                </span>
              ) : (
                '게임 시작'
              )}
            </button>
          </form>
          
          <div className="mt-8 pt-6 border-t border-gray-200">
            <h3 className="text-lg font-semibold text-gray-800 mb-3">게임 규칙</h3>
            <div className="text-sm text-gray-600 space-y-2 text-left">
              <p>• 5개의 주사위를 사용하여 13개의 카테고리에 점수를 기록합니다</p>
              <p>• 각 턴마다 최대 3번까지 주사위를 굴릴 수 있습니다</p>
              <p>• 원하는 주사위만 선택하여 다시 굴릴 수 있습니다</p>
              <p>• 상위 섹션에서 63점 이상 획득 시 35점 보너스를 받습니다</p>
              <p>• 모든 카테고리를 채우면 게임이 종료됩니다</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GameStart;
