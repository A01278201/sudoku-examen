package com.app.sudoku.data.remote.dto

data class SudokuDto(
    val puzzle: List<List<Int?>>,
    val solution: List<List<Int>>
)
