import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class Checker(
    var row: Int = 0,
    var column: Int = 0,
    val color: Colors = Colors.White,
    var type: CheckerType = CheckerType.Checker
) : Serializable {

    //region Методы
    public fun Move(row: Int, column: Int) {
        this.row = row
        this.column = column
    }

    fun Copy(): Checker {
        return Checker(
            row = this.row,
            column = this.column,
            color = this.color,
            type = this.type
        )
    }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Checker) return false

        if (row != other.row) return false
        if (column != other.column) return false
        if (color != other.color) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        result = 31 * result + color.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
    //endregion

}