/**
 * Интерфейс для ввода-вывода.
 */
interface IO {
    /**
     * Получает строку.
     *
     * @return Строка.
     */
    fun GetStr(): String

    /**
     * Получает координаты.
     *
     * @return Пара координат (x, y).
     */
    fun GetCoordinates(): Pair<Int, Int>

    /**
     * Отображает сообщение.
     *
     * @param message Сообщение для отображения.
     */
    fun ShowMessage(message: String)

    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     */
    fun ShowBoard(board: Board)
}
