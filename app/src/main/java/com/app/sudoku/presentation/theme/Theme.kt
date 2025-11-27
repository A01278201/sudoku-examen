package com.app.sudoku.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
        primary = SudokuPrimary,
        onPrimary = Color.White,

        secondary = SudokuAccent,
        onSecondary = Color.Black,

        background = SudokuBackgroundDark,
        onBackground = SudokuOnDark,

        surface = SudokuSurfaceDark,
        onSurface = SudokuOnDark,
        surfaceVariant = SudokuSurfaceVariant,
        outline = SudokuOutline,

        error = SudokuError,
        onError = Color.White,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = SudokuPrimary,
        onPrimary = Color.White,

        secondary = SudokuAccent,
        onSecondary = Color.Black,

        background = Color(0xFFF4F4F5),
        onBackground = Color(0xFF020617),

        surface = Color.White,
        onSurface = Color(0xFF020617),
        surfaceVariant = Color(0xFFE5E7EB),
        outline = SudokuOutline,

        error = SudokuError,
        onError = Color.White,
    )

@Composable
fun SudokuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
