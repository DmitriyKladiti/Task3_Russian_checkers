/**
 * Этот перечисляемый тип представляет возможные команды, которые могут быть выполнены в рамках игры.
 * <p>
 * Команды следующие:
 * </p>
 * <ul>
 *     <li>{@link #Start} - для начала игры.</li>
 *     <li>{@link #Exit} - для выхода из игры.</li>
 *     <li>{@link #Save} - для сохранения текущего состояния игры.</li>
 *     <li>{@link #Load} - для загрузки сохраненного состояния игры.</li>
 *     <li>{@link #PrintString} - для вывода строки.</li>
 *     <li>{@link #GetString} - для получения строки от пользователя.</li>
 *     <li>{@link #GetCoordinate} - для получения координат от пользователя.</li>
 *     <li>{@link #GetCell} - для получения ячейки по определенным координатам.</li>
 *     <li>{@link #GetChecker} - для получения шашки по определенным координатам.</li>
 *     <li>{@link #MakeMove} - сделать ход шашкой..</li>
 * </ul>
 */
public enum Commands {
    /**
     * Начать новую игру.
     */
    Start,

    /**
     * Выйти из игры.
     */
    Exit,

    /**
     * Сохранить текущее состояние игры.
     */
    Save,

    /**
     * Загрузить сохраненное состояние игры.
     */
    Load,

    /**
     * Вывести строку на экран.
     */
    PrintString,

    /**
     * Получить строку от пользователя.
     */
    GetString,

    /**
     * Получить координаты от пользователя.
     */
    GetCoordinate,

    /**
     * Получить ячейку по определенным координатам.
     */
    GetCell,

    /**
     * Получить шашку по определенным координатам.
     */
    GetChecker,
    /**
     * Сделать ход шашкой.
     */
    MakeMove
}
