package ui

import androidx.compose.runtime.*
import model.GameConfig

class ConfigViewModel {
    var rows by mutableStateOf(10)
        private set
    var cols by mutableStateOf(10)
        private set
    var winCondition by mutableStateOf(4)
        private set
    var errors by mutableStateOf(mapOf<Field, String>())
        private set

    fun onRowsChange(value: Int) {
        rows = value
        errors = validateField(Field.ROWS, value, cols, winCondition)
    }

    fun onColsChange(value: Int) {
        cols = value
        errors = validateField(Field.COLS, rows, value, winCondition)
    }

    fun onWinConditionChange(value: Int) {
        winCondition = value
        errors = validateField(Field.WIN, rows, cols, value)
    }

    fun validate(): Boolean {
        val combined = Field.entries.fold(mapOf<Field, String>()) { acc, field ->
            acc + validateField(field, rows, cols, winCondition)
        }
        errors = combined
        return combined.isEmpty()
    }

    fun toConfig() = GameConfig(rows = rows, cols = cols, winCondition = winCondition)

    private fun validateField(field: Field, r: Int, c: Int, win: Int): Map<Field, String> {
        val current = errors.toMutableMap()
        val minDim = minOf(r, c)
        when (field) {
            Field.ROWS -> {
                if (r !in 4..20) current[Field.ROWS] = "Rows must be between 4 and 20"
                else current.remove(Field.ROWS)
                if (win > minDim) current[Field.WIN] = "Win condition can't exceed board dimensions ($minDim)"
                else if (win in 3..10) current.remove(Field.WIN)
            }
            Field.COLS -> {
                if (c !in 4..20) current[Field.COLS] = "Columns must be between 4 and 20"
                else current.remove(Field.COLS)
                if (win > minDim) current[Field.WIN] = "Win condition can't exceed board dimensions ($minDim)"
                else if (win in 3..10) current.remove(Field.WIN)
            }
            Field.WIN -> when {
                win < 3        -> current[Field.WIN] = "Win condition must be at least 3"
                win > 10       -> current[Field.WIN] = "Win condition can be at most 10"
                win > minDim   -> current[Field.WIN] = "Win condition can't exceed board dimensions ($minDim)"
                else           -> current.remove(Field.WIN)
            }
        }
        return current
    }
}