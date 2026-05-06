import org.jetbrains.compose.web.renderComposable

import game.GameViewModel
import game.Screen
import org.jetbrains.compose.web.css.Style
import storage.GameStorageImpl
import ui.AppStyleSheet
import ui.ConfigScreen
import ui.GameScreen

fun main() {
    val gameStorage = GameStorageImpl()
    val controller = GameViewModel(gameStorage)

    renderComposable(rootElementId = "root") {
        Style(AppStyleSheet)

        when (controller.screen) {
            is Screen.Config -> ConfigScreen(
                onStart = { config -> controller.startGame(config) }
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