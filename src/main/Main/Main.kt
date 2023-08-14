fun PrinAscii() {
    for (i in 0..1024) {
        println("$i: '${i.toChar()}'")
    }
}


fun main(args: Array<String>) {
    var io = IOConsole()
    var board = Board()
    board.RemoveCheckers()
    val row = 2
    val column = 3
    board.AddChecker(row, column, Checker(row, column, Colors.Black, CheckerType.King))
    board.SelectChecker(row, column)
    io.ShowBoard(board)

}