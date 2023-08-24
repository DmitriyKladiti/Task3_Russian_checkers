import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameTest {
    private lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game(IOConsole())
    }

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
}