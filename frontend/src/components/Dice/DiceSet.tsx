import React from 'react';
import DiceComponent from './DiceComponent';

interface DiceSetProps {
  diceValues: number[];
  selectedDice: Set<number>;
  isRolling: boolean;
  onDiceClick: (index: number) => void;
  canSelectDice: boolean;
}

const DiceSet: React.FC<DiceSetProps> = ({
  diceValues,
  selectedDice,
  isRolling,
  onDiceClick,
  canSelectDice
}) => {
  
  if (diceValues.length === 0) {
    return (
      <div className="flex justify-center items-center space-x-4 p-8">
        <div className="text-gray-500 text-lg">주사위를 굴려주세요</div>
      </div>
    );
  }
  
  return (
    <div className="flex justify-center items-center space-x-4 p-4">
      {diceValues.map((value, index) => (
        <div key={index} className="flex flex-col items-center space-y-2">
          <DiceComponent
            value={value}
            isSelected={selectedDice.has(index)}
            isRolling={isRolling}
            onClick={() => onDiceClick(index)}
            disabled={!canSelectDice || isRolling}
          />
          <div className="text-xs text-gray-500">
            {index + 1}
          </div>
        </div>
      ))}
    </div>
  );
};

export default DiceSet;
