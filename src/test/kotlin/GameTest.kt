
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
        game = Game(IOConsole())
        // Удаление файла, если он уже существует, чтобы начать с чистого состояния
        File(filePath).delete()
    }

    //region Провекра логики
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
    //endregion

    //region Сохранение/загрузка
    @Test
    fun testSaveAndLoad() {
        game.Save(filePath)
        // Удостоверимся, что файл действительно сохранен
        assertTrue(File(filePath).exists())

        // Загружаем игру в новый объект
        val loadedGame =  Game(IOConsole())
        loadedGame.Load(filePath)

        // Теперь сравним каждое поле в originalGame и loadedGame
        assertEquals(game.GetIsStarted(), loadedGame.GetIsStarted())
        assertEquals(game.GetPlayerCurrentIndex(), loadedGame.GetPlayerCurrentIndex())
        assertEquals(game.GetPlayerCount(),loadedGame.GetPlayerCount())
        assertEquals(game.GetPlayers().toList(), loadedGame.GetPlayers().toList()) // удостоверьтесь, что у Player есть метод equals
        assertEquals(game.GetBoard(), loadedGame.GetBoard()) // удостоверьтесь, что у Board есть метод equals
        assertEquals(game.GetCurrentChecker(), loadedGame.GetCurrentChecker()) // удостоверьтесь, что у Checker есть метод equals
    }
    //endregion

}