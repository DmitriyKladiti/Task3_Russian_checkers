/**
 * Интерфейс для ввода-вывода.
 */
interface IO {

    //region Поля
    /**
     * Готов интерфейс или нет для отправки данных.
     *
     */
    val isReady: Boolean

    /**
     * Выбранные пользователем клетки
     */
    var selectedCellsList: ArrayList<Pair<Int, Int>>

    /**
     * Текущее сообщение
     */
    var currentMessage: String
    //endregion

    //region Методы

    /**
     * Добавляет выбранную клетку в список выбранных клеток на основе указанных координат.
     *
     * @param rowTo    Ряд, в который необходимо добавить выбранную клетку.
     * @param columnTo Столбец, в который необходимо добавить выбранную клетку.
     */
    fun AddSelectedCell(rowTo: Int, columnTo: Int)


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
    fun Show(board: Board, isShowColumnsRowsNumbers: Boolean = true): Any?
    //endregion

}
