package com.app.sudoku.domain.usecase

import com.app.sudoku.domain.common.Result
import com.app.sudoku.domain.model.SudokuPuzzle
import com.app.sudoku.domain.repository.SudokuRepository
import javax.inject.Inject

class SolveSudokuUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(board: List<List<Int?>>): Result<SudokuPuzzle> {
        return repository.solveSudoku(board)
    }
}
