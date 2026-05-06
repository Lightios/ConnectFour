import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

import game.GameController
import game.Screen
import org.jetbrains.compose.web.css.Style
import ui.AppStyleSheet
import ui.ConfigScreen
import ui.GameScreen

fun main() {
    val controller = GameController()

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