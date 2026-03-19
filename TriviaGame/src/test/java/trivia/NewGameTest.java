package trivia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NewGameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.add("Chet");
        game.add("Pat");
    }

    @Test
    void moveWrapsAroundBoard() {
        game.roll(11);
        assertEquals(12, game.getPlayers().get(0).getPlace());

        game.handleCorrectAnswer();
        game.handleCorrectAnswer();

        game.roll(1);
        assertEquals(1, game.getPlayers().get(0).getPlace());
    }

    @Test
    void wrongAnswerSendsToPenaltyBox() {
        game.wrongAnswer();
        assertTrue(game.getPlayers().get(0).isInPenaltyBox());
    }

    @Test
    void evenRollStaysInPenaltyBox() {
        game.wrongAnswer();
        game.handleCorrectAnswer();

        game.roll(2);
        assertFalse(game.isGettingOutOfPenaltyBox());

        game.handleCorrectAnswer();
        assertEquals(0, game.getPlayers().get(0).getCoins());
    }

    @Test
    void oddRollEscapesPenaltyBox() {
        game.wrongAnswer();
        game.handleCorrectAnswer();

        game.roll(3);

        game.handleCorrectAnswer();
        assertEquals(1, game.getPlayers().get(0).getCoins());
    }

    @Test
    void playerWinsAtSixCoins() {
        Player chet = game.getPlayers().get(0);
        for (int i = 0; i < 5; i++) {
            chet.addCoin();
        }

        game.roll(1);
        boolean gameContinues = game.handleCorrectAnswer();

        assertFalse(gameContinues);
        assertTrue(chet.hasWon());
    }

    @Test
    void correctCategoryAssignment() {
        game.roll(1);
        assertEquals("Science", game.currentCategory(game.getPlayers().get(0).getPlace()));

        game.handleCorrectAnswer();
        game.handleCorrectAnswer();

        game.roll(1);
        assertEquals("Sports", game.currentCategory(game.getPlayers().get(0).getPlace()));
    }
    @Test
    void addPlayerAfterGameStarted() {
        game.setGameStarted(true);
        boolean result = game.add("New Player");
        assertFalse(result, "Should not allow adding players after the game has started");
    }
}