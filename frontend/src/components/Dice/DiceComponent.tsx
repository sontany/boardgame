import React from 'react';

interface DiceComponentProps {
  value: number;
  isSelected: boolean;
  isRolling: boolean;
  onClick: () => void;
  disabled?: boolean;
}

const DiceComponent: React.FC<DiceComponentProps> = ({
  value,
  isSelected,
  isRolling,
  onClick,
  disabled = false
}) => {
  
  // 주사위 점 패턴을 렌더링하는 함수
  const renderDots = (value: number) => {
    const dotPositions: Record<number, string[]> = {
      1: ['center'],
      2: ['top-left', 'bottom-right'],
      3: ['top-left', 'center', 'bottom-right'],
      4: ['top-left', 'top-right', 'bottom-left', 'bottom-right'],
      5: ['top-left', 'top-right', 'center', 'bottom-left', 'bottom-right'],
      6: ['top-left', 'top-right', 'middle-left', 'middle-right', 'bottom-left', 'bottom-right']
    };
    
    const positions = dotPositions[value] || [];
    
    return (
      <div className="relative w-full h-full">
        {positions.map((position, index) => (
          <div
            key={index}
            className={`absolute w-2 h-2 bg-gray-800 rounded-full ${getPositionClass(position)}`}
          />
        ))}
      </div>
    );
  };
  
  const getPositionClass = (position: string): string => {
    const classes: Record<string, string> = {
      'top-left': 'top-1 left-1',
      'top-right': 'top-1 right-1',
      'middle-left': 'top-1/2 left-1 -translate-y-1/2',
      'middle-right': 'top-1/2 right-1 -translate-y-1/2',
      'center': 'top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2',
      'bottom-left': 'bottom-1 left-1',
      'bottom-right': 'bottom-1 right-1'
    };
    return classes[position] || '';
  };
  
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`
        dice
        ${isSelected ? 'selected' : ''}
        ${isRolling ? 'rolling' : ''}
        ${disabled ? 'opacity-50 cursor-not-allowed' : ''}
      `}
    >
      {isRolling ? (
        <div className="animate-spin text-2xl">🎲</div>
      ) : (
        renderDots(value)
      )}
    </button>
  );
};

export default DiceComponent;
