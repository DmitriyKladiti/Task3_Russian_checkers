import java.lang.Exception

class Game(private val io: IO) {
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


    }

    fun MakeMove(rowFrom: Int, columnFrom: Int, rowTo: Int, columnTo: Int) {
        //this.board.MoveChecker()
    }

    fun GetChecker(row: Int, column: Int, player: Player): Checker {
        // Проверка координат
        if (!board.CheckCoordinate(row, column)) {
            throw IllegalArgumentException("Координаты за пределами доски!")
        }

        val checker = board.GetCell(row, column).checker

        // Проверка наличия шашки
        if (checker == null) {
            throw IllegalStateException("На данной клетке нет шашки!")
        }

        // Проверка соответствия цвета
        if (checker.color != player.color) {
            throw IllegalArgumentException("Цвет шашки не соответствует цвету игрока!")
        }

        return checker
    }

    fun GetChecker(): Checker {
        io.Show("Выберите шашку")
        var coords = io.GetCoordinates()
        try {
            return this.GetChecker(coords.first, coords.second, this.GetCurrentPlayer())
        } catch (e: Exception) {
            e.message?.let { this.io.Show(it) }
            return GetChecker()
        }
    }

    fun Start() {
        this.isStarted = true
        io.Show("Игра началась!")
        while (this.isStarted) {
            io.Show("Ход игрока ${this.GetCurrentPlayer()}")
            io.Show(this.board, true)
            //TODO: добавить и проверить шашки,
            // которые могут походить с боем в данный момент. По правилам бой обязателен.
            if (this.currentChecker == null) {
                this.currentChecker = this.GetChecker()
                board.SelectChecker(this.currentChecker!!.row, this.currentChecker!!.column)
            }
            else {
                var availableMoves =
                    this.board.GetAvailableMoves(this.currentChecker!!.row, this.currentChecker!!.column)
                var availableBeats =
                    this.board.GetAvailableBeats(this.currentChecker!!.row, this.currentChecker!!.column)
                this.io.Show("Куда поставить шашку?")
                var coords = this.io.GetCoordinates()
                if (availableMoves.contains(coords)) {
                    // Координаты содержатся в списке доступных ходов
                    this.board.MoveChecker(
                        this.currentChecker!!.row, this.currentChecker!!.column,
                        coords.first, coords.second
                    )
                    this.currentChecker = null
                    this.GetNextPlayer()
                    this.board.UnselectAll()
                } else if (availableBeats.contains(coords)) {
                    var beatCheckers = this.board.GetCheckersBetween(
                        this.currentChecker!!.row, this.currentChecker!!.column,
                        coords.first, coords.second
                    )
                    this.board.MoveChecker(
                        this.currentChecker!!.row, this.currentChecker!!.column,
                        coords.first, coords.second
                    )
                    for (checkers in beatCheckers) {
                        this.board.RemoveChecker(checkers.row, checkers.column)
                    }
                    currentChecker = this.board.GetCell(coords.first, coords.second).checker
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
}