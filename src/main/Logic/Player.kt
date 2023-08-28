import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class Player(
    var title: String = "Белый игрок",
    val color: Colors = Colors.White
) : Serializable {

    //region Методы
    override fun toString(): String {
        return """Игрок: '$title' Цвет: $color""".trimIndent()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (title != other.title) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }

    fun Copy(): Player {
        return Player(this.title, this.color)
    }


    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
    }
    //endregion

}