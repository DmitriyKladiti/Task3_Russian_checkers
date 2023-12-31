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
            else -> throw IllegalArgumentException(Strings.Companion.Errors.incorrectPlayerIndex)
        }
    }
    private var board: Board = Board()
    private var currentChecker: Checker? = null
    private var currentGameSate: GameState = GameState.IsStarted
    //endregion

    //region Сеттеры
    fun SetIsStarted(isStarted: Boolean) {
        this.isStarted = isStarted
    }

    fun SetBoard(board: Board) {
        this.board = board
    }
    //endregion

    //region Геттеры
    fun GetGameState(): GameState {
        return this.currentGameSate;
    }

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

    fun GetCell(row: Int, column: Int): Cell {
        return this.board.GetCell(row, column)
    }

    fun UnSelectAll() {
        this.board.UnselectAll()
    }

    fun GetCurrentPlayer(): Player {
        if (this.playerCurrentIndex < 0 || this.playerCurrentIndex >= this.players.size) {
            throw IndexOutOfBoundsException("${Strings.Companion.Errors.incorrectPlayerIndex}: ${this.playerCurrentIndex}")
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
            throw IllegalArgumentException(Strings.Companion.Errors.cordsOutOfBounds)
        }

        val checker =
            board.GetCell(row, column).checker ?: throw IllegalStateException(Strings.Companion.Errors.noCheckerInCell)

        // Проверка наличия шашки

        // Проверка соответствия цвета
        if (checker.color != player.color) {
            throw IllegalArgumentException(Strings.Companion.Errors.colorPlayerCheckerMismatch)
        }

        return checker
    }

    fun Start() {
        this.SetIsStarted(true)
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
        val checker = this.GetChecker(row, column, this.GetCurrentPlayer())
        val checkersCanBeat = this.GetCheckersThatCanBeat()
        if (checkersCanBeat.isNotEmpty() && !checkersCanBeat.contains(this.GetCell(row, column)))
            throw Exception(Strings.Companion.Errors.notBeatCheckerSelected)
        this.board.SelectChecker(checker.row, checker.column)
    }

    // Метод возвращает список шашек, которые могут бить
    fun GetCheckersThatCanBeat(): List<Cell> {
        val checkersToBeat = mutableListOf<Cell>()

        for (i in 0 until this.board.GetSize()) {
            for (j in 0 until this.board.GetSize()) {
                val checker = this.board.GetCell(i, j).checker
                if (checker != null && checker.color == this.GetCurrentPlayer().color) {
                    val availableBeats = this.board.GetAvailableBeats(i, j)
                    if (availableBeats.isNotEmpty()) {
                        checkersToBeat.add(this.GetCell(i, j))
                    }
                }
            }
        }

        return checkersToBeat
    }

    /**
     * Выделяет клетки для атаки для текущего игрока
     */
    fun SelectAvailableBeats(checkers: List<Cell>) {
        for (checker in checkers) {
            this.board.SelectCell(checker.row, checker.column, Selections.AvailableCheckerToBeat)
            val availableBeats = this.board.GetAvailableBeats(checker.row, checker.column)
            for (cell in availableBeats) {
                this.board.SelectCell(cell.first, cell.second, Selections.AvailableCellToBeat)
            }
        }
    }

    fun CheckGameOver(): GameState {
        // Проверка на количество шашек каждого игрока
        val whiteCheckers = board.GetCheckersByColor(Colors.White)
        val blackCheckers = board.GetCheckersByColor(Colors.Black)

        if (whiteCheckers.isEmpty()) {
            return GameState.BlackWins
        }

        if (blackCheckers.isEmpty()) {
            return GameState.WhiteWins
        }

        // Проверка на доступные ходы или атаки для текущего игрока
        val currentPlayerCheckers = if (this.GetCurrentPlayer().color == Colors.White) whiteCheckers else blackCheckers
        for (checker in currentPlayerCheckers) {
            if (board.GetAvailableMoves(checker.row, checker.column).isNotEmpty() ||
                board.GetAvailableBeats(checker.row, checker.column).isNotEmpty()
            ) {
                // Есть доступные ходы или атаки
                return GameState.IsStarted
            }
        }

        // Если у текущего игрока нет доступных ходов или атак, другой игрок победил
        return if (this.GetCurrentPlayer().color == Colors.White) GameState.BlackWins else GameState.WhiteWins
    }

    fun MakeMove(checker: Checker?, rowTo: Int, columnTo: Int) {
        if (checker != null) {
            if (checker.color != this.GetCurrentPlayer().color)
                throw Exception(Strings.Companion.Errors.colorPlayerCheckerMismatch)
            val checkersCanBeat = this.GetCheckersThatCanBeat()
            if (checkersCanBeat.isNotEmpty() && !checkersCanBeat.contains(this.GetCell(checker.row, checker.column)))
                throw Exception(Strings.Companion.Errors.notBeatCheckerSelected)
        }
        val availableMoves =
            this.board.GetAvailableMoves(checker!!.row, checker!!.column)
        var availableBeats =
            this.board.GetAvailableBeats(checker!!.row, checker!!.column)
        val cords = Pair<Int, Int>(rowTo, columnTo)
        if (availableMoves.contains(cords) && availableBeats.isEmpty()) {
            // Координаты содержатся в списке доступных ходов
            this.board.MoveChecker(
                checker!!.row, checker!!.column,
                cords.first, cords.second
            )

            this.GetNextPlayer()
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
            throw Exception(Strings.Companion.Errors.incorrectMove)
        }
        this.currentGameSate = this.CheckGameOver()
        if (this.currentGameSate == GameState.Draw ||
            this.currentGameSate == GameState.BlackWins ||
            this.currentGameSate == GameState.WhiteWins
        ) {
            this.isStarted = false;
        }
    }

    fun MakeKing(row: Int, column: Int) {
        this.board.MakeKing(row, column)
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