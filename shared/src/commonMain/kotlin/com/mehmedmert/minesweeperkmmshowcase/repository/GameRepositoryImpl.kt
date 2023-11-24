package com.mehmedmert.minesweeperkmmshowcase.repository

import com.mehmedmert.minesweeperkmmshowcase.domain.GameRepository
import com.mehmedmert.minesweeperkmmshowcase.domain.GameStatus
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlin.random.Random

internal class GameRepositoryImpl : GameRepository {
    private val _gameStatus = MutableSharedFlow<GameStatus>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var mines: List<Pair<Int, Int>> = emptyList()

    init {
        newGame()
    }

    override val gameStatus: Flow<GameStatus> = _gameStatus

    override suspend fun openCell(row: Int, column: Int) {
        val cell = Pair(row, column)
        val lastStatus = _gameStatus.first()
        if (lastStatus !is GameStatus.Running) {
            return
        }

        if (mines.contains(cell)) {
            _gameStatus.tryEmit(
                GameStatus.Lost(
                    rows = lastStatus.rows,
                    columns = lastStatus.columns,
                    openedCells = lastStatus.openedCells,
                    flags = lastStatus.flags,
                    mines = mines
                )
            )
        } else {
            val gameStatus = openCellRecursively(cell, lastStatus)
            checkGameWonAndEmit(gameStatus)
        }
    }

    private fun openCellRecursively(cell: Pair<Int, Int>, gameStatus: GameStatus.Running): GameStatus.Running {
        if (cell.first < 0 ||
            cell.first >= gameStatus.rows ||
            cell.second < 0 ||
            cell.second >= gameStatus.columns) {
            return gameStatus
        }

        if (gameStatus.openedCells.contains(cell) || gameStatus.flags.contains(cell)) {
            return gameStatus
        }

        val minesAround = calculateMinesAround(cell)
        val openedCells = HashMap(gameStatus.openedCells)
        openedCells[cell] = minesAround
        var newGameStatus = gameStatus.copy(openedCells = openedCells)

        return if (minesAround == 0) {
            newGameStatus = openCellRecursively(cell.topLeft(), newGameStatus)
            newGameStatus = openCellRecursively(cell.top(), newGameStatus)
            newGameStatus = openCellRecursively(cell.topRight(), newGameStatus)
            newGameStatus = openCellRecursively(cell.right(), newGameStatus)
            newGameStatus = openCellRecursively(cell.bottomRight(), newGameStatus)
            newGameStatus = openCellRecursively(cell.bottom(), newGameStatus)
            newGameStatus = openCellRecursively(cell.bottomLeft(), newGameStatus)
            openCellRecursively(cell.left(), newGameStatus)
        } else {
            newGameStatus
        }
    }

    private fun calculateMinesAround(cell: Pair<Int, Int>): Int {
        return listOf(
            mines.contains(cell.topLeft()),
            mines.contains(cell.top()),
            mines.contains(cell.topRight()),
            mines.contains(cell.right()),
            mines.contains(cell.bottomRight()),
            mines.contains(cell.bottom()),
            mines.contains(cell.bottomLeft()),
            mines.contains(cell.left())
        ).count { it }
    }

    private fun Pair<Int, Int>.topLeft() = Pair(first - 1, second - 1)
    private fun Pair<Int, Int>.top() = Pair(first - 1, second)
    private fun Pair<Int, Int>.topRight() = Pair(first - 1, second + 1)
    private fun Pair<Int, Int>.right() = Pair(first, second + 1)
    private fun Pair<Int, Int>.bottomRight() = Pair(first + 1, second + 1)
    private fun Pair<Int, Int>.bottom() = Pair(first + 1, second)
    private fun Pair<Int, Int>.bottomLeft() = Pair(first + 1, second - 1)
    private fun Pair<Int, Int>.left() = Pair(first, second - 1)

    override fun newGame(rows: Int, columns: Int, minesCount: Int) {
        newGame(rows, columns, generateMines(rows, columns, minesCount))
    }

    internal fun newGame(
        rows: Int,
        columns: Int,
        mines: List<Pair<Int, Int>>,
    ) {
        this.mines = mines
        _gameStatus.tryEmit(
            GameStatus.Running(
                startTime = Clock.System.now(),
                rows = rows,
                columns = columns,
                openedCells = mapOf(),
                flags = setOf(),
                remainingMines = mines.size
            )
        )
    }

    private fun generateMines(rows: Int, columns: Int, minesCount: Int): List<Pair<Int, Int>> {
        val mines = mutableListOf<Pair<Int, Int>>()
        val randomRow = Random(rows - 1)
        val randomColumn = Random(columns - 1)
        var index = 0
        while (index < minesCount) {
            val row = randomRow.nextInt()
            val column = randomColumn.nextInt()
            val mine = Pair(row, column)
            if (mines.contains(mine).not()) {
                mines.add(mine)
                index++
            }
        }
        return mines
    }

    override suspend fun toggleFlagCell(row: Int, column: Int) {
        val gameStatus = _gameStatus.first()
        if (gameStatus !is GameStatus.Running) {
            return
        }

        val cell = Pair(row, column)
        if (gameStatus.openedCells.keys.contains(cell)) {
            return
        }

        val newStatus = gameStatus.copy(
            flags = if (gameStatus.flags.contains(cell)) {
                gameStatus.flags.subtract(setOf(cell)).toSet()
            } else {
                gameStatus.flags.plusElement(cell).toSet()
            },
            remainingMines = if (gameStatus.flags.contains(cell)) {
                gameStatus.remainingMines + 1
            } else {
                gameStatus.remainingMines - 1
            }
        )

        checkGameWonAndEmit(newStatus)
    }

    private fun checkGameWonAndEmit(newStatus: GameStatus.Running) {
        val isGameWon = newStatus.remainingMines == 0 &&
                mines.size == newStatus.flags.size &&
                newStatus.openedCells.size + mines.size == newStatus.rows * newStatus.columns

        _gameStatus.tryEmit(
            if (isGameWon) {
                GameStatus.Won(
                    rows = newStatus.rows,
                    columns = newStatus.columns,
                    openedCells = newStatus.openedCells,
                    flags = newStatus.flags
                )
            } else {
                newStatus
            }
        )
    }
}
