import org.jetbrains.compose.web.renderComposable

import game.GameController
import game.Screen
import org.jetbrains.compose.web.css.Style
import storage.GameStorageImpl
import ui.AppStyleSheet
import ui.ConfigScreen
import ui.ConfigViewModel
import ui.GameScreen

fun main() {
    val gameStorage = GameStorageImpl()
    val configViewModel = ConfigViewModel()
    val controller = GameController(gameStorage)

    renderComposable(rootElementId = "root") {
        Style(AppStyleSheet)

        when (controller.screen) {
            is Screen.Config -> ConfigScreen(
                onStart = { config -> controller.startGame(config) },
                viewModel = configViewModel,
            )
            is Screen.Game -> GameScreen(
                state = controller.state,
                onColumnClick = { col -> controller.dropPiece(col) },
                onReset = { controller.resetGame() },
                onBack = { controller.goToConfig() }
            )
        }
    }
}