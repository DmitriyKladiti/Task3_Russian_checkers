val ANSI_RESET = "\u001B[0m"
val ANSI_BLACK = "\u001B[30m"
val ANSI_WHITE = "\u001B[37m"
val ANSI_RED = "\u001B[31m"
val ANSI_GREEN = "\u001B[32m"
val ANSI_LIGHT_CYAN = "\u001B[36m"
val ANSI_BG_RED = "\u001B[41m"
val ANSI_BG_GREEN = "\u001B[42m"
val ANSI_BG_LIGHT_CYAN = "\u001B[46m"
val ANSI_BG_WHITE = "\u001B[47m";
val ANSI_BG_BLACK = "\u001B[40m"


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
            var checkerStr = " "
            if (cell.checker != null)
                checkerStr = cell.checker.toString()
            cellStr = "│ $checkerStr │"
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
        }
        if (cell.selection == Selections.CheckerSelected) {
            PrintStr(ANSI_BLACK, ANSI_BG_GREEN, cellStr)
        }
        if (cell.selection == Selections.AvailableMove) {
            PrintStr(ANSI_BLACK, ANSI_BG_LIGHT_CYAN, cellStr)
        }
        //endregion

    }

    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     */
    override fun ShowBoard(board: Board) {
        for (i in 0 until board.GetSize()) {
            for (j in 0 until board.GetSize()) {
                this.PrinCell(board.GetCell(i, j), CellParts.Top)
            }
            println()
            for (j in 0 until board.GetSize()) {
                this.PrinCell(board.GetCell(i, j), CellParts.Middle)
            }
            println()
            for (j in 0 until board.GetSize()) {
                this.PrinCell(board.GetCell(i, j), CellParts.Bottom)
            }
            println()
        }
    }

    fun PrintStr(textColor: String, backgroundColor: String, text: String) {
        val coloredText = "$backgroundColor$textColor$text$ANSI_RESET$ANSI_WHITE"
        print(coloredText)
    }
}