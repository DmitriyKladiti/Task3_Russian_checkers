/**
 * Клетка поля
 */
class Cell(
    val row: Int = 0,
    val column: Int = 0,
    val color: Colors = Colors.White,
    var checker: Checker? = null
) {
    override fun toString(): String {
        if(this.checker!=null){
            return this.checker.toString()
        }
        else{
            if (this.color == Colors.White)
                return "_"
            return "#"
        }
    }
}