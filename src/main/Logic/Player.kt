class Player(
    var title: String = "Белый игрок",
    val color: Colors = Colors.White
) {
    override fun toString(): String {
        return """Игрок: '$title' Цвет: $color""".trimIndent()
    }
}