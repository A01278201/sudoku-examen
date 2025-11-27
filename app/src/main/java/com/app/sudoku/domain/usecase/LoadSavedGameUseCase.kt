package com.app.sudoku.domain.usecase

import com.app.sudoku.domain.model.SudokuSavedGame
import com.app.sudoku.domain.repository.SudokuRepository
import javax.inject.Inject

class LoadSavedGameUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke(): SudokuSavedGame? =
        repository.loadSavedGame()
}
