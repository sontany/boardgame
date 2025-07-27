import api from './api';
import {
  Game,
  DiceRollResponse,
  ScoreCardResponse,
  CreateGameRequest,
  RollDiceRequest,
  RecordScoreRequest,
  Category
} from '../types/game';

export class GameService {
  
  /**
   * 새 게임을 생성합니다.
   */
  static async createGame(request: CreateGameRequest): Promise<Game> {
    const response = await api.post<Game>('/api/yacht/games', request);
    return response.data;
  }
  
  /**
   * 게임 상태를 조회합니다.
   */
  static async getGame(gameId: number): Promise<Game> {
    const response = await api.get<Game>(`/api/yacht/games/${gameId}`);
    return response.data;
  }
  
  /**
   * 주사위를 굴립니다.
   */
  static async rollDice(gameId: number, request: RollDiceRequest): Promise<DiceRollResponse> {
    const response = await api.post<DiceRollResponse>(
      `/api/yacht/games/${gameId}/roll`,
      request
    );
    return response.data;
  }
  
  /**
   * 점수를 기록합니다.
   */
  static async recordScore(gameId: number, request: RecordScoreRequest): Promise<ScoreCardResponse> {
    const response = await api.post<ScoreCardResponse>(
      `/api/yacht/games/${gameId}/score`,
      request
    );
    return response.data;
  }
  
  /**
   * 점수판을 조회합니다.
   */
  static async getScoreCard(gameId: number): Promise<ScoreCardResponse> {
    const response = await api.get<ScoreCardResponse>(`/api/yacht/games/${gameId}/scorecard`);
    return response.data;
  }
  
  /**
   * 점수 미리보기를 조회합니다.
   */
  static async previewScore(gameId: number, category: Category): Promise<number> {
    const response = await api.get<{ previewScore: number }>(
      `/api/yacht/games/${gameId}/preview/${category}`
    );
    return response.data.previewScore;
  }
}

export default GameService;
