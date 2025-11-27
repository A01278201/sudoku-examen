package com.app.sudoku.data.repository

import com.app.sudoku.data.local.SudokuLocalDataSource
import com.app.sudoku.data.mapper.toDomain
import com.app.sudoku.data.remote.api.SudokuApi
import com.app.sudoku.data.remote.api.SudokuSolveRequest
import com.app.sudoku.domain.common.Result
import com.app.sudoku.domain.model.*
import com.app.sudoku.domain.repository.SudokuRepository
import java.io.IOException
import javax.inject.Inject

class SudokuRepositoryImpl @Inject constructor(
    private val api: SudokuApi,
    private val local: SudokuLocalDataSource
) : SudokuRepository {

    override suspend fun generateSudoku(
        boardSize: SudokuBoardSize,
        difficulty: SudokuDifficulty
    ): Result<SudokuPuzzle> {
        return try {
            // 👈 La API espera el tamaño de los bloques, no del tablero completo.
            // Para 4x4 => width = 2, height = 2
            // Para 9x9 => width = 3, height = 3
            val dto = api.generateSudoku(
                width = boardSize.boxWidth,
                height = boardSize.boxHeight,
                difficulty = difficulty.apiValue
            )

            val puzzle = dto.toDomain(boardSize, difficulty)
            Result.Success(puzzle)

        } catch (e: IOException) {
            // Error típico de red -> sin conexión
            Result.Error("No se pudo conectar al servidor. Revisa tu conexión e inténtalo de nuevo.")
        } catch (e: Exception) {
            // Errores HTTP (502, 500, etc.) u otros
            Result.Error("Ocurrió un error al generar el Sudoku: ${e.message ?: "desconocido"}")
        }
    }

    override suspend fun solveSudoku(board: List<List<Int?>>): Result<SudokuPuzzle> {
        return try {
            val request = SudokuSolveRequest(puzzle = board)
            val response = api.solveSudoku(request)

            // Inferimos el tamaño del tablero a partir de la respuesta
            val n = response.puzzle.size
            val size = when (n) {
                4 -> SudokuBoardSize.BOARD_4x4
                9 -> SudokuBoardSize.BOARD_9x9
                else -> SudokuBoardSize.BOARD_4x4
            }

            // Dificultad “dummy” solo para completar el modelo (no afecta validación)
            val difficulty = SudokuDifficulty.EASY

            val puzzle = response.toDomain(size, difficulty)
            Result.Success(puzzle)
        } catch (e: IOException) {
            Result.Error("No se pudo conectar al servidor al resolver el Sudoku.")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al resolver el Sudoku")
        }
    }

    override suspend fun saveGame(game: SudokuSavedGame) {
        local.saveGame(game)
    }

    override suspend fun loadSavedGame(): SudokuSavedGame? =
        local.loadGame()

    override suspend fun clearSavedGame() {
        local.clearGame()
    }
}
