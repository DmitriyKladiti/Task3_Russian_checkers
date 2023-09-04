import java.awt.BorderLayout
import javax.swing.*

class WindowBoard {
    private var pMain: JPanel = JPanel()
    private val io: IOGUI = IOGUI()
    private val game: Game = Game(this.io)
    private val lblStatus = JLabel("Состояние: Готово")

    init {
        this.game.Start()
        this.pMain = this.game.ShowBoard() as JPanel

        //region Меню
        // Создаем строку меню
        val menuBar = JMenuBar()

        // Создаем меню "Игра"
        val gameMenu = JMenu("Игра")

        // Создаем пункты меню
        val newGameItem = JMenuItem("Новая игра")
        val loadGameItem = JMenuItem("Загрузить")
        val saveGameItem = JMenuItem("Сохранить")
        val exitGameItem = JMenuItem("Выход")

        // Добавляем слушателей событий для пунктов меню
        newGameItem.addActionListener { this.StartNewGame() }
        loadGameItem.addActionListener { this.LoadGame() }
        saveGameItem.addActionListener { this.SaveGame() }
        exitGameItem.addActionListener { this.Exit() } // Закрыть программу

        // Добавляем пункты меню в меню "Игра"
        gameMenu.add(newGameItem)
        gameMenu.add(loadGameItem)
        gameMenu.add(saveGameItem)
        gameMenu.add(exitGameItem)

        // Добавляем меню "Игра" в строку меню
        menuBar.add(gameMenu)
        //endregion

        //region Создание окна
        // Добавляем строку меню в главный JFrame
        val frame = JFrame("Шашки")
        frame.jMenuBar = menuBar
        frame.setSize(800, 800)
        frame.layout = BorderLayout()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.add(pMain, BorderLayout.CENTER)
        frame.add(lblStatus, BorderLayout.SOUTH)  // Строка состояния
        frame.isVisible = true
        //endregion


    }

    //region Методы
    fun StartNewGame() {

    }

    fun LoadGame() {

    }

    fun SaveGame() {

    }

    fun Exit() {
        System.exit(0)
    }
    //endregion
}
