package com.app.sudoku.data.local

import android.content.SharedPreferences
import com.app.sudoku.domain.model.SudokuBoardSize
import com.app.sudoku.domain.model.SudokuDifficulty
import com.app.sudoku.domain.model.SudokuSavedGame
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SudokuLocalDataSource(
    private val prefs: SharedPreferences,
    private val gson: Gson
) {

    private val KEY_GAME = "sudoku_saved_game_v1"

    fun saveGame(game: SudokuSavedGame) {
        val json = gson.toJson(game)
        prefs.edit().putString(KEY_GAME, json).apply()
    }

    fun loadGame(): SudokuSavedGame? {
        val json = prefs.getString(KEY_GAME, null) ?: return null
        return try {
            val type = object : TypeToken<SudokuSavedGame>() {}.type
            gson.fromJson<SudokuSavedGame>(json, type)
        } catch (_: Exception) {
            null
        }
    }

    fun clearGame() {
        prefs.edit().remove(KEY_GAME).apply()
    }
}
