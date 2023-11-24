package com.mehmedmert.minesweeperkmmshowcase.repository

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs
import app.cash.turbine.test
import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus
import kotlin.test.assertContains
import kotlin.test.assertEquals

class GameRepositoryTest {
    @Test
    fun `test game is initially running`() = runBlocking {
        val tested = createTestGame()
        tested.gameStatus.test {
            assertIs<GameStatus.Running>(awaitItem())
        }
    }

    @Test
    fun `test game over when hitting a mine`() = runBlocking {
        val tested = createTestGame()
        tested.openCell(0, 0)
        tested.gameStatus.test {
            assertIs<GameStatus.Lost>(awaitItem())
        }
    }

    @Test
    fun `test game running when opening a cell`() = runBlocking {
        val tested = createTestGame()
        tested.openCell(0, 1)
        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Running>(gameStatus)
            assertEquals(1, gameStatus.remainingMines)
            assertContains(gameStatus.openedCells, Pair(0, 1))
            assertEquals(1, gameStatus.openedCells[Pair(0, 1)])
        }
    }

    /**
     * _______
     * |*|1|0|
     * |1|1|0|
     * |0|0|0|
     * -------
     */
    @Test
    fun `test open all cells but the mine with single cell click`() = runBlocking {
        val tested = createTestGame()
        tested.openCell(2, 2)
        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Running>(gameStatus)
            assertContains(gameStatus.openedCells, Pair(0, 1))
            assertContains(gameStatus.openedCells, Pair(0, 2))
            assertContains(gameStatus.openedCells, Pair(1, 0))
            assertContains(gameStatus.openedCells, Pair(1, 1))
            assertContains(gameStatus.openedCells, Pair(1, 2))
            assertContains(gameStatus.openedCells, Pair(2, 0))
            assertContains(gameStatus.openedCells, Pair(2, 1))
            assertContains(gameStatus.openedCells, Pair(2, 2))
            assertEquals(1, gameStatus.openedCells[Pair(0, 1)])
            assertEquals(1, gameStatus.openedCells[Pair(1, 0)])
            assertEquals(1, gameStatus.openedCells[Pair(1, 1)])
            assertEquals(0, gameStatus.openedCells[Pair(0, 2)])
            assertEquals(0, gameStatus.openedCells[Pair(1, 2)])
            assertEquals(0, gameStatus.openedCells[Pair(2, 0)])
            assertEquals(0, gameStatus.openedCells[Pair(2, 1)])
            assertEquals(0, gameStatus.openedCells[Pair(2, 2)])
        }
    }

    @Test
    fun `test winning game`() = runBlocking {
        val tested = createTestGame()
        tested.openCell(2, 2)
        tested.toggleFlagCell(0, 0)
        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Won>(gameStatus)
        }
    }

    @Test
    fun `test default new game`() = runBlocking {
        val tested = GameRepositoryImpl()
        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Running>(gameStatus)
            assertEquals(30, gameStatus.rows)
            assertEquals(30, gameStatus.columns)
        }
    }

    @Test
    fun `test open invalid cell has no effect`() = runBlocking {
        val tested = createTestGame()
        tested.openCell(-1, -1)
        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Running>(gameStatus)
        }
    }

    @Test
    fun `test open cell when game already lost has no effect`() = runBlocking {
        val tested = createTestGame()
        tested.openCell(0, 0)
        tested.openCell(1, 1)
        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Lost>(gameStatus)
            assertEquals(0, gameStatus.openedCells.size)
        }
    }

    @Test
    fun `test max neighbour mines`() = runBlocking {
        val tested = GameRepositoryImpl().apply {
            newGame(
                rows = 3,
                columns = 3,
                mines = listOf(
                    Pair(0, 0),
                    Pair(0, 1),
                    Pair(0, 2),
                    Pair(1, 0),
                    Pair(1, 2),
                    Pair(2, 0),
                    Pair(2, 1),
                    Pair(2, 2)
                )
            )
        }
        tested.openCell(1, 1)
        tested.toggleFlagCell(0, 0)
        tested.toggleFlagCell(0, 1)
        tested.toggleFlagCell(0, 2)
        tested.toggleFlagCell(1, 0)
        tested.toggleFlagCell(1, 2)
        tested.toggleFlagCell(2, 0)
        tested.toggleFlagCell(2, 1)
        tested.toggleFlagCell(2, 1)
        tested.toggleFlagCell(2, 1)
        tested.toggleFlagCell(2, 1)

        tested.gameStatus.test {
            val gameStatus = awaitItem()
            assertIs<GameStatus.Running>(gameStatus)
            assertEquals(8, gameStatus.openedCells[Pair(1, 1)])

            tested.toggleFlagCell(2, 1)
            assertIs<GameStatus.Running>(awaitItem())

            tested.toggleFlagCell(2, 2)
            assertIs<GameStatus.Won>(awaitItem())
        }
    }

    private fun createTestGame() = GameRepositoryImpl().apply {
        newGame(
            rows = 3,
            columns = 3,
            mines = listOf(Pair(0, 0))
        )
    }
}
