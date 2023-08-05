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
        return this.cells.get(row).get(column)
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
     * Проверяет, являются ли переданные координаты допустимыми на доске
     * @param row номер строки
     * @param column номер столбца
     * @return true, если координаты допустимы, иначе false
     */
    fun CheckCoordinate(row: Int, column: Int): Boolean {
        return row >= 0 && row < size && column >= 0 && column < size
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
}
