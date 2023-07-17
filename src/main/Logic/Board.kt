import java.lang.IllegalArgumentException

class Board {
    val size: Int = 8
    val cells: Array<Array<Cell>> = Array(size) { i ->
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

    public fun MoveChecker(rowFrom: Int, columnFrom: Int, rowTo: Int, columnTo: Int) {
        if (!CheckCoordinate(rowFrom, columnFrom))
            throw IllegalArgumentException("Неправильные начальные координаты: ($rowFrom, $columnFrom)")
        if (!CheckCoordinate(rowTo, columnTo))
            throw IllegalArgumentException("Неправильные конечные координаты: ($rowTo, $columnTo)")
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