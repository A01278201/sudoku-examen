package com.app.sudoku.domain.model

enum class SudokuDifficulty(val apiValue: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard")
}

enum class SudokuBoardSize(
    val boxWidth: Int,
    val boxHeight: Int,
    val displayName: String
) {
    BOARD_4x4(boxWidth = 2, boxHeight = 2, displayName = "4x4"),
    BOARD_9x9(boxWidth = 3, boxHeight = 3, displayName = "9x9");

    val totalSize: Int get() = boxWidth * boxHeight
}

data class SudokuPuzzle(
    val boardSize: SudokuBoardSize,
    val difficulty: SudokuDifficulty,
    val puzzle: List<List<Int?>>,   // null = celda vacía
    val solution: List<List<Int>>
)

data class SudokuSavedGame(
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int>>,
    val currentBoard: List<List<Int?>>,
    val boardSize: SudokuBoardSize,
    val difficulty: SudokuDifficulty
)
