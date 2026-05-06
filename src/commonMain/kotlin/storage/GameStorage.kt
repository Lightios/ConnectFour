package storage

import model.GameState

interface GameStorage {
    fun save(state: GameState)
    fun load(): GameState?
    fun clear()
}