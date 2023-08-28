/**
 * Интерфейс для ввода-вывода.
 */
interface IO {
    /**
     * Готов интерфейс или нет для отправки данных.
     *
     */
    val isReady: Boolean

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
     * Выводит список на экран.
     *
     * @param options Список єлементов для вывода на экран.
     */
    fun Show(menuTitle: String, options: List<String>)

    /**
     * Выводит главное меню на экран.
     *
     */
    fun ShowMainMenu(): Commands

    /**
     * Выводит меню хода (показывается пере ходом игрока) на экран.
     *
     */
    fun ShowMoveMenu(): Commands

    /**
     * Отображает доску.
     *
     * @param board Доска для отображения.
     * @param isShowColumnsRowsNumbers Показывать номера столбцов и строк или нет.
     */
    fun Show(board: Board, isShowColumnsRowsNumbers: Boolean = true)
}
