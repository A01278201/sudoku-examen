package com.app.sudoku.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.sudoku.presentation.screens.sudoku.SudokuScreen

object Routes {
    const val SUDOKU = "sudoku"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SUDOKU
    ) {
        composable(route = Routes.SUDOKU) {
            SudokuScreen()
        }
    }
}
