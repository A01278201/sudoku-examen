package com.app.sudoku.presentation.screens.sudoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sudoku.domain.common.Result
import com.app.sudoku.domain.model.*
import com.app.sudoku.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SudokuViewModel @Inject constructor(
    private val generateSudokuUseCase: GenerateSudokuUseCase,
    private val verifySolutionUseCase: VerifySolutionUseCase,
    private val saveGameUseCase: SaveGameUseCase,
    private val loadSavedGameUseCase: LoadSavedGameUseCase,
    private val clearSavedGameUseCase: ClearSavedGameUseCase,
    private val solveSudokuUseCase: SolveSudokuUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SudokuUiState())
    val uiState: StateFlow<SudokuUiState> = _uiState

    fun generateSudoku(size: SudokuBoardSize, difficulty: SudokuDifficulty) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }

            when (val result = generateSudokuUseCase(size, difficulty)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            puzzle = result.data,
                            currentBoard = result.data.puzzle.map { row -> row.toMutableList() },
                            boardSize = size,
                            difficulty = difficulty,
                            isOfflineMode = false
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No se pudo generar el Sudoku.\n${result.message}",
                            isOfflineMode = true
                        )
                    }
                }
            }
        }
    }

    fun updateCell(row: Int, col: Int, value: Int?) {
        _uiState.update { state ->
            val newBoard = state.currentBoard.mapIndexed { i, r ->
                if (i == row) r.mapIndexed { j, c -> if (j == col) value else c } else r
            }
            state.copy(currentBoard = newBoard)
        }
    }

    fun verifySolution() {
        val puzzle = _uiState.value.puzzle ?: return
        val current = _uiState.value.currentBoard
        val isCorrect = current == puzzle.solution
        _uiState.update {
            it.copy(
                successMessage = if (isCorrect)
                    "¡Solución correcta!"
                else
                    "La solución no es correcta, sigue intentando."
            )
        }
    }

    fun requestSolutionFromApi() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, successMessage = null, error = null) }

            when (val result = solveSudokuUseCase(_uiState.value.currentBoard)) {
                is Result.Success -> {
                    val solved = result.data.puzzle
                    val current = _uiState.value.currentBoard
                    val isCorrect = solved == current

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = if (isCorrect)
                                "Tu solución es correcta (verificada con la API)"
                            else
                                "Tu solución no coincide con la respuesta de la API"
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al verificar con la API: ${result.message}"
                        )
                    }
                }
            }
        }
    }

    fun resetBoard() {
        val puzzle = _uiState.value.puzzle ?: return
        _uiState.update {
            it.copy(currentBoard = puzzle.puzzle.map { row -> row.toMutableList() })
        }
    }

    fun saveGame() {
        viewModelScope.launch {
            val state = _uiState.value
            val puzzle = state.puzzle ?: return@launch
            val game =
                SudokuSavedGame(
                    puzzle = puzzle.puzzle,
                    solution = puzzle.solution,
                    currentBoard = state.currentBoard,
                    boardSize = state.boardSize,
                    difficulty = state.difficulty
                )
            saveGameUseCase(game)
            _uiState.update { it.copy(successMessage = "Partida guardada correctamente") }
        }
    }

    fun loadSavedGame() {
        viewModelScope.launch {
            val saved = loadSavedGameUseCase()
            if (saved != null) {
                _uiState.update {
                    it.copy(
                        puzzle = SudokuPuzzle(
                            saved.boardSize,
                            saved.difficulty,
                            saved.puzzle,
                            saved.solution
                        ),
                        currentBoard = saved.currentBoard,
                        boardSize = saved.boardSize,
                        difficulty = saved.difficulty,
                        isOfflineMode = true,
                        successMessage = "Partida cargada correctamente"
                    )
                }
            } else {
                _uiState.update { it.copy(error = "No hay partida guardada.") }
            }
        }
    }

    fun clearSavedGame() {
        viewModelScope.launch {
            clearSavedGameUseCase()
            _uiState.update { it.copy(successMessage = "🗑 Partida eliminada") }
        }
    }
}
