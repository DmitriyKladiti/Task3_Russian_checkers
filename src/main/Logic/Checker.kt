class Checker(
    var row: Int = 0,
    var column: Int = 0,
    val color: Colors = Colors.White,
    var type: CheckerType = CheckerType.Checker
) {
    public fun Move(row: Int, column: Int) {
        this.row = row
        this.column = column
    }

    override fun toString(): String {
        if (this.type == CheckerType.Checker) {
            if (this.color == Colors.White)
                return "w"
            return "b"
        } else {
            if (this.color == Colors.White)
                return "W"
            return "B"
        }

    }
}