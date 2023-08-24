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
     * Запрашивает у пользователя целое число через консоль.
     * В случае ввода некорректного значения, метод повторно запрашивает число.
     *
     * @return Введенное пользователем целое число.
     */
    fun GetInt(): Int

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
    fun Show(message: String)

    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     * @param isShowColumnsRowsNumbers Показывать номера столбцов и строк или нет.
     */
    fun Show(board: Board, isShowColumnsRowsNumbers: Boolean)
}
