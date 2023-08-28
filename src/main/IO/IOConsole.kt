import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

//region Цвет текста
val ANSI_RESET = "\u001B[0m"
val ANSI_BLACK = "\u001B[30m"
val ANSI_RED = "\u001B[31m"
val ANSI_GREEN = "\u001B[32m"
val ANSI_YELLOW = "\u001B[33m"
val ANSI_BLUE = "\u001B[34m"
val ANSI_PURPLE = "\u001B[35m"
val ANSI_CYAN = "\u001B[36m"
val ANSI_WHITE = "\u001B[37m"
//endregion

//region Цвет фона
val ANSI_BG_BLACK = "\u001B[40m"
val ANSI_BG_RED = "\u001B[41m"
val ANSI_BG_GREEN = "\u001B[42m"
val ANSI_BG_YELLOW_BLACK = "\u001B[43;30m"
val ANSI_BG_YELLOW = "\u001B[43m"
val ANSI_BG_BLUE = "\u001B[44m"
val ANSI_BG_PURPLE = "\u001B[45m"
val ANSI_BG_CYAN = "\u001B[46m"
val ANSI_BG_WHITE = "\u001B[47m"

//endregion


class IOConsole(override val isReady: Boolean = true) : IO, Serializable {
    /**
     * Получает строку.
     *
     * @return Строка.
     */
    override fun GetStr(): String {
        TODO("Not yet implemented")
    }

    /**
     * Запрашивает у пользователя целое число через консоль.
     * В случае ввода некорректного значения, метод повторно запрашивает число.
     *
     * @return Введенное пользователем целое число.
     */
    override fun GetInt(): Int {
        try {
//            print("Введите целое число: ")
            return readln().toInt()
        } catch (e: NumberFormatException) {
            this.Show("Это не целое число. Попробуйте еще раз.")
            return this.GetInt()
        }

    }

    /**
     * Получает координаты.
     *
     * @return Пара координат (x, y).
     */
    override fun GetCoordinates(): Pair<Int, Int> {
        // Запрашиваем строку у пользователя
        Show("Введите номер строки: ")
        val row = this.GetInt()

        // Запрашиваем столбец у пользователя
        Show("Введите номер столбца: ")
        val column = this.GetInt()

        return Pair(row, column)
    }


    /**
     * Отображает сообщение.
     *
     * @param message Сообщение для отображения.
     */
    override fun Show(message: String) {
        println(message)
    }

    /**
     * Выводит список на экран.
     *
     * @param options Список єлементов для вывода на экран.
     */
    override fun Show(options: List<String>) {
        Show("Выберите один из пунктов:")
        for ((index, option) in options.withIndex()) {
            println("${index + 1}. $option")
        }
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
            PrintStr(ANSI_BLACK, ANSI_BG_CYAN, cellStr)
        }
        if (cell.selection == Selections.AvailableBeat) {
            PrintStr(ANSI_BLACK, ANSI_BG_RED, cellStr)
        }
        if (cell.selection == Selections.AvailableCheckerToBeat) {
            PrintStr(ANSI_WHITE, ANSI_BG_PURPLE, cellStr)
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
    override fun Show(board: Board, isShowColumnsRowsNumbers: Boolean) {
//        this.clearConsole()
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

    /**
     * Выводит главное меню на экран.
     *
     */
    override fun ShowMainMenu(): Commands {
        val menuOptions = listOf("Новая игра", "Загрузить игру", "Выход")
        Show(menuOptions)
        var index = this.GetInt() - 1
        if (index < 0 || index >= menuOptions.size) {
            Show("Указан некорректный индекс!")
            return this.ShowMainMenu()
        }
        if (menuOptions[index] == "Новая игра")
            return Commands.Start
        if (menuOptions[index] == "Загрузить игру")
            return Commands.Load
        if (menuOptions[index] == "Выход")
            return Commands.Exit
        throw IllegalStateException("Неизвестная команда")
    }

    /**
     * Выводит меню хода (показывается пере ходом игрока) на экран.
     *
     */
    override fun ShowMoveMenu(): Commands {
        val menuOptions = listOf("Сделать ход", "Сохранить игру", "Загрузить игру", "Выход")
        Show(menuOptions)
        var index = this.GetInt() - 1
        if (index < 0 || index >= menuOptions.size) {
            Show("Указан некорректный индекс!")
            return this.ShowMainMenu()
        }
        if (menuOptions[index] == "Сделать ход")
            return Commands.GetCoordinate
        if (menuOptions[index] == "Сохранить игру")
            return Commands.Save
        if (menuOptions[index] == "Загрузить игру")
            return Commands.Load
        if (menuOptions[index] == "Выход")
            return Commands.Exit
        throw IllegalStateException("Неизвестная команда")
    }

    fun PrintStr(textColor: String, backgroundColor: String, text: String) {
        val coloredText = "$backgroundColor$textColor$text$ANSI_RESET$ANSI_WHITE"
        print(coloredText)
    }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
    }
}