package model

enum class Player {
    RED, YELLOW;

    fun other(): Player = if (this == RED) YELLOW else RED
}