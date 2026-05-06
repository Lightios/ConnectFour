package ui

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import model.GameConfig
import org.jetbrains.compose.web.attributes.InputType

@Composable
fun ConfigScreen(onStart: (GameConfig) -> Unit) {
    var rows by remember { mutableStateOf(6) }
    var cols by remember { mutableStateOf(7) }
    var winCondition by remember { mutableStateOf(4) }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    fun validate(): Boolean {
        val newErrors = mutableMapOf<String, String>()

        if (rows < 4 || rows > 20) newErrors["rows"] = "Rows must be between 4 and 20"
        if (cols < 4 || cols > 20) newErrors["cols"] = "Columns must be between 4 and 20"
        if (winCondition < 3) newErrors["win"] = "Win condition must be at least 3"
        if (winCondition > 10) newErrors["win"] = "Win condition can be at most 10"
        if (winCondition > minOf(rows, cols)) {
            newErrors["win"] = "Win condition can't exceed board dimensions (${minOf(rows, cols)})"
        }

        errors = newErrors
        return newErrors.isEmpty()
    }

    Div({ classes(AppStyleSheet.screen) }) {
        // ── Title ────────────────────────────────────────────────────
        Div {
            H1({ classes(AppStyleSheet.title) }) { Text("Connect Four") }
            P({ classes(AppStyleSheet.subtitle) }) { Text("Configure your game") }
        }

        // ── Config card ──────────────────────────────────────────────
        Div({ classes(AppStyleSheet.card) }) {
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Center)
                    gap(24.px)
                }
            }) {
                Div({ classes(AppStyleSheet.configForm) }) {

                    // Rows input
                    InputGroup(
                        label = "Rows",
                        value = rows,
                        min = 4,
                        max = 20,
                        error = errors["rows"],
                        onChange = {
                            rows = it
                            errors = errors - "rows"
                        }
                    )

                    // Cols input
                    InputGroup(
                        label = "Columns",
                        value = cols,
                        min = 4,
                        max = 20,
                        error = errors["cols"],
                        onChange = {
                            cols = it
                            errors = errors - "cols"
                        }
                    )

                    // Win condition input
                    InputGroup(
                        label = "Win Condition (connect N)",
                        value = winCondition,
                        min = 3,
                        max = 10,
                        error = errors["win"],
                        onChange = {
                            winCondition = it
                            errors = errors - "win"
                        }
                    )
                }

                // ── Preview text ─────────────────────────────────────
                P({
                    classes(AppStyleSheet.subtitle)
                    style { fontSize(0.85.cssRem) }
                }) {
                    Text("${rows} × ${cols} board  •  Connect ${winCondition} to win")
                }

                // ── Start button ─────────────────────────────────────
                Button({
                    classes(AppStyleSheet.button)
                    style { width(100.percent) }
                    onClick {
                        if (validate()) {
                            onStart(GameConfig(rows = rows, cols = cols, winCondition = winCondition))
                        }
                    }
                }) {
                    Text("Start Game")
                }
            }
        }
    }
}

@Composable
private fun InputGroup(
    label: String,
    value: Int,
    min: Int,
    max: Int,
    error: String?,
    onChange: (Int) -> Unit
) {
    Div({ classes(AppStyleSheet.inputGroup) }) {
        Label {
            Span({ classes(AppStyleSheet.label) }) { Text(label) }
        }

        Div({
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                gap(8.px)
            }
        }) {
            // Decrement button
            Button({
                classes(AppStyleSheet.buttonOutline)
                style {
                    padding(8.px, 14.px)
                    fontSize(1.1.cssRem)
                }
                onClick { if (value > min) onChange(value - 1) }
            }) { Text("−") }

            // Number input
            Input(type = InputType.Number) {
                classes(AppStyleSheet.input)
                style { textAlign("center") }
                value(value.toString())
                attr("min", min.toString())
                attr("max", max.toString())
                onInput { event ->
                    val v = event.value?.toInt()
                    if (v != null) onChange(v.coerceIn(min, max))
                }
            }

            // Increment button
            Button({
                classes(AppStyleSheet.buttonOutline)
                style {
                    padding(8.px, 14.px)
                    fontSize(1.1.cssRem)
                }
                onClick { if (value < max) onChange(value + 1) }
            }) { Text("+") }
        }

        // Inline error message
        if (error != null) {
            Span({
                style {
                    fontSize(0.8.cssRem)
                    color(AppStyleSheet.Colors.textAccent)
                }
            }) { Text(error) }
        }
    }
}