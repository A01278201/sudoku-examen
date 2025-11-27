package com.app.sudoku.domain.usecase

import com.app.sudoku.domain.common.Result
import com.app.sudoku.domain.model.SudokuBoardSize
import com.app.sudoku.domain.model.SudokuDifficulty
import com.app.sudoku.domain.model.SudokuPuzzle
import com.app.sudoku.domain.repository.SudokuRepository
import javax.inject.Inject

class GenerateSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(
        boardSize: SudokuBoardSize,
        difficulty: SudokuDifficulty
    ): Result<SudokuPuzzle> = repository.generateSudoku(boardSize, difficulty)
}
