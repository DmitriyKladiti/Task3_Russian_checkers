import java.io.*

@Suppress("FunctionName")
class Game(private val io: IO) : Serializable {
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

    fun SelectChecker(row: Int, column: Int) {
        this.board.SelectChecker(row, column)

    }

    fun MakeMove(rowFrom: Int, columnFrom: Int, rowTo: Int, columnTo: Int) {
        //this.board.MoveChecker()
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
        io.Show("Выберите шашку")
        val cords = io.GetCoordinates()
        return try {
            this.GetChecker(cords.first, cords.second, this.GetCurrentPlayer())
        } catch (e: Exception) {
            e.message?.let { this.io.Show(it) }
            GetChecker()
        }
    }

    fun Start() {

        val command = io.ShowMainMenu()
        this.ExecuteCommand(command)
        if (this.isStarted) {
            io.Show("Ход игрока ${this.GetCurrentPlayer()}")
            io.Show(this.board, true)
            //TODO: добавить и проверить шашки,
            // которые могут походить с боем в данный момент. По правилам бой обязателен.
            if (this.currentChecker == null) {
                this.currentChecker = this.GetChecker()
                board.SelectChecker(this.currentChecker!!.row, this.currentChecker!!.column)
            } else {
                val availableMoves =
                    this.board.GetAvailableMoves(this.currentChecker!!.row, this.currentChecker!!.column)
                var availableBeats =
                    this.board.GetAvailableBeats(this.currentChecker!!.row, this.currentChecker!!.column)
                this.io.Show("Куда поставить шашку?")
                val cords = this.io.GetCoordinates()
                if (availableMoves.contains(cords)) {
                    // Координаты содержатся в списке доступных ходов
                    this.board.MoveChecker(
                        this.currentChecker!!.row, this.currentChecker!!.column,
                        cords.first, cords.second
                    )
                    this.currentChecker = null
                    this.GetNextPlayer()
                    this.board.UnselectAll()
                } else if (availableBeats.contains(cords)) {
                    val beatCheckers = this.board.GetCheckersBetween(
                        this.currentChecker!!.row, this.currentChecker!!.column,
                        cords.first, cords.second
                    )
                    this.board.MoveChecker(
                        this.currentChecker!!.row, this.currentChecker!!.column,
                        cords.first, cords.second
                    )
                    for (checkers in beatCheckers) {
                        this.board.RemoveChecker(checkers.row, checkers.column)
                    }
                    currentChecker = this.board.GetCell(cords.first, cords.second).checker
                    availableBeats = this.board.GetAvailableBeats(
                        this.currentChecker!!.row, this.currentChecker!!.column
                    )
                    if (availableBeats.isEmpty()) {
                        this.GetNextPlayer()
                    }
                    this.currentChecker = null
                    this.board.UnselectAll()
                } else {
                    // Координаты не содержатся в списке доступных ходов
                    this.io.Show("Недопустимый ход. Попробуйте снова.")
                }
            }
            io.Show(this.board, true)
        }
    }

    fun MakeMove() {

    }

    private fun ExecuteCommand(command: Commands) {
        if (command == Commands.Start) {
            io.Show("Игра началась!")
            this.isStarted = true
        }
        if (command == Commands.Exit) {
            this.isStarted = false
        }
        if (command == Commands.Save) {
            io.Show("Введите путь до файла")
            val filePath = io.GetStr()
            this.Save(filePath)
        }
        if (command == Commands.Load) {
            io.Show("Введите путь до файла")
            val filePath = io.GetStr()
            this.Load(filePath)
        }
    }

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

}