import java.lang.Exception
import java.lang.IllegalArgumentException

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

    //region Сеттері и геттері
    fun GetSize(): Int {
        return this.size
    }

    fun GetCells():Array<Array<Cell>>{
        return this.cells
    }
    //endregion

    private fun InitializeBoard() {
//        Установка шашек на правильніе позиции
        for (row in cells) {
            for (cell in row) {

                if (cell.row < 3 && cell.color == Colors.Black)
                    cell.checker = Checker(cell.row, cell.column, Colors.Black)
                if (cell.row > 4 && cell.color == Colors.Black)
                    cell.checker = Checker(cell.row, cell.column, Colors.White)
            }
        }
    }

    public fun GetCheckers(): ArrayList<Checker> {
        var checkers = ArrayList<Checker>()
        for (row in cells) {
            for (cell in row) {
                cell.checker?.let { checkers.add(it) }
            }
        }
        return checkers
    }

    public fun CheckCoordinate(row: Int, column: Int): Boolean {
        return row >= 0 && row < size && column >= 0 && column < size
    }

    public fun Get(row: Int, column: Int): Cell {
        if (!CheckCoordinate(row, column))
            throw IllegalArgumentException("Некоректные координаты клетки: ($row, $column)")
        return this.cells.get(row).get(column)
    }

    public fun MoveChecker(rowFrom: Int, columnFrom: Int, rowTo: Int, columnTo: Int) {
        var cellFrom = this.Get(rowFrom, columnFrom)
        if (cellFrom.checker == null)
            throw Exception("На исходной клетке нет шашки")
        var cellTo = this.Get(rowTo, columnTo)
        if (cellTo.checker != null)
            throw Exception("На конечной клетке уже стоит шашка")
        cellTo.SetChecker(cellFrom.checker!!)
        cellFrom.checker = null
    }

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