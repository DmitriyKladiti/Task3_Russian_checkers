import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BoardTest {
    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        board = Board()
    }

    @AfterEach
    fun tearDown() {
    }

    //region Проверка инициализации поля перед игрой

    @Test
    fun testStartChessPattern() {
        val expectedColors = arrayOf(
            arrayOf(
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black
            ),
            arrayOf(
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White
            ),
            arrayOf(
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black
            ),
            arrayOf(
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White
            ),
            arrayOf(
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black
            ),
            arrayOf(
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White
            ),
            arrayOf(
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black
            ),
            arrayOf(
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White,
                Colors.Black,
                Colors.White
            )
        )

        for (i in 0 until board.GetSize()) {
            for (j in 0 until board.GetSize()) {
                val cell = board.Get(i, j)
                val expectedColor = expectedColors[i][j]
                Assertions.assertEquals(expectedColor, cell.color, "Неверный цвет в клетке ($i, $j)")

                if ((i < 3 || i > 4) && cell.color != Colors.White) {
                    Assertions.assertNotNull(cell.checker, "Отсутствует шашка в клетке ($i, $j)")
                    val checker = cell.checker
                    if (checker != null) {
                        val expectedCheckerColor = if (checker.row < 3) Colors.Black else Colors.White
                        Assertions.assertEquals(
                            expectedCheckerColor,
                            checker.color,
                            "Неверный цвет шашки на месте ($checker.row, $checker.column)"
                        )
                    }
                } else {
                    Assertions.assertNull(cell.checker, "Неожиданная шашка в клетке ($i, $j)")
                }
            }
        }
    }
    //endregion

    //region Проверка корректности хода
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
            board.MoveChecker(
                5,
                4,
                5,
                4
            ) // Ожидается Exception при попытке переместить шашку на занятую конечную клетку
        }
    }

    @Test
    fun testMoveChecker_Correct() {
        board.MoveChecker(0, 1, 1, 3)
        Assertions.assertNull(board.Get(0, 1).checker)
        val checker = board.Get(1, 3).checker
        Assertions.assertNotNull(checker)
        if (checker != null) {
            Assertions.assertEquals( 1,checker.row)
            Assertions.assertEquals( 3,checker.column)
        }

    }
    //endregion


    @Test
    fun testToString() {
    }
}