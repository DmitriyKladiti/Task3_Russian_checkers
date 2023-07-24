import Colors.*

fun main(args: Array<String>) {
    var board = Board()
    println(board)
    println("___________________________________________________")
    board.MoveChecker(0,0,1,1)
    println(board)
//    var cell = board.Get(0,0)
//    for (i in 0..1024) {
//        println("$i: '${i.toChar()}'")
//    }
}