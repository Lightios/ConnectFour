package game

import androidx.compose.runtime.*
import model.*

class GameController {

    var state by mutableStateOf(GameState(
        board = GameEngine.createBoard(GameConfig()),
        currentPlayer = Player.RED,
        status = GameStatus.Playing,
        config = GameConfig()
    ))
        private set

    var screen by mutableStateOf<Screen>(Screen.Config)
        private set

    fun startGame(config: GameConfig) {
        state = GameEngine.initialState(config)
        screen = Screen.Game
    }

    fun dropPiece(col: Int) {
        if (state.status != GameStatus.Playing) return
        state = GameEngine.dropPiece(state, col)
    }

    fun resetGame() {
        state = GameEngine.initialState(state.config)
    }

    fun goToConfig() {
        screen = Screen.Config
    }
}

sealed class Screen {
    object Config : Screen()
    object Game : Screen()
}