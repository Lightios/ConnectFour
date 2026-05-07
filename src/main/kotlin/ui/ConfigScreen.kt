package ui

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import model.GameConfig
import org.jetbrains.compose.web.attributes.InputType

enum class Field {
    ROWS,
    COLS,
    WIN,
}

@Composable
fun ConfigScreen(
    onStart: (GameConfig) -> Unit,
    viewModel: ConfigViewModel,
){
    Div({ classes(AppStyleSheet.screen) }) {
        Div {
            H1({ classes(AppStyleSheet.title) }) { Text("Connect Four") }
            P({ classes(AppStyleSheet.subtitle) }) { Text("Configure your game") }
        }
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
                    InputGroup(
                        label = "Rows",
                        value = viewModel.rows,
                        error = viewModel.errors[Field.ROWS],
                        onChange = viewModel::onRowsChange
                    )
                    InputGroup(
                        label = "Columns",
                        value = viewModel.cols,
                        error = viewModel.errors[Field.COLS],
                        onChange = viewModel::onColsChange
                    )
                    InputGroup(
                        label = "Win Condition (connect N)",
                        value = viewModel.winCondition,
                        error = viewModel.errors[Field.WIN],
                        onChange = viewModel::onWinConditionChange
                    )
                }
                P({ classes(AppStyleSheet.subtitle) }) {
                    Text("${viewModel.rows} × ${viewModel.cols} board  •  Connect ${viewModel.winCondition} to win")
                }
                Button({
                    classes(AppStyleSheet.button)
                    onClick {
                        if (viewModel.validate()) onStart(viewModel.toConfig())
                    }
                }) { Text("Start Game") }
            }
        }
    }
}


@Composable
private fun InputGroup(
    label: String,
    value: Int,
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
            Button({
                classes(AppStyleSheet.buttonOutline)
                style {
                    padding(8.px, 14.px)
                    fontSize(1.1.cssRem)
                }
                onClick { onChange(value - 1) }
            }) { Text("−") }

            Input(type = InputType.Number) {
                classes(AppStyleSheet.input)
                style { textAlign("center") }
                value(value.toString())
                onInput { event ->
                    val v = event.value?.toInt()
                    if (v != null) onChange(v)
                }
            }

            Button({
                classes(AppStyleSheet.buttonOutline)
                style {
                    padding(8.px, 14.px)
                    fontSize(1.1.cssRem)
                }
                onClick { onChange(value + 1) }
            }) { Text("+") }
        }

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