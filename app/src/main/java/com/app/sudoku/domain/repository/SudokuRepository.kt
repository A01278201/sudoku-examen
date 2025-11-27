package com.app.sudoku.domain.repository

import com.app.sudoku.domain.common.Result
import com.app.sudoku.domain.model.*

interface SudokuRepository {

    suspend fun generateSudoku(
        boardSize: SudokuBoardSize,
        difficulty: SudokuDifficulty
    ): Result<SudokuPuzzle>

    suspend fun solveSudoku(board: List<List<Int?>>): Result<SudokuPuzzle>


    suspend fun saveGame(game: SudokuSavedGame)

    suspend fun loadSavedGame(): SudokuSavedGame?

    suspend fun clearSavedGame()
}
