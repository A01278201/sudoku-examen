package com.app.sudoku.domain.usecase

import javax.inject.Inject

/**
 * Caso de uso para verificar si la solución ingresada por el usuario
 * coincide con la solución correcta del Sudoku.
 *
 * La idea es que desde el ViewModel le pases:
 *  - solutionBoard: la matriz solución que vino del API
 *  - currentBoard: la matriz que el usuario ha ido llenando
 */
class VerifySolutionUseCase @Inject constructor() {

    operator fun invoke(
        solutionBoard: List<List<Int?>>,
        currentBoard: List<List<Int?>>
    ): Boolean {
        // Si no tienen el mismo tamaño, ya está mal
        if (solutionBoard.size != currentBoard.size) return false

        return solutionBoard.indices.all { row ->
            val solutionRow = solutionBoard[row]
            val currentRow = currentBoard[row]

            if (solutionRow.size != currentRow.size) return false

            solutionRow.indices.all { col ->
                solutionRow[col] == currentRow[col]
            }
        }
    }
}
