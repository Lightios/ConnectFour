package game

import androidx.compose.runtime.*
import model.*
import storage.GameStorage

class GameController(
    private val gameStorage: GameStorage,
) {

    var state by mutableStateOf(
        gameStorage.load() ?: GameEngine.initialState(GameConfig())
    )
        private set

    var screen by mutableStateOf(
        if (gameStorage.load() != null)
            Screen.Game
        else
            Screen.Config
    )
        private set

    fun startGame(config: GameConfig) {
        state = GameEngine.initialState(config)
        gameStorage.save(state)
        screen = Screen.Game
    }

    fun dropPiece(col: Int) {
        if (state.status != GameStatus.Playing) return
        state = GameEngine.dropPiece(state, col)
        gameStorage.save(state)
    }

    fun resetGame() {
        state = GameEngine.initialState(state.config)
        gameStorage.clear()
        screen = Screen.Game
    }

    fun goToConfig() {
        gameStorage.clear()
        screen = Screen.Config
    }
}

sealed class Screen {
    object Config : Screen()
    object Game : Screen()
}