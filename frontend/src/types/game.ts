// 게임 관련 타입 정의

export interface Game {
  gameId: number;
  playerName: string;
  status: 'IN_PROGRESS' | 'COMPLETED';
  currentTurn: number;
  createdAt: string;
  completedAt?: string;
}

export interface DiceRollResponse {
  diceValues: number[];
  rollCount: number;
  canRollMore: boolean;
  canRecordScore: boolean;
}

export interface ScoreInfo {
  category: Category;
  points: number;
  recordedAt: string;
}

export interface ScoreCardResponse {
  scores: Record<Category, ScoreInfo>;
  upperSectionTotal: number;
  upperSectionBonus: number;
  lowerSectionTotal: number;
  totalScore: number;
  isComplete: boolean;
  availableCategories: Category[];
}

export enum Category {
  ONES = 'ONES',
  TWOS = 'TWOS',
  THREES = 'THREES',
  FOURS = 'FOURS',
  FIVES = 'FIVES',
  SIXES = 'SIXES',
  THREE_OF_A_KIND = 'THREE_OF_A_KIND',
  FOUR_OF_A_KIND = 'FOUR_OF_A_KIND',
  FULL_HOUSE = 'FULL_HOUSE',
  SMALL_STRAIGHT = 'SMALL_STRAIGHT',
  LARGE_STRAIGHT = 'LARGE_STRAIGHT',
  YACHT = 'YACHT',
  CHANCE = 'CHANCE'
}

export interface CreateGameRequest {
  playerName: string;
}

export interface RollDiceRequest {
  rerollIndices: number[];
}

export interface RecordScoreRequest {
  category: Category;
}

// 게임 상태 관리를 위한 타입
export interface GameState {
  game: Game | null;
  currentDice: number[];
  rollCount: number;
  canRollMore: boolean;
  canRecordScore: boolean;
  scoreCard: ScoreCardResponse | null;
  selectedDice: Set<number>;
  isLoading: boolean;
  error: string | null;
}

// 카테고리 표시명 매핑
export const CATEGORY_NAMES: Record<Category, string> = {
  [Category.ONES]: '1 (Ones)',
  [Category.TWOS]: '2 (Twos)',
  [Category.THREES]: '3 (Threes)',
  [Category.FOURS]: '4 (Fours)',
  [Category.FIVES]: '5 (Fives)',
  [Category.SIXES]: '6 (Sixes)',
  [Category.THREE_OF_A_KIND]: 'Three of a Kind',
  [Category.FOUR_OF_A_KIND]: 'Four of a Kind',
  [Category.FULL_HOUSE]: 'Full House',
  [Category.SMALL_STRAIGHT]: 'Small Straight',
  [Category.LARGE_STRAIGHT]: 'Large Straight',
  [Category.YACHT]: 'Yacht',
  [Category.CHANCE]: 'Chance'
};

// 상위 섹션 카테고리
export const UPPER_SECTION_CATEGORIES = [
  Category.ONES,
  Category.TWOS,
  Category.THREES,
  Category.FOURS,
  Category.FIVES,
  Category.SIXES
];

// 하위 섹션 카테고리
export const LOWER_SECTION_CATEGORIES = [
  Category.THREE_OF_A_KIND,
  Category.FOUR_OF_A_KIND,
  Category.FULL_HOUSE,
  Category.SMALL_STRAIGHT,
  Category.LARGE_STRAIGHT,
  Category.YACHT,
  Category.CHANCE
];
