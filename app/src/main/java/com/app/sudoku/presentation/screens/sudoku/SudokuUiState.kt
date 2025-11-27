package com.app.sudoku.presentation.screens.sudoku

import com.app.sudoku.domain.model.*

data class SudokuUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val puzzle: SudokuPuzzle? = null,
    val currentBoard: List<List<Int?>> = emptyList(),
    val boardSize: SudokuBoardSize = SudokuBoardSize.BOARD_4x4,
    val difficulty: SudokuDifficulty = SudokuDifficulty.EASY,
    val isOfflineMode: Boolean = false,
    val successMessage: String? = null
)
