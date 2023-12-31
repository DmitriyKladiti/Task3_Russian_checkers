import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

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
                val cell = board.GetCell(i, j)
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

    //region Сеттері и геттер
    @Test
    fun testGetCheckers() {
        // Создаем доску и получаем все шашки
        val checkers = board.GetCheckers()

        // Проверяем, что список шашек содержит верное количество шашек
        Assertions.assertEquals(24, checkers.size, "Ожидается 24 шашки на доске")

        // Проверяем, что все шашки находятся на допустимых координатах
        for (checker in checkers) {
            val row = checker.row
            val column = checker.column
            Assertions.assertTrue(
                board.CheckCoordinate(row, column),
                "Недопустимые координаты для шашки: ($row, $column)"
            )
        }

        // Проверяем, что у всех клеток с шашками ссылка на шашку соответствует текущим координатам клетки
        for (checker in checkers) {
            val cell = board.GetCell(checker.row, checker.column)
            Assertions.assertEquals(checker, cell.checker, "Некорректная связь клетки и шашки")
        }
    }

    @Test
    fun testGetCell_ValidCoordinates() {
        val board = Board()

        // Проверяем, что метод возвращает ожидаемые клетки для всех допустимых координат
        for (row in 0 until board.GetSize()) {
            for (column in 0 until board.GetSize()) {
                val cell = board.GetCell(row, column)
                Assertions.assertEquals(row, cell.row, "Неверная строка для клетки ($row, $column)")
                Assertions.assertEquals(column, cell.column, "Неверный столбец для клетки ($row, $column)")
            }
        }
    }

    @Test
    fun testGetCell_InvalidCoordinates() {
        val board = Board()

        // Проверяем, что метод генерирует исключение для всех недопустимых координат
        val invalidCoordinates = listOf(
            Pair(-1, 0),
            Pair(0, -1),
            Pair(board.GetSize(), 0),
            Pair(0, board.GetSize()),
            Pair(-100, -100),
            Pair(100, 100)
        )

        for ((row, column) in invalidCoordinates) {
            val executable = Executable { board.GetCell(row, column) }
            Assertions.assertThrows(
                IllegalArgumentException::class.java, executable,
                "Ожидается исключение для недопустимых координат ($row, $column)"
            )
        }
    }
    //endregion

    //region Проверка корректности хода
    @Test
    fun testGet_InvalidCoordinates() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            board.GetCell(-1, 2) // Ожидается IllegalArgumentException при получении клетки с недопустимыми координатами
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
        Assertions.assertNull(board.GetCell(0, 1).checker)
        val checker = board.GetCell(1, 3).checker
        Assertions.assertNotNull(checker)
        if (checker != null) {
            Assertions.assertEquals(1, checker.row)
            Assertions.assertEquals(3, checker.column)
        }

    }
    //endregion

    //region Удаление
    @Test
    fun testRemoveChecker() {
        Assertions.assertNotNull(board.GetCell(1, 2).checker)
        board.RemoveChecker(1, 2)
        Assertions.assertNull(board.GetCell(1, 2).checker)
    }

    @Test
    fun testRemoveChecker_InvalidCoordinates() {
        for (row in -100..100) {
            for (column in -100..100) {
                if (!board.CheckCoordinate(row, column)) {
                    Assertions.assertThrows(Exception::class.java) {
                        board.RemoveChecker(row, column)
                    }
                }
            }
        }
    }

    @Test
    fun testRemoveChecker_NoChecker() {
        Assertions.assertNull(board.GetCell(0, 0).checker)
        Assertions.assertThrows(Exception::class.java) {
            board.RemoveChecker(0, 0)
        }
    }
    //endregion

    //region Преобразование в строку
    @Test
    fun testToString() {
        val board = Board()
        val expectedString = buildString {
            for (i in 0 until board.GetSize()) {
                for (j in 0 until board.GetSize()) {
                    val cell = board.GetCell(i, j)
                    append(cell.toString())
                }
                append('\n')
            }
        }

        Assertions.assertEquals(expectedString, board.toString())
    }
    //endregion

    //region Дамка
    @Test
    fun testMakeKing_SourceCellEmpty() {
        for (row in 0 until board.GetSize()) {
            for (column in 0 until board.GetSize()) {
                if (board.GetCell(row, column).checker == null) {
                    Assertions.assertThrows(Exception::class.java) {
                        board.MakeKing(row, column)
                    }
                }
            }
        }

    }

    @Test
    fun testMakeKing_InvalidCoordinates() {
        // Проверяем, что метод генерирует исключение для некорректных координат
        val board = Board()

        val invalidCoordinates = listOf(
            Pair(-1, 0),
            Pair(0, -1),
            Pair(board.GetSize(), 0),
            Pair(0, board.GetSize()),
            Pair(-100, -100),
            Pair(100, 100)
        )

        for ((row, column) in invalidCoordinates) {
            Assertions.assertThrows(java.lang.IllegalArgumentException::class.java) {
                board.MakeKing(row, column)
            }
        }
    }

    @Test
    fun testMakeKing() {
        for (row in 0 until board.GetSize()) {
            for (column in 0 until board.GetSize()) {
                if (board.GetCell(row, column).checker != null) {
                    board.MakeKing(row, column)
                    Assertions.assertEquals(CheckerType.King, board.GetCell(row, column).checker!!.type)
                }
            }
        }
    }
    //endregion

    //region Проверка доступніх ходов
    @Test
    fun testGetAvailableMoves_NoChecker_ReturnsEmptyList() {
        val board = Board()
        val row = 3
        val column = 2
        val availableMoves = board.GetAvailableMoves(row, column)
        assertEquals(emptyList<Pair<Int, Int>>(), availableMoves)
    }

    @Test
    fun testGetAvailableMoves_NormalChecker_ReturnsCorrectMoves() {
        val board = Board()
        val row = 5
        val column = 4
        val availableMoves = board.GetAvailableMoves(row, column)
        val expectedMoves = listOf(
            Pair(4, 3),
            Pair(4, 5)
        )
        assertEquals(expectedMoves, availableMoves)
    }

    @Test
    fun testGetAvailableMoves_KingChecker_EmptyBoard() {
        val board = Board()
        val row = 2
        val column = 3
        board.RemoveCheckers()
        val availableMoves = board.GetAvailableMoves(row, column)
        assertEquals(emptyList<Pair<Int, Int>>(), availableMoves)
    }

    @Test
    fun testGetAvailableMoves_KingChecker_AllMoves() {
        val board = Board()
        val row = 5
        val column = 4
        board.RemoveCheckers()
        board.AddChecker(row, column, Checker(row, column, Colors.Black, CheckerType.King))
        val availableMoves = board.GetAvailableMoves(row, column)
        // Здесь укажите ожидаемые координаты всех доступных ходов для дамки
        val expectedMoves = listOf(
            Pair(4, 3),
            Pair(3, 2),
            Pair(2, 1),
            Pair(1, 0),
            Pair(4, 5),
            Pair(3, 6),
            Pair(2, 7),
            Pair(6, 3),
            Pair(7, 2),
            Pair(6, 5),
            Pair(7, 6)
        )
        assertEquals(expectedMoves, availableMoves)
        assertTrue(expectedMoves.containsAll(availableMoves))
    }
    //endregion

    //region Проверка доступных атак
    @Test
    fun testGetAvailableBeats_KingChecker_Beats() {
        val board = Board()
        val row = 5
        val column = 4
        board.RemoveCheckers()

        // Размещаем дамку на доске
        board.AddChecker(row, column, Checker(row, column, Colors.Black, CheckerType.King))

        // Размещаем шашки противника на доске для проверки возможных атак
        board.AddChecker(4, 3, Checker(4, 3, Colors.White, CheckerType.Checker))
        board.AddChecker(6, 5, Checker(6, 5, Colors.White, CheckerType.Checker))

        val availableBeats = board.GetAvailableBeats(row, column)

        // Здесь укажите ожидаемые координаты всех доступных атак для дамки
        val expectedBeats = listOf(
            Pair(3, 2), // Атака вверх-влево после шашки на 4,3
            Pair(7, 6),  // Атака вниз-вправо после шашки на 6,5
            Pair(2, 1),
            Pair(1, 0)
        )

        assertEquals(expectedBeats.size, availableBeats.size)
        assertTrue(expectedBeats.containsAll(availableBeats))
    }

    @Test
    fun testGetAvailableBeats_NormalChecker_Beats() {
        val board = Board()
        val row = 5
        val column = 4
        board.RemoveCheckers()

        // Размещаем обычную шашку на доске
        board.AddChecker(row, column, Checker(row, column, Colors.Black, CheckerType.Checker))

        // Размещаем шашки противника на доске для проверки возможных атак
        board.AddChecker(4, 3, Checker(4, 3, Colors.White, CheckerType.Checker))
        board.AddChecker(4, 5, Checker(4, 5, Colors.White, CheckerType.Checker))
        board.AddChecker(6, 3, Checker(6, 3, Colors.White, CheckerType.Checker))
        board.AddChecker(6, 5, Checker(6, 5, Colors.White, CheckerType.Checker))


        val availableBeats = board.GetAvailableBeats(row, column)

        // Ожидаемые координаты всех доступных атак для обычной шашки
        val expectedBeats = listOf(
            Pair(3, 2),  // Атака вверх-влево после шашки на 4,3
            Pair(3, 6),  // Атака вверх-вправо после шашки на 4,5
            Pair(7, 2),  // Атака вниз-влево после шашки на 6,3
            Pair(7, 6)   // Атака вниз-вправо после шашки на 6,5
        )

        assertEquals(expectedBeats, availableBeats)
        assertTrue(expectedBeats.containsAll(availableBeats))
    }

    //endregion
}