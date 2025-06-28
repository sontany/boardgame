package com.boardgame.exception

class BoardGameNotFoundException(message: String) : RuntimeException(message)
class PlayerNotFoundException(message: String) : RuntimeException(message)
class GameSessionNotFoundException(message: String) : RuntimeException(message)
class ReviewNotFoundException(message: String) : RuntimeException(message)
class InvalidGameSessionStateException(message: String) : RuntimeException(message)
class DuplicateEmailException(message: String) : RuntimeException(message)
