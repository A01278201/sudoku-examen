package com.app.sudoku.presentation.screens.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.sudoku.domain.model.SudokuBoardSize
import com.app.sudoku.domain.model.SudokuDifficulty

@Composable
fun SudokuScreen(viewModel: SudokuViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sudoku Examen",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DropdownSelector(
                    label = "Tamaño",
                    options = SudokuBoardSize.values().toList(),
                    selected = state.boardSize,
                    onSelect = { viewModel.generateSudoku(it, state.difficulty) }
                )
                DropdownSelector(
                    label = "Dificultad",
                    options = SudokuDifficulty.values().toList(),
                    selected = state.difficulty,
                    onSelect = { viewModel.generateSudoku(state.boardSize, it) }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Zona central
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }

                    state.error != null -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = {
                                viewModel.generateSudoku(state.boardSize, state.difficulty)
                            }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    state.puzzle != null -> {
                        SudokuBoard(
                            state = state,
                            onCellChange = { r, c, v -> viewModel.updateCell(r, c, v) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.verifySolution() },
                    enabled = state.puzzle != null
                ) {
                    Text("Verificar")
                }
                OutlinedButton(
                    onClick = { viewModel.resetBoard() },
                    enabled = state.puzzle != null
                ) {
                    Text("Reiniciar")
                }
                Button(
                    onClick = { viewModel.saveGame() },
                    enabled = state.puzzle != null
                ) {
                    Text("Guardar")
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.loadSavedGame() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cargar partida")
            }

            Spacer(Modifier.height(8.dp))

            // Mensajes
            state.successMessage?.let {
                Text(
                    text = it,
                    color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (state.isOfflineMode) {
                Text(
                    text = "⚠ Modo sin conexión (datos simulados)",
                    color = Color(0xFFFFC107),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}



@Composable
private fun SudokuBoard(
    state: SudokuUiState,
    onCellChange: (Int, Int, Int?) -> Unit
) {
    val puzzle = state.puzzle ?: return
    val current = state.currentBoard
    val n = puzzle.boardSize.totalSize

    val cellSize: Dp = if (n <= 4) 64.dp else 32.dp
    val spacing: Dp = if (n <= 4) 6.dp else 2.dp

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .wrapContentWidth()
        ) {
            for (rowIndex in 0 until n) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(vertical = 1.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (colIndex in 0 until n) {
                        val initialValue = puzzle.puzzle[rowIndex][colIndex]
                        val editable = initialValue == null
                        val value = current[rowIndex][colIndex]

                        val cellBackground =
                            if (editable) Color(0xFF1F2937) else Color(0xFF312E81)

                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF4B5563),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .background(cellBackground, RoundedCornerShape(4.dp))
                                .clickable(enabled = editable) {
                                    if (editable) {
                                        val nextValue =
                                            ((value ?: 0) + 1).let { next ->
                                                if (next > n) null else next
                                            }
                                        onCellChange(rowIndex, colIndex, nextValue)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = value?.toString() ?: "",
                                color = Color.White,
                                fontWeight = if (editable) FontWeight.Normal else FontWeight.Bold
                            )
                        }

                        if (colIndex < n - 1) {
                            Spacer(modifier = Modifier.width(spacing))
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(onClick = { expanded = true }) {
        Text("$label: $selected")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { opt ->
            DropdownMenuItem(
                text = { Text(opt.toString()) },
                onClick = {
                    expanded = false
                    onSelect(opt)
                }
            )
        }
    }
}
