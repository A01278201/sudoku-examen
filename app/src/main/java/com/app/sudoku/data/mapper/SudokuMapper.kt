package com.app.sudoku.data.mapper

import com.app.sudoku.data.remote.dto.SudokuDto
import com.app.sudoku.domain.model.*

fun SudokuDto.toDomain(
    size: SudokuBoardSize,
    difficulty: SudokuDifficulty
): SudokuPuzzle =
    SudokuPuzzle(
        boardSize = size,
        difficulty = difficulty,
        puzzle = puzzle,
        solution = solution
    )
