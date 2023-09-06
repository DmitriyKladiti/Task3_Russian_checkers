import java.io.*

@Suppress("FunctionName")
class Game : Serializable {
    //region Поля
    private var isStarted: Boolean = false

    private val playersCount: Int = 2
    private var playerCurrentIndex: Int = 0
    private var players: Array<Player> = Array(playersCount) { index ->
        when (index) {
            0 -> Player("Белый игрок", Colors.White)
            1 -> Player("Черный игрок", Colors.Black)
            else -> throw IllegalArgumentException("Недопустимый индекс игрока")
        }
    }
    private var board: Board = Board()
    private var currentChecker: Checker? = null

    //endregion

    //region Сеттеры
    fun SetIsStarted(isStarted: Boolean) {
        this.isStarted = isStarted
    }
    //endregion

    //region Геттеры
    fun GetIsStarted(): Boolean {
        return this.isStarted
    }

    fun GetPlayerCount(): Int {
        return this.playersCount
    }

    fun GetPlayerCurrentIndex(): Int {
        return this.playerCurrentIndex
    }

    fun GetPlayers(): Array<Player> {
        return this.players
    }

    fun GetBoard(): Board {
        return this.board
    }

    fun GetCurrentChecker(): Checker? {
        return this.currentChecker
    }
    //endregion

    //region Методы
    fun GetCurrentPlayer(): Player {
        if (this.playerCurrentIndex < 0 || this.playerCurrentIndex >= this.players.size) {
            throw IndexOutOfBoundsException("Недопустимый индекс текущего игрока: ${this.playerCurrentIndex}")
        }
        return this.players[this.playerCurrentIndex]
    }

    fun GetNextPlayer(): Player {
        this.playerCurrentIndex++
        this.playerCurrentIndex %= 2
        return this.GetCurrentPlayer()
    }

    private fun GetChecker(row: Int, column: Int, player: Player): Checker {
        // Проверка координат
        if (!board.CheckCoordinate(row, column)) {
            throw IllegalArgumentException("Координаты за пределами доски!")
        }

        val checker = board.GetCell(row, column).checker ?: throw IllegalStateException("На данной клетке нет шашки!")

        // Проверка наличия шашки

        // Проверка соответствия цвета
        if (checker.color != player.color) {
            throw IllegalArgumentException("Цвет шашки не соответствует цвету игрока!")
        }

        return checker
    }

    private fun GetChecker(): Checker {
//        io.Show("Выберите шашку")
//        val cords = io.GetCoordinates()
//        return try {
//            this.GetChecker(cords.first, cords.second, this.GetCurrentPlayer())
//        } catch (e: Exception) {
//            e.message?.let { this.io.Show(it) }
//            GetChecker()
//        }
        return Checker();
    }


    fun Start() {
//        this.ExecuteCommand(Commands.Start)
//        while (this.isStarted) {
//            if (io.isReady) {
//                io.Show("Ход игрока ${this.GetCurrentPlayer()}")
//                if (io.selectedCellsList.size == 1) {
//                    val selectedCell = io.selectedCellsList[0];
//                    this.SelectChecker(selectedCell.first, selectedCell.second)
//                }
//                if (io.selectedCellsList.size == 2) {
//
//                }
//                io.Show(board)
//            }
//        }
//        while (this.isStarted) {
//            //region Проверка на наличие шашек с доступной атакой
//            //TODO: добавить и проверить шашки,
//            // которые могут походить с боем в данный момент. По правилам бой обязателен.
////            for (i in 0 until this.board.GetSize()) {
////                for (j in 0 until this.board.GetSize()) {
////                    val checker = this.board.GetCell(i, j).checker
////                    if (checker != null && checker.color == this.GetCurrentPlayer().color) {
////                        val availableBeats = this.board.GetAvailableBeats(i, j)
////                        if (availableBeats.size > 0) {
////                            this.board.SelectCell(i, j, Selections.AvailableCheckerToBeat);
////                        }
////                    }
////                }
////            }
//            //endregion
//            io.Show(board)
//
//            //command = io.ShowMoveMenu()
//            //this.ExecuteCommand(command)
//        }
    }

    fun SelectChecker(row: Int, column: Int) {
        this.board.SelectChecker(row, column)
    }

    fun MakeMove() {
//        var cell: Cell? = null
//        io.Show("Ход игрока ${this.GetCurrentPlayer()}")
//
//        //region Запрос координат шашки
//        try {
//            this.currentChecker = this.ExecuteCommand(Commands.GetChecker) as Checker?
//            if (this.currentChecker == null)
//                throw Exception("На указанной клетке нет шашки!")
//            board.SelectChecker(this.currentChecker!!.row, this.currentChecker!!.column)
//            io.Show(board)
//        } catch (e: Exception) {
//            e.message?.let { this.io.Show(it) }
//            MakeMove()
//        }
//        //endregion
//        //region Запрос координат клетки
//        try {
//            cell = this.ExecuteCommand(Commands.GetCell) as Cell?
//        } catch (e: Exception) {
//            e.message?.let { this.io.Show(it) }
//            MakeMove()
//        }
//        //endregion
//        //region Логика хода
//        if (cell == null)
//            throw Exception("Не выбрана клетка!")
//        this.MakeMove(this.currentChecker, cell.row, cell.column)
//
//        this.currentChecker = null
//        this.GetNextPlayer()
//        this.board.UnselectAll()
//        io.Show(this.board, true)
//        //endregion
    }

    private fun MakeMove(checker: Checker?, rowTo: Int, columnTo: Int) {
        val availableMoves =
            this.board.GetAvailableMoves(checker!!.row, checker!!.column)
        var availableBeats =
            this.board.GetAvailableBeats(checker!!.row, checker!!.column)
        val cords = Pair<Int, Int>(rowTo, columnTo)
        if (availableMoves.contains(cords)) {
            // Координаты содержатся в списке доступных ходов
            this.board.MoveChecker(
                checker!!.row, checker!!.column,
                cords.first, cords.second
            )

        } else if (availableBeats.contains(cords)) {
            val beatCheckers = this.board.GetCheckersBetween(
                checker!!.row, checker!!.column,
                cords.first, cords.second
            )
            this.board.MoveChecker(
                checker!!.row, checker!!.column,
                cords.first, cords.second
            )
            for (checkers in beatCheckers) {
                this.board.RemoveChecker(checkers.row, checkers.column)
            }
            currentChecker = this.board.GetCell(cords.first, cords.second).checker
            availableBeats = this.board.GetAvailableBeats(
                checker!!.row, checker!!.column
            )
            if (availableBeats.isEmpty()) {
                this.GetNextPlayer()
            }
        } else {
            // Координаты не содержатся в списке доступных ходов
            //this.io.Show("Недопустимый ход. Попробуйте снова.")
        }
    }

    //region Сохранение/загрузка
    fun Save(filePath: String) {
        ObjectOutputStream(FileOutputStream(filePath)).use { outputStream ->
            outputStream.writeObject(this)
        }
    }

    private fun Copy(loadedGame: Game) {
        this.isStarted = loadedGame.isStarted
        this.playerCurrentIndex = loadedGame.playerCurrentIndex
        this.players = loadedGame.players.map { it.Copy() }.toTypedArray()
        this.board = loadedGame.board.Copy() // Предполагая, что у вас есть метод copy() в классе Board
        this.currentChecker =
            loadedGame.currentChecker?.Copy() // Предполагая, что у вас есть метод copy() в классе Checker
    }

    fun Load(filePath: String) {
        ObjectInputStream(FileInputStream(filePath)).use { inputStream ->
            val loadedGame = inputStream.readObject() as Game
            Copy(loadedGame)
        }
    }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
    }
    //endregion

    //endregion

}