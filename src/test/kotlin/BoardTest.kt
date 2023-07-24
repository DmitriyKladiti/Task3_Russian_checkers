import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BoardTest {
    private lateinit var board: Board
    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        board = Board()
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
    }

    @Test
    fun testGetCheckers() {
        val checkers = board.GetCheckers()
        Assertions.assertEquals(24, checkers.size) // Ожидается 24 шашки на доске
    }

    @Test
    fun testGet_InvalidCoordinates() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            board.Get(-1, 2) // Ожидается IllegalArgumentException при получении клетки с недопустимыми координатами
        }
    }

    @Test
    fun testMoveChecker_InvalidMove_SourceCellEmpty() {
        Assertions.assertThrows(Exception::class.java) {
            board.MoveChecker(4, 3, 5, 4) // Ожидается Exception при попытке переместить шашку с пустой исходной клетки
        }
    }

    @Test
    fun testMoveChecker_InvalidMove_TargetCellOccupied() {
        Assertions.assertThrows(Exception::class.java) {
            board.MoveChecker(5, 4, 5, 4) // Ожидается Exception при попытке переместить шашку на занятую конечную клетку
        }
    }

    @Test
    fun testChessPattern() {
        val expectedColors = arrayOf(
            arrayOf(Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black),
            arrayOf(Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White),
            arrayOf(Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black),
            arrayOf(Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White),
            arrayOf(Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black),
            arrayOf(Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White),
            arrayOf(Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black),
            arrayOf(Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White, Colors.Black, Colors.White)
        )

        for (i in 0 until board.size) {
            for (j in 0 until board.size) {
                val cell = board.Get(i, j)
                val expectedColor = expectedColors[i][j]
                Assertions.assertEquals(expectedColor, cell.color, "Неверный цвет в клетке ($i, $j)")

                if ((i < 3 || i > 4) && cell.color != Colors.White) {
                    Assertions.assertNotNull(cell.checker, "Отсутствует шашка в клетке ($i, $j)")
                } else {
                    Assertions.assertNull(cell.checker, "Неожиданная шашка в клетке ($i, $j)")
                }
            }
        }
    }



    @org.junit.jupiter.api.Test
    fun getSize() {
    }

    @org.junit.jupiter.api.Test
    fun getCells() {
    }

    @org.junit.jupiter.api.Test
    fun getCheckers() {
    }

    @org.junit.jupiter.api.Test
    fun checkCoordinate() {
    }

    @org.junit.jupiter.api.Test
    fun get() {
    }

    @org.junit.jupiter.api.Test
    fun moveChecker() {
    }

    @org.junit.jupiter.api.Test
    fun testToString() {
    }
}