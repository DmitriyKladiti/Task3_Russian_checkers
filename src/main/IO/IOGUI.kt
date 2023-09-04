import java.awt.Color
import java.awt.GridLayout
import java.io.Serializable
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JPanel

class IOGUI(override val isReady: Boolean = true) : IO, Serializable {

    //region Поля
    /**
     * Выбранные пользователем клетки
     */
    override var selectedCellsList = arrayListOf<Pair<Int, Int>>()

    /**
     * Текущее сообщение
     */
    override var currentMessage: String = ""
    //endregion

    //region Методы

    /**
     * Добавляет выбранную клетку в список выбранных клеток на основе указанных координат.
     *
     * @param rowTo    Ряд, в который необходимо добавить выбранную клетку.
     * @param columnTo Столбец, в который необходимо добавить выбранную клетку.
     */
    override fun AddSelectedCell(rowTo: Int, columnTo: Int) {
        if (this.selectedCellsList.size == 2) {
            this.selectedCellsList.clear()
        }
        this.selectedCellsList.add(Pair<Int, Int>(rowTo, columnTo))
    }

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
    override fun Show(message: String) {
        this.currentMessage = message
    }

    private fun CreateCellButton(cell: Cell): JButton {
        val button = JButton()
        button.putClientProperty("row", cell.row)
        button.putClientProperty("col", cell.column)
        button.addActionListener {
            val clickedRow = button.getClientProperty("row") as Int
            val clickedCol = button.getClientProperty("col") as Int
            this.AddSelectedCell(clickedRow, clickedCol)
        }

        // Установка изображения на кнопке в зависимости от содержимого клетки
        if (cell.checker != null) {
            var imagePath: String? = null
            if (cell.checker!!.color == Colors.White) {
                imagePath = "src/main/resources/white.png"
                if (cell.checker!!.type == CheckerType.King)
                    imagePath = "src/main/resources/whiteKing.png"
            } else {
                imagePath = "src/main/resources/black.png"
                if (cell.checker!!.type == CheckerType.King)
                    imagePath = "src/main/resources/blackKing.png"
            }

            if (imagePath != null) {
                val icon = ImageIcon(imagePath)
                button.icon = icon
            }
        }

        // Установка цвета фона кнопки в зависимости от состояния клетки и ее цвета
        when (cell.selection) {
            Selections.None -> {
                when (cell.color) {
                    Colors.White -> button.background = Color.WHITE
                    Colors.Black -> button.background = Color.BLACK
                    Colors.Edge -> button.background = Color.YELLOW // Или другой цвет для границы
                }
            }

            Selections.CheckerSelected -> button.background = Color.GREEN
            Selections.AvailableMove -> button.background = Color.CYAN
            Selections.AvailableBeat -> button.background = Color.RED
            Selections.AvailableCheckerToBeat -> button.background = Color.MAGENTA // Или другой цвет
        }

        return button
    }


    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     * @param isShowColumnsRowsNumbers Показывать номера столбцов и строк или нет.
     */
    override fun Show(board: Board, isShowColumnsRowsNumbers: Boolean): Any? {
        val panel = JPanel()
        val size = board.GetSize()
        panel.layout = GridLayout(size, size)

        for (i in 0 until size) {
            for (j in 0 until size) {
                val cell = board.GetCell(i, j)
                val button = CreateCellButton(cell)
                panel.add(button)
            }
        }

        return panel
    }

    //endregion

}
