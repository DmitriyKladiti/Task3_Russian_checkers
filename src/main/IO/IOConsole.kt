val ANSI_RESET = "\u001B[0m"
val ANSI_BLACK = "\u001B[30m"
val ANSI_BROWN = "\u001B[33m"

val ANSI_WHITE = "\u001B[37m"
val ANSI_RED = "\u001B[31m"
val ANSI_GREEN = "\u001B[32m"
val ANSI_LIGHT_CYAN = "\u001B[36m"
val ANSI_BG_RED = "\u001B[41m"
val ANSI_BG_GREEN = "\u001B[42m"
val ANSI_BG_GREEN_BROWN = "\u001B[42;33m"
val ANSI_BG_LIGHT_CYAN = "\u001B[46m"
val ANSI_BG_WHITE = "\u001B[47m";
val ANSI_BG_BLACK = "\u001B[40m"
val ANSI_BG_YELLOW_BLACK = "\u001B[43;30m"


class IOConsole : IO {
    /**
     * Получает строку.
     *
     * @return Строка.
     */
    override fun GetStr(): String {
        TODO("Not yet implemented")
    }

    /**
     * Получает координаты.
     *
     * @return Пара координат (x, y).
     */
    override fun GetCoordinates(): Pair<Int, Int> {
        TODO("Not yet implemented")
    }

    /**
     * Отображает сообщение.
     *
     * @param message Сообщение для отображения.
     */
    override fun ShowMessage(message: String) {
        TODO("Not yet implemented")
    }

    private fun PrinCell(cell: Cell, part: CellParts) {
        if (cell == null)
            throw Exception("Передана пустая ссілка на клетку!")
        var cellStr = ""

        //region Выбор части клетки
        if (part == CellParts.Top) {
            cellStr = "┌───┐"
        }
        if (part == CellParts.Middle) {
            if (cell.displayContent != null) {
                cellStr = "│ ${cell.displayContent} │"
            } else {
                var checkerStr = " "
                if (cell.checker != null)
                    checkerStr = cell.checker.toString()
                cellStr = "│ $checkerStr │"
            }
        }
        if (part == CellParts.Bottom) {
            cellStr = "└───┘"
        }
        //endregion

        //region Выделение цветом в зависимости от состояния клетки
        if (cell.selection == Selections.None) {
            if (cell.color == Colors.White)
                PrintStr(ANSI_BLACK, ANSI_BG_WHITE, cellStr)
            if (cell.color == Colors.Black)
                PrintStr(ANSI_WHITE, ANSI_BG_BLACK, cellStr)
            if (cell.color == Colors.Edge)
                PrintStr(ANSI_BLACK, ANSI_BG_YELLOW_BLACK, cellStr)
        }
        if (cell.selection == Selections.CheckerSelected) {
            PrintStr(ANSI_BLACK, ANSI_BG_GREEN, cellStr)
        }
        if (cell.selection == Selections.AvailableMove) {
            PrintStr(ANSI_BLACK, ANSI_BG_LIGHT_CYAN, cellStr)
        }
        if (cell.selection == Selections.AvailableBeat) {
            PrintStr(ANSI_BLACK, ANSI_BG_RED, cellStr)
        }
        //endregion

    }

    fun ShowBoardRow(cells: Array<Cell>) {
        for (element in cells) {
            this.PrinCell(element, CellParts.Top)
        }
        println()
        for (element in cells) {
            this.PrinCell(element, CellParts.Middle)
        }
        println()
        for (element in cells) {
            this.PrinCell(element, CellParts.Bottom)
        }
        println()
    }


    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     * @param isShowColumnsRowsNumbers Показывать номера столбцов и строк или нет.
     */
    override fun ShowBoard(board: Board, isShowColumnsRowsNumbers: Boolean) {
        var cellsList: ArrayList<Cell> = ArrayList()
        //region Вывод строки с номерами столбцов
        if (isShowColumnsRowsNumbers) {
            for (i in 0..board.GetSize()) {
                var cell = Cell(-1, -1, Colors.Edge, null, Selections.None)
                if (i > 0) {
                    cell.displayContent = (i - 1).toString()
                }
                cellsList.add(cell)
            }
            cellsList.add(Cell(-1, -1, Colors.Edge, null, Selections.None))
            this.ShowBoardRow(cellsList.toTypedArray())
        }
        //endregion

        //region Вывод строк игрового поля
        for (i in 0 until board.GetSize()) {
            if (isShowColumnsRowsNumbers) {
                cellsList.clear()
                var cell = Cell(-1, -1, Colors.Edge, null, Selections.None, i.toString())
                cellsList.add(cell)
                cellsList.addAll(board[i])
                cellsList.add(cell)
                this.ShowBoardRow(cellsList.toTypedArray())
            } else {
                this.ShowBoardRow(board[i])
            }
        }
        //endregion

        //region Вывод строки с номерами столбцов
        if (isShowColumnsRowsNumbers) {
            cellsList.clear()
            for (i in 0..board.GetSize()) {
                var cell = Cell(-1, -1, Colors.Edge, null, Selections.None, " ")
                if (i > 0) {
                    cell.displayContent = (i - 1).toString()
                }
                cellsList.add(cell)
            }
            cellsList.add(Cell(-1, -1, Colors.Edge, null, Selections.None, " "))
            this.ShowBoardRow(cellsList.toTypedArray())
        }
        //endregion
    }

    fun PrintStr(textColor: String, backgroundColor: String, text: String) {
        val coloredText = "$backgroundColor$textColor$text$ANSI_RESET$ANSI_WHITE"
        print(coloredText)
    }
}