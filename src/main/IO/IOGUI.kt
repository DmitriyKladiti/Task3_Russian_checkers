import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.Serializable
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter

class IOGUI : IO, Serializable {

    //region Поля
    var isDebug: Boolean = false
    override var isReady: Boolean

    /**
     * Выбранные пользователем клетки
     */
    override var selectedCellsList: ArrayList<Pair<Int, Int>>

    /**
     * Объект, отчевающий за логику игры
     */
    override var game: Game

    /**
     * Текущая команда на выполнение
     */
    override var currentCommand: Commands
    private var fMain: JFrame
    private var pMain: JPanel
    private val lblStatus: JLabel
    //endregion

    //region Конструкторы
    constructor(game: Game = Game(), isReady: Boolean = true) {

        //region Инициализация полей
        this.isReady = isReady
        this.game = game
        this.currentCommand = Commands.None
        this.selectedCellsList = arrayListOf<Pair<Int, Int>>()
        this.pMain = JPanel()
        this.lblStatus = JLabel("Состояние: Готово")
        //endregion

        //region Меню
        // Создаем строку меню
        val menuBar = JMenuBar()

        // Создаем меню "Игра"
        val gameMenu = JMenu("Игра")

        // Создаем пункты меню
        val newGameItem = JMenuItem("Новая игра")
        val loadGameItem = JMenuItem("Загрузить")
        val saveGameItem = JMenuItem("Сохранить")
        val exitGameItem = JMenuItem("Выход")

        // Добавляем слушателей событий для пунктов меню
        newGameItem.addActionListener { this.StartNewGame() }
        loadGameItem.addActionListener { this.LoadGame() }
        saveGameItem.addActionListener { this.SaveGame() }
        exitGameItem.addActionListener { this.Exit() } // Закрыть программу

        // Добавляем пункты меню в меню "Игра"
        gameMenu.add(newGameItem)
        gameMenu.add(loadGameItem)
        gameMenu.add(saveGameItem)
        gameMenu.add(exitGameItem)

        // Добавляем меню "Игра" в строку меню
        menuBar.add(gameMenu)
        //endregion

        //region Создание окна
        // Добавляем строку меню в главный JFrame
        fMain = JFrame("Шашки")
        fMain.jMenuBar = menuBar
        fMain.setSize(800, 800)
        fMain.layout = BorderLayout()
        fMain.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        fMain.add(pMain, BorderLayout.CENTER)
        fMain.add(lblStatus, BorderLayout.SOUTH)  // Строка состояния
        fMain.isVisible = true
        //endregion
    }
    //endregion

    //region Сеттеры и геттеры
    fun SetCurrentCommand(command: Commands) {
        this.isReady = true
        this.currentCommand = command
    }

    fun GetCurrentCommand(): Commands {
        this.isReady = false
        val command = this.currentCommand
        this.currentCommand = Commands.None
        return command
    }

    fun GetSelectedCells(): ArrayList<Pair<Int, Int>> {
        return this.selectedCellsList;
    }
    //endregion

    //region Методы

    override fun ExecuteCommand(command: Commands) {
        try {
            //this.currentCommand = command
            this.ShowDebug("execute command" + command.toString())
            if (command == Commands.Start) {
                this.Show("Игра началась!")
                this.game = Game()
                this.game.SetIsStarted(true)
                this.SetCurrentCommand(Commands.ShowBoard)
            }
            if (command == Commands.Exit) {
                this.game.SetIsStarted(false)
            }
            if (command == Commands.Save) {
                val fileChooser = JFileChooser()
                fileChooser.dialogTitle = "Сохранить файл"
                // Устанавливаем рабочий стол в качестве начальной директории
                val desktopPath = System.getProperty("user.home") + File.separator + "Desktop"
                fileChooser.currentDirectory = File(desktopPath)
                // Опционально: Если вы хотите, чтобы пользователь мог выбирать только определенные типы файлов, установите фильтр
                val filter = FileNameExtensionFilter("Текстовые файлы", "txt")
                fileChooser.fileFilter = filter

                val userSelection = fileChooser.showSaveDialog(null)

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    var fileToSave = fileChooser.selectedFile

                    // Проверка на наличие расширения ".txt"
                    if (!fileToSave.absolutePath.endsWith(".txt")) {
                        fileToSave = File(fileToSave.absolutePath + ".txt")
                    }

                    this.game.Save(fileToSave.absolutePath)
                }
                this.SetCurrentCommand(Commands.ShowBoard)
            }
            if (command == Commands.Load) {
                val fileChooser = JFileChooser()
                fileChooser.dialogTitle = "Открыть файл"
                // Устанавливаем рабочий стол в качестве начальной директории
                val desktopPath = System.getProperty("user.home") + File.separator + "Desktop"
                fileChooser.currentDirectory = File(desktopPath)
                // Опционально: Если вы хотите, чтобы пользователь мог выбирать только определенные типы файлов, установите фильтр
                val filter = FileNameExtensionFilter("Текстовые файлы", "txt")
                fileChooser.fileFilter = filter

                val userSelection = fileChooser.showOpenDialog(null)

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    val fileToLoad = fileChooser.selectedFile
                    println("Открыт файл: ${fileToLoad.absolutePath}")
                    // Теперь вы можете использовать fileToLoad для загрузки данных из вашего файла
                    this.game.Load(fileToLoad.absolutePath)
                    this.SetCurrentCommand(Commands.ShowBoard)
                }


            }
            if (command == Commands.GetCoordinate) {
                //io.Show("Введите координаты")
                //return io.GetCoordinates()
            }
            if (command == Commands.GetCell) {
//            io.Show("Введите координаты клетки")
//            val cord = io.GetCoordinates()
//            return this.board.GetCell(cord.first, cord.second)
            }
            if (command == Commands.GetChecker) {
//            io.Show("Введите координаты шашки")
//            val cord = io.GetCoordinates()
//            return this.board.GetCell(cord.first, cord.second).checker
            }
            if (command == Commands.MakeMove) {
                if (this.selectedCellsList.size != 2)
                    throw Exception("Невозможно сделать ход! Не выбрано две клетки на поле!")
                val checker =
                    this.game.GetCell(this.selectedCellsList[0].first, this.selectedCellsList[0].second).checker
                this.game.MakeMove(checker, this.selectedCellsList[1].first, this.selectedCellsList[1].second)
                this.game.UnSelectAll()
                this.SetCurrentCommand(Commands.ShowBoard)
            }
            if (command == Commands.ShowBoard) {
                this.Show("Ход игрока ${this.game.GetCurrentPlayer()}")
                this.game.SelectAvailableBeats(this.game.GetCheckersThatCanBeat())
                this.Show(this.game.GetBoard())

            }
            if (command == Commands.SelectChecker) {
                this.game.UnSelectAll()
                if (this.selectedCellsList.size == 1) {
                    val selectedCell = this.selectedCellsList[0];
                    this.game.SelectChecker(selectedCell.first, selectedCell.second)
                    this.SetCurrentCommand(Commands.ShowBoard)
                    //this.isReady = true
                    //this.Show(this.game.GetBoard())
                }


            }
            if (command == Commands.UnselectAll) {
                this.selectedCellsList.clear()
                this.game.UnSelectAll()
                this.SetCurrentCommand(Commands.ShowBoard)
            }

        } catch (ex: Exception) {
            ex.message?.let { this.Show(it) }
        }
    }

    override fun Start() {
        this.ExecuteCommand(Commands.Start)
        this.SetCurrentCommand(Commands.ShowBoard)
        while (this.game.GetIsStarted()) {
            if (this.isReady) {
                this.ExecuteCommand(this.GetCurrentCommand())
            }
            Thread.sleep(50);
        }
    }

    //region Действия меню
    fun StartNewGame() {
        this.SetCurrentCommand(Commands.Start)
    }

    fun LoadGame() {
        this.SetCurrentCommand(Commands.Load)
    }

    fun SaveGame() {
        this.SetCurrentCommand(Commands.Save)
    }

    fun Exit() {
        System.exit(0)
    }
    //endregion

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
        //this.pMain = this.game.ShowBoard() as JPanel
        this.lblStatus.text = message;
    }

    fun ShowDebug(message: String) {
        if (this.isDebug)
            this.Show(message)
    }

    //region Отрисовка доски
    private fun LeftButtonClick(button: JButton) {
        val clickedRow = button.getClientProperty("row") as Int
        val clickedCol = button.getClientProperty("col") as Int
        this.AddSelectedCell(clickedRow, clickedCol)
        this.ShowDebug("cell clicked " + clickedRow.toString() + clickedCol.toString())
        if (this.GetSelectedCells().size == 1) {
            this.SetCurrentCommand(Commands.SelectChecker)
        }
        if (this.GetSelectedCells().size == 2) {
            this.SetCurrentCommand(Commands.MakeMove)
        }
    }

    private fun RightButtonClick(button: JButton) {
        this.SetCurrentCommand(Commands.UnselectAll)
    }

    private fun CreateCellButton(cell: Cell): JButton {
        val button = JButton()
        button.putClientProperty("row", cell.row)
        button.putClientProperty("col", cell.column)
        button.preferredSize = Dimension(100, 100)  // Установите размер на свое усмотрение

        button.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                if (e.button == MouseEvent.BUTTON1) {  // Проверка на левую кнопку мыши
                    LeftButtonClick(button)
                } else if (e.button == MouseEvent.BUTTON3) {  // Проверка на правую кнопку мыши
                    RightButtonClick(button)
                }
            }
        })


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
            Selections.AvailableCellToBeat -> button.background = Color.ORANGE // Или другой цвет
        }

        return button
    }

    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     * @param isShowColumnsRowsNumbers Показывать номера столбцов и строк или нет.
     */
    override fun Show(board: Board, isShowColumnsRowsNumbers: Boolean) {
        // Удалите все компоненты из главной панели
        pMain.removeAll()
        val size = board.GetSize()
        // Установите GridLayout для pMain
        pMain.layout = GridLayout(size, size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                val cell = board.GetCell(i, j)
                val button = CreateCellButton(cell)
                pMain.add(button)
            }
        }

        // Обновите главную панель
        pMain.revalidate()
        pMain.repaint()
        this.isReady = false
    }
    //endregion


    //endregion


    //endregion

}
