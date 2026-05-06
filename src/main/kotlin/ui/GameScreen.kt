package ui

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import model.*

@Composable
fun GameScreen(
    state: GameState,
    onColumnClick: (Int) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    Div({ classes(AppStyleSheet.screen) }) {

        // ── Header ───────────────────────────────────────────────────
        Div({
            style {
                display(DisplayStyle.Flex)
                justifyContent(JustifyContent.SpaceBetween)
                alignItems(AlignItems.Center)
                width(100.percent)
            }
        }) {
            Button({
                classes(AppStyleSheet.buttonOutline)
                onClick { onBack() }
            }) {
                Text("← Config")
            }

            H1({ classes(AppStyleSheet.title) }) { Text("Connect Four") }

            Button({
                classes(AppStyleSheet.buttonOutline)
                onClick { onReset() }
            }) {
                Text("Restart")
            }
        }

        // ── Status bar ───────────────────────────────────────────────
        StatusBar(state)

        // ── Board ────────────────────────────────────────────────────
        Board(
            state = state,
            onColumnClick = onColumnClick
        )

        // ── Game over overlay ────────────────────────────────────────
        if (state.status != GameStatus.Playing) {
            GameOverBanner(
                status = state.status,
                onReset = onReset,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun StatusBar(state: GameState) {
    Div({ classes(AppStyleSheet.statusBar) }) {
        when (val status = state.status) {
            is GameStatus.Playing -> {
                PlayerChip(state.currentPlayer)

                Span({ classes(AppStyleSheet.statusMessage) }) {
                    Text("Player ${playerLabel(state.currentPlayer)}'s turn")
                }

                // spacer to balance the layout
                Div({ style { width(80.px) } })
            }

            is GameStatus.Won -> {
                Span({
                    classes(AppStyleSheet.statusMessage)
                    style {
                        color(playerColor(status.player))
                    }
                }) {
                    Text("Player ${playerLabel(status.player)} wins! 🎉")
                }
            }

            is GameStatus.Draw -> {
                Span({ classes(AppStyleSheet.statusMessage) }) {
                    Text("It's a draw!")
                }
            }
        }
    }
}

@Composable
private fun PlayerChip(player: Player) {
    Div({ classes(AppStyleSheet.playerIndicator) }) {
        Div({
            classes(AppStyleSheet.playerDot)
            style { backgroundColor(playerColor(player)) }
        })
        Span { Text("Player ${playerLabel(player)}") }
    }
}

@Composable
private fun GameOverBanner(
    status: GameStatus,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    // full-screen dimmed overlay
    Div({
        style {
            position(Position.Fixed)
            top(0.px)
            left(0.px)
            width(100.percent)
            height(100.percent)
            backgroundColor(AppStyleSheet.Colors.overlay)
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            alignItems(AlignItems.Center)
            property("z-index", "100")
            property("animation", "fadeIn 0.3s ease")
        }
        // clicking the overlay dismisses it and resets
        onClick { onReset() }
    }) {
        // modal card — stop click propagation so clicks inside don't reset
        Div({
            classes(AppStyleSheet.card)
            style {
                property("max-width", "360px")
                textAlign("center")
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
                gap(20.px)
            }
            onClick { it.stopPropagation() }
        }) {
            when (status) {
                is GameStatus.Won -> {
                    Div({
                        style {
                            width(56.px)
                            height(56.px)
                            borderRadius(50.percent)
                            backgroundColor(playerColor(status.player))
                            property("box-shadow", "0 0 24px ${playerGlow(status.player)}")
                        }
                    })
                    H2({ classes(AppStyleSheet.title) }) {
                        Text("Player ${playerLabel(status.player)} Wins!")
                    }
                    P({ classes(AppStyleSheet.subtitle) }) {
                        Text("Connected ${(status as? GameStatus.Won)?.winningCells?.size ?: 4} in a row")
                    }
                }

                is GameStatus.Draw -> {
                    H2({ classes(AppStyleSheet.title) }) { Text("It's a Draw!") }
                    P({ classes(AppStyleSheet.subtitle) }) {
                        Text("No more moves available")
                    }
                }

                else -> {}
            }

            Div({
                style {
                    display(DisplayStyle.Flex)
                    gap(12.px)
                    width(100.percent)
                }
            }) {
                Button({
                    classes(AppStyleSheet.buttonOutline)
                    style { property("flex", "1") }
                    onClick { onBack() }
                }) {
                    Text("New Config")
                }

                Button({
                    classes(AppStyleSheet.button)
                    style { property("flex", "1") }
                    onClick { onReset() }
                }) {
                    Text("Play Again")
                }
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────

private fun playerLabel(player: Player): String = when (player) {
    Player.RED -> "Red"
    Player.YELLOW -> "Yellow"
}

private fun playerColor(player: Player): CSSColorValue = when (player) {
    Player.RED -> AppStyleSheet.Colors.red
    Player.YELLOW -> AppStyleSheet.Colors.yellow
}

private fun playerGlow(player: Player): String = when (player) {
    Player.RED -> "rgba(233,69,96,0.6)"
    Player.YELLOW -> "rgba(245,166,35,0.6)"
}