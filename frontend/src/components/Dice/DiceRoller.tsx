import React, { useState } from 'react';
import DiceSet from './DiceSet';

interface DiceRollerProps {
  diceValues: number[];
  rollCount: number;
  canRollMore: boolean;
  canRecordScore: boolean;
  selectedDice: Set<number>;
  isRolling: boolean;
  onRollDice: (rerollIndices: number[]) => void;
  onDiceSelect: (index: number) => void;
  onClearSelection: () => void;
}

const DiceRoller: React.FC<DiceRollerProps> = ({
  diceValues,
  rollCount,
  canRollMore,
  canRecordScore,
  selectedDice,
  isRolling,
  onRollDice,
  onDiceSelect,
  onClearSelection
}) => {
  
  const handleRollAll = () => {
    onRollDice([]);
  };
  
  const handleReroll = () => {
    onRollDice(Array.from(selectedDice));
  };
  
  const getRollButtonText = () => {
    if (rollCount === 0) return '주사위 굴리기';
    if (selectedDice.size === 0) return '모든 주사위 다시 굴리기';
    return `선택된 주사위 다시 굴리기 (${selectedDice.size}개)`;
  };
  
  const canRoll = canRollMore && !isRolling;
  const hasSelection = selectedDice.size > 0;
  
  return (
    <div className="game-card">
      <div className="text-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-2">주사위</h2>
        <div className="flex justify-center items-center space-x-4 text-sm text-gray-600">
          <span>굴린 횟수: {rollCount}/3</span>
          {rollCount > 0 && (
            <span className={canRollMore ? 'text-green-600' : 'text-red-600'}>
              {canRollMore ? '더 굴릴 수 있음' : '굴리기 완료'}
            </span>
          )}
        </div>
      </div>
      
      <DiceSet
        diceValues={diceValues}
        selectedDice={selectedDice}
        isRolling={isRolling}
        onDiceClick={onDiceSelect}
        canSelectDice={rollCount > 0 && canRollMore}
      />
      
      <div className="flex flex-col items-center space-y-3 mt-6">
        {rollCount > 0 && canRollMore && hasSelection && (
          <div className="text-sm text-blue-600 bg-blue-50 px-3 py-1 rounded-full">
            선택된 주사위만 다시 굴립니다
          </div>
        )}
        
        <div className="flex space-x-3">
          <button
            onClick={rollCount === 0 || selectedDice.size === 0 ? handleRollAll : handleReroll}
            disabled={!canRoll}
            className="btn-primary min-w-[200px]"
          >
            {isRolling ? (
              <span className="flex items-center justify-center">
                <div className="animate-spin mr-2">🎲</div>
                굴리는 중...
              </span>
            ) : (
              getRollButtonText()
            )}
          </button>
          
          {rollCount > 0 && canRollMore && hasSelection && (
            <button
              onClick={onClearSelection}
              className="btn-secondary"
            >
              선택 해제
            </button>
          )}
        </div>
        
        {rollCount > 0 && (
          <div className="text-xs text-gray-500 text-center max-w-md">
            💡 팁: 다시 굴리고 싶은 주사위를 클릭하여 선택하세요. 
            선택하지 않으면 모든 주사위가 다시 굴려집니다.
          </div>
        )}
      </div>
    </div>
  );
};

export default DiceRoller;
