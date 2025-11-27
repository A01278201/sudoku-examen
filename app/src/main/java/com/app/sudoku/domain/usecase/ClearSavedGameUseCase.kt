package com.app.sudoku.domain.usecase

import com.app.sudoku.domain.repository.SudokuRepository
import javax.inject.Inject

class ClearSavedGameUseCase @Inject constructor(
    private val repository: SudokuRepository
) {
    suspend operator fun invoke() {
        repository.clearSavedGame()
    }
}
