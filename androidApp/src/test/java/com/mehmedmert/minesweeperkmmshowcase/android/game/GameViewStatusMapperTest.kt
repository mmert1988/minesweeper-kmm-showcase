package com.mehmedmert.minesweeperkmmshowcase.android.game

import com.mehmedmert.minesweeperkmmshowcase.domain.model.GameStatus
import kotlinx.datetime.Instant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GameViewStatusMapperTest {
    @Test
    fun `test map running game`() {
        val tested = GameViewStatusMapper()

        val result = tested.map(
            GameStatus.Running(
                rows = 3,
                columns = 3,
                openedCells = mapOf(
                    Pair(0, 0) to 1
                ),
                flags = setOf(Pair(1, 1)),
                startTime = Instant.DISTANT_PAST,
                remainingMines = 1
            )
        )

        assertEquals(3, result.rows)
        assertEquals(3, result.columns)
        assertEquals(
            listOf(
                Cell.Open(1),
                Cell.Closed,
                Cell.Closed,
                Cell.Closed,
                Cell.Flagged,
                Cell.Closed,
                Cell.Closed,
                Cell.Closed,
                Cell.Closed
            ),
            result.cells
        )
        assertEquals(Status.Running, result.status)
        assertEquals(1, result.remainingMines)
    }

    @Test
    fun `test map lost game`() {
        val tested = GameViewStatusMapper()

        val result = tested.map(
            GameStatus.Lost(
                rows = 3,
                columns = 3,
                openedCells = mapOf(
                    Pair(0, 0) to 1
                ),
                flags = setOf(Pair(1, 1)),
                mines = setOf(Pair(1, 1), Pair(2, 2))
            )
        )

        assertEquals(3, result.rows)
        assertEquals(3, result.columns)
        assertEquals(
            listOf(
                Cell.Open(1),
                Cell.Closed,
                Cell.Closed,
                Cell.Closed,
                Cell.Flagged,
                Cell.Closed,
                Cell.Closed,
                Cell.Closed,
                Cell.Mined
            ),
            result.cells
        )
        assertEquals(Status.Lost, result.status)
        assertNull(result.remainingMines)
    }

    @Test
    fun `test map won game`() {
        val tested = GameViewStatusMapper()

        val result = tested.map(
            GameStatus.Won(
                rows = 3,
                columns = 3,
                openedCells = mapOf(
                    Pair(0, 0) to 1
                ),
                flags = setOf(Pair(1, 1)),
            )
        )

        assertEquals(3, result.rows)
        assertEquals(3, result.columns)
        assertEquals(
            listOf(
                Cell.Open(1),
                Cell.Closed,
                Cell.Closed,
                Cell.Closed,
                Cell.Flagged,
                Cell.Closed,
                Cell.Closed,
                Cell.Closed,
                Cell.Closed
            ),
            result.cells
        )
        assertEquals(Status.Won, result.status)
        assertNull(result.remainingMines)
    }
}
