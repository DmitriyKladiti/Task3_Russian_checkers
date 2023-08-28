import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Клетка поля
 */
class Cell(
    val row: Int = 0,
    val column: Int = 0,
    val color: Colors = Colors.White,
    var checker: Checker? = null,
    var selection: Selections = Selections.None,
    var displayContent: String? = null
) : Serializable {
    fun SetChecker(checker: Checker?) {
        this.checker = checker
        this.checker?.Move(this.row, this.column)
    }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
    }

    override fun toString(): String {
        if (this.checker != null) {
            return this.checker.toString()
        } else {
            if (this.color == Colors.White)
                return "_"
            if (this.color == Colors.Edge)
                return "E"
            return "#"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cell) return false

        if (row != other.row) return false
        if (column != other.column) return false
        if (color != other.color) return false
        if (checker != other.checker) return false
        if (selection != other.selection) return false
        if (displayContent != other.displayContent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        result = 31 * result + color.hashCode()
        result = 31 * result + (checker?.hashCode() ?: 0)
        result = 31 * result + selection.hashCode()
        result = 31 * result + (displayContent?.hashCode() ?: 0)
        return result
    }
}