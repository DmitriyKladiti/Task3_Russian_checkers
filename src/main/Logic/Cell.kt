import Colors


/**
 * Клетка поля
 */
class Cell(
    var row: Int = 0,
    var column: Int = 0,
    var color: Colors = Colors.WHITE,
    var checker: Checker? = null
)