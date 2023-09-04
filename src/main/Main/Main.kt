fun SetChecker(board: Board, row: Int, column: Int, color: Colors, type: CheckerType) {
    val cell = board.GetCell(row, column)
    cell.checker = Checker(row, column, color, type)
}

fun SetCheckers(board: Board, coords: List<Pair<Int, Int>>, color: Colors, type: CheckerType) {
    for ((row, column) in coords) {
        SetChecker(board, row, column, color, type)
    }
}

fun TestCheckersMoves() {
//    var io = IOConsole()
//    var board = Board()
//    board.RemoveCheckers()
//
//    val blackCheckerCoordinates = listOf(
//        Pair(5, 4)
////        Pair(1, 6)
//    )
//    val whiteCheckerCoordinates = listOf(
////        Pair(6, 5),
////        Pair(6, 3),
//        Pair(4, 3),
//        Pair(4, 5)
////        Pair(1, 2)
//    )
//    SetCheckers(board, blackCheckerCoordinates, Colors.Black, CheckerType.King)
//    SetCheckers(board, whiteCheckerCoordinates, Colors.White, CheckerType.Checker)
//
//    board.SelectChecker(5, 4)
////    board.SelectChecker(1, 6)
////    board.SelectChecker(1, 2)
//    io.Show(board, true)
}

fun TestGame() {
//    var game = Game(IOConsole())
//    val filePath = "game.ser"
//    // Сохранение игры в файл
//    game.Save(filePath)
//    // Загрузка игры из файла
//    val loadedGame = game.Load(filePath)
//    game.Start()
}

fun TestWindowBoard() {
    val windowBoard = WindowBoard()
}

fun main(args: Array<String>) {
    //TestGame()
    TestWindowBoard()

}