import java.lang.Exception
import java.lang.IllegalArgumentException

@Suppress("FunctionName")
/**
 * Класс игровой доски
 */
class Board {
    private val size: Int = 8
    private val cells: Array<Array<Cell>> = Array(size) { i ->
        Array(size) { j ->
            Cell(
                i, j,
                if ((i + j) % 2 == 0) Colors.White else Colors.Black
            )
        }
    }

    init {
        InitializeBoard()
    }

    //region Сеттеры и геттеры
    /**
     * Возвращает размер доски
     */
    fun GetSize(): Int {
        return this.size
    }

    /**
     * Возвращает клетку с указанными координатами
     * @param row номер строки
     * @param column номер столбца
     * @throws IllegalArgumentException если переданные координаты некорректны
     */
    fun GetCell(row: Int, column: Int): Cell {
        if (!CheckCoordinate(row, column))
            throw IllegalArgumentException("Некорректные координаты клетки: ($row, $column)")
        return this.cells[row][column]
    }

    /**
     * Получает массив клеток для указанной строки доски.
     *
     * @param row Номер строки.
     * @return Массив клеток для указанной строки.
     * @throws IllegalStateException Если доска не была инициализирована.
     * @throws IllegalArgumentException Если номер строки некорректен.
     */
    fun GetRow(row: Int): Array<Cell> {
        if (this.cells == null) {
            throw IllegalStateException("Доска не инициализирована")
        }
        if (row < 0 || row >= this.GetSize()) {
            throw IllegalArgumentException("Некорректный номер строки: $row")
        }
        return this.cells[row]
    }


    /**
     * Возвращает двумерный массив всех клеток доски
     * (предпочтительнее использовать этот метод только для тестирования или особых случаев)
     * @return двумерный массив клеток
     */
    @Suppress("unused")
    fun GetCells(): Array<Array<Cell>> {
        return this.cells
    }
    //endregion

    //region Методы

    //region Инициализация
    private fun InitializeBoard() {
//        Установка шашек на правильные позиции
        for (row in cells) {
            for (cell in row) {
                if (cell.row < 3 && cell.color == Colors.Black)
                    cell.checker = Checker(cell.row, cell.column, Colors.Black)
                if (cell.row > 4 && cell.color == Colors.Black)
                    cell.checker = Checker(cell.row, cell.column, Colors.White)
            }
        }
    }
    //endregion

    //region Проверки
    /**
     * Проверяет, являются ли переданные координаты допустимыми на доске
     * @param row номер строки
     * @param column номер столбца
     * @return true, если координаты допустимы, иначе false
     */
    fun CheckCoordinate(row: Int, column: Int): Boolean {
        return row >= 0 && row < size && column >= 0 && column < size
    }

    fun GetAvailableMoves(row: Int, column: Int): List<Pair<Int, Int>> {
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        val cell = GetCell(row, column)
        // Если на клетке нет шашки или дамки, возвращаем пустой список
        val checker = cell.checker ?: return emptyList()
        if (checker.type == CheckerType.King) {
            AddDiagonalMoves(row, column, -1, -1, availableMoves) // вверх-влево
            AddDiagonalMoves(row, column, -1, 1, availableMoves)  // вверх-вправо
            AddDiagonalMoves(row, column, 1, -1, availableMoves)  // вниз-влево
            AddDiagonalMoves(row, column, 1, 1, availableMoves)   // вниз-вправо
        } else {
            if (cell.checker!!.color == Colors.White) {
                AddDiagonalMove(row, column, -1, -1, availableMoves) // вниз-влево
                AddDiagonalMove(row, column, -1, 1, availableMoves)  // вниз-вправо
            } else if (cell.checker!!.color == Colors.Black) {
                AddDiagonalMove(row, column, 1, -1, availableMoves) // вверх-влево
                AddDiagonalMove(row, column, 1, 1, availableMoves)  // вверх-вправо
            }
        }

        return availableMoves
    }

    fun GetAvailableBeats(row: Int, column: Int): List<Pair<Int, Int>> {
        val availableBeats = mutableListOf<Pair<Int, Int>>()
        val cell = GetCell(row, column)
        val checker = cell.checker ?: return emptyList()

        val directions = if (checker.type == CheckerType.King) {
            (-7..7).flatMap { rowDelta ->
                (-7..7).map { colDelta ->
                    Pair(rowDelta, colDelta)
                }
            }
        } else {
            listOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1))
        }


        for ((rowDelta, colDelta) in directions) {
            val captureRow = row + rowDelta * 2
            val captureColumn = column + colDelta * 2
            val landingRow = row + rowDelta
            val landingColumn = column + colDelta

            if (CheckCoordinate(captureRow, captureColumn) && CheckCoordinate(landingRow, landingColumn)) {
                val captureCell = GetCell(captureRow, captureColumn)
                val captureChecker = captureCell.checker
                val landingCell = GetCell(landingRow, landingColumn)
                val landingChecker = landingCell.checker

                if (captureChecker == null && landingChecker != null && landingChecker.color != checker.color) {
                    availableBeats.add(Pair(captureRow, captureColumn))
                }
            }
        }

        return availableBeats
    }


    /**
     * Рекурсивный метод для добавления возможных ходов по диагонали.
     */
    private fun AddDiagonalMoves(row: Int, column: Int, rowDir: Int, colDir: Int, moves: MutableList<Pair<Int, Int>>) {
        val newRow = row + rowDir
        val newColumn = column + colDir
        if (!CheckCoordinate(newRow, newColumn)) {
            return
        }
        val cell = GetCell(newRow, newColumn)
        // Если на клетке нет шашки, добавляем ход
        if (cell.checker == null) {
            moves.add(Pair(newRow, newColumn))
            AddDiagonalMoves(newRow, newColumn, rowDir, colDir, moves) // Рекурсивно ищем следующие ходы
        }
    }

    /**
     * Метод для добавления возможного хода по диагонали.
     */
    private fun AddDiagonalMove(row: Int, column: Int, rowDir: Int, colDir: Int, moves: MutableList<Pair<Int, Int>>) {
        val newRow = row + rowDir
        val newColumn = column + colDir

        if (CheckCoordinate(newRow, newColumn) && GetCell(newRow, newColumn).checker == null) {
            moves.add(Pair(newRow, newColumn))
        }
    }
    //endregion

    //region Работа с шашками
    /**
     * Возвращает список всех шашек на доске
     * @return список шашек
     */
    fun GetCheckers(): ArrayList<Checker> {
        val checkers = ArrayList<Checker>()
        for (row in cells) {
            for (cell in row) {
                cell.checker?.let { checkers.add(it) }
            }
        }
        return checkers
    }

    /**
     * Добавляет шашку на указанную клетку доски.
     *
     * @param row Строка клетки.
     * @param column Колонка клетки.
     * @param checker Шашка для добавления.
     * @throws Exception Если на указанной клетке уже есть шашка.
     */
    fun AddChecker(row: Int, column: Int, checker: Checker) {
        val cell = GetCell(row, column)
        if (cell.checker != null) {
            throw Exception("На указанной клетке уже есть шашка")
        }
        cell.checker = checker
    }


    /**
     * Перемещает шашку с указанными координатами на другую клетку
     * @param rowFrom номер строки исходной клетки
     * @param columnFrom номер столбца исходной клетки
     * @param rowTo номер строки конечной клетки
     * @param columnTo номер столбца конечной клетки
     * @throws Exception если на исходной клетке нет шашки или на конечной клетке уже стоит шашка
     */
    fun MoveChecker(rowFrom: Int, columnFrom: Int, rowTo: Int, columnTo: Int) {
        val cellFrom = this.GetCell(rowFrom, columnFrom)
        if (cellFrom.checker == null)
            throw Exception("На исходной клетке нет шашки")
        val cellTo = this.GetCell(rowTo, columnTo)
        if (cellTo.checker != null)
            throw Exception("На конечной клетке уже стоит шашка")
        cellTo.SetChecker(cellFrom.checker!!)
        cellFrom.SetChecker(null)
    }

    /**
     * Удаляет шашку с указанными координатами
     * @param row номер строки клетки с шашкой
     * @param column номер столбца клетки с шашкой
     * @throws Exception если на указанной клетке нет шашки
     */
    fun RemoveChecker(row: Int, column: Int) {
        val cell = this.GetCell(row, column)
        if (cell.checker == null)
            throw Exception("На исходной клетке нет шашки")
        cell.SetChecker(null)
    }

    /**
     * Удаляет все шашки с доски.
     */
    fun RemoveCheckers() {
        for (i in 0 until this.GetSize()) {
            for (j in 0 until this.GetSize()) {
                if (this.GetCell(i, j).checker != null)
                    RemoveChecker(i, j)
            }
        }
    }


    /**
     * Превращает шашку на указанных координатах в короля
     * @param row номер строки клетки с шашкой
     * @param column номер столбца клетки с шашкой
     * @throws Exception если на указанной клетке нет шашки
     */
    fun MakeKing(row: Int, column: Int) {
        val cell = this.GetCell(row, column)
        if (cell.checker == null)
            throw Exception("На исходной клетке нет шашки")
        cell.checker!!.type = CheckerType.King
    }

    fun SelectChecker(row: Int, column: Int) {
        val cell = this.GetCell(row, column)
        if (cell.checker == null) {
            throw Exception("На указанной клетке нет шашки")
        }
        SelectCell(row, column, Selections.CheckerSelected)
        var availableBeats = this.GetAvailableBeats(row, column)
        if (availableBeats.size > 0) {
            for (item in availableBeats) {
                SelectCell(item.first, item.second, Selections.AvailableBeat)
            }
        } else {
            val availableMoves = this.GetAvailableMoves(row, column)
            for (item in availableMoves) {
                SelectCell(item.first, item.second, Selections.AvailableMove)
            }
        }
    }
    //endregion

    //region Работа с клетками
    fun UnselectAll() {
        for (row in cells) {
            for (cell in row) {
                cell.selection = Selections.None
            }
        }
    }

    fun SelectCell(row: Int, column: Int, selection: Selections) {
        val cell = this.GetCell(row, column)
        cell.selection = selection
    }
    //endregion


    /**
     * Возвращает строковое представление игровой доски
     * @return строка, представляющая доску
     */
    override fun toString(): String {
        val builder = StringBuilder()
        for (row in cells) {
            for (cell in row) {
                builder.append(cell)
            }
            builder.append('\n')
        }
        return builder.toString()
    }
    //endregion

    //region Операторі
    operator fun get(row: Int, column: Int): Cell {
        return GetCell(row, column)
    }

    operator fun set(row: Int, column: Int, cell: Cell) {
        var targetCell = GetCell(row, column)
        targetCell = cell
    }

    operator fun get(row: Int): Array<Cell> {
        return GetRow(row)
    }
    //endregion
}
