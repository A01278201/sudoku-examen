package com.app.sudoku.data.remote.api

import com.app.sudoku.data.remote.dto.SudokuDto
import retrofit2.http.*

interface SudokuApi {

    @GET("sudokugenerate")
    suspend fun generateSudoku(
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("difficulty") difficulty: String
    ): SudokuDto


    @POST("sudokusolve")
    suspend fun solveSudoku(
        @Body request: SudokuSolveRequest
    ): SudokuDto
}

data class SudokuSolveRequest(
    val puzzle: List<List<Int?>>
)
