import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class GameTest {
    val filePath = "testGame.ser"
    private lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game()
        // Удаление файла, если он уже существует, чтобы начать с чистого состояния
        File(filePath).delete()
    }

    //region Проверка логики
    @Test
    fun testGetNextPlayer() {
        var currentPlayer = game.GetNextPlayer()
        assertEquals("Черный игрок", currentPlayer.title)
        assertEquals(Colors.Black, currentPlayer.color)
        currentPlayer = game.GetNextPlayer()
        assertEquals("Белый игрок", currentPlayer.title)
        assertEquals(Colors.White, currentPlayer.color)
    }

    @Test
    fun testGetCurrentPlayer() {
        val currentPlayer = game.GetCurrentPlayer()
        assertEquals("Белый игрок", currentPlayer.title)
    }

    @Test
    fun testMakeKing() {
        val board = Board()
        board.RemoveCheckers()
        board.AddChecker(1, 2, Checker(1, 2, Colors.White))
        board.AddChecker(6, 1, Checker(6, 1, Colors.Black))
        board.AddChecker(1, 4, Checker(1, 4, Colors.Black))
        this.game.SetBoard(board)
        this.game.Start()
        this.game.Save("C:\\Users\\Дмитрий\\Desktop\\testMakeKing.txt")
    }

    @Test
    fun testKingSmallCornerAttack() {
        val board = Board()
        board.RemoveCheckers()
        board.AddChecker(2, 1, Checker(2, 1, Colors.White))
        board.AddChecker(1, 2, Checker(1, 2, Colors.Black))
        board.AddChecker(1, 4, Checker(1, 4, Colors.Black))
        board.AddChecker(6, 1, Checker(6, 1, Colors.Black))
        this.game.SetBoard(board)
        this.game.Start()
        this.game.Save("C:\\Users\\Дмитрий\\Desktop\\testKingSmallCornerAttack.txt")
    }

    @Test
    fun testTwoKings() {
        val board = Board()
        board.RemoveCheckers()
        board.AddChecker(5, 2, Checker(5, 2, Colors.White,CheckerType.King))
        board.AddChecker(2, 5, Checker(2, 5, Colors.Black,CheckerType.King))
        this.game.SetBoard(board)
        this.game.Start()
        this.game.Save("C:\\Users\\Дмитрий\\Desktop\\testTwoKings.txt")
    }
    //endregion

    //region Сохранение/загрузка
    @Test
    fun testSaveAndLoad() {
        game.Save(filePath)
        // Удостоверимся, что файл действительно сохранен
        assertTrue(File(filePath).exists())

        // Загружаем игру в новый объект
        val loadedGame = Game()
        loadedGame.Load(filePath)

        // Теперь сравним каждое поле в originalGame и loadedGame
        assertEquals(game.GetIsStarted(), loadedGame.GetIsStarted())
        assertEquals(game.GetPlayerCurrentIndex(), loadedGame.GetPlayerCurrentIndex())
        assertEquals(game.GetPlayerCount(), loadedGame.GetPlayerCount())
        assertEquals(
            game.GetPlayers().toList(),
            loadedGame.GetPlayers().toList()
        ) // удостоверьтесь, что у Player есть метод equals
        assertEquals(game.GetBoard(), loadedGame.GetBoard()) // удостоверьтесь, что у Board есть метод equals
        assertEquals(
            game.GetCurrentChecker(),
            loadedGame.GetCurrentChecker()
        ) // удостоверьтесь, что у Checker есть метод equals
    }
    //endregion

}