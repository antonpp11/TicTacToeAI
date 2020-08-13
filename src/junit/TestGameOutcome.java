import org.junit.*;
import org.junit.rules.*;
import java.io.*;

import java.util.Arrays;
import static org.junit.Assert.*;

public class TestGameOutcome {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Test
  public void test_isBetter() {
    assertEquals(false, GameOutcome.WIN.isBetter(GameOutcome.WIN));
    assertEquals(true, GameOutcome.WIN.isBetter(GameOutcome.DRAW));
    assertEquals(true, GameOutcome.WIN.isBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.WIN.isBetter(GameOutcome.UNKNOWN));

    assertEquals(false, GameOutcome.DRAW.isBetter(GameOutcome.WIN));
    assertEquals(false, GameOutcome.DRAW.isBetter(GameOutcome.DRAW));
    assertEquals(true, GameOutcome.DRAW.isBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.DRAW.isBetter(GameOutcome.UNKNOWN));

    assertEquals(false, GameOutcome.LOSE.isBetter(GameOutcome.WIN));
    assertEquals(false, GameOutcome.LOSE.isBetter(GameOutcome.DRAW));
    assertEquals(false, GameOutcome.LOSE.isBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.LOSE.isBetter(GameOutcome.UNKNOWN));

    assertEquals(false, GameOutcome.UNKNOWN.isBetter(GameOutcome.WIN));
    assertEquals(false, GameOutcome.UNKNOWN.isBetter(GameOutcome.DRAW));
    assertEquals(false, GameOutcome.UNKNOWN.isBetter(GameOutcome.LOSE));
    assertEquals(false, GameOutcome.UNKNOWN.isBetter(GameOutcome.UNKNOWN));
  }

  @Test
  public void test_asGoodOrBetter() {
    assertEquals(true, GameOutcome.WIN.asGoodOrBetter(GameOutcome.WIN));
    assertEquals(true, GameOutcome.WIN.asGoodOrBetter(GameOutcome.DRAW));
    assertEquals(true, GameOutcome.WIN.asGoodOrBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.WIN.asGoodOrBetter(GameOutcome.UNKNOWN));

    assertEquals(false, GameOutcome.DRAW.asGoodOrBetter(GameOutcome.WIN));
    assertEquals(true, GameOutcome.DRAW.asGoodOrBetter(GameOutcome.DRAW));
    assertEquals(true, GameOutcome.DRAW.asGoodOrBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.DRAW.asGoodOrBetter(GameOutcome.UNKNOWN));

    assertEquals(false, GameOutcome.LOSE.asGoodOrBetter(GameOutcome.WIN));
    assertEquals(false, GameOutcome.LOSE.asGoodOrBetter(GameOutcome.DRAW));
    assertEquals(true, GameOutcome.LOSE.asGoodOrBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.LOSE.asGoodOrBetter(GameOutcome.UNKNOWN));

    assertEquals(false, GameOutcome.UNKNOWN.asGoodOrBetter(GameOutcome.WIN));
    assertEquals(false, GameOutcome.UNKNOWN.asGoodOrBetter(GameOutcome.DRAW));
    assertEquals(false, GameOutcome.UNKNOWN.asGoodOrBetter(GameOutcome.LOSE));
    assertEquals(true, GameOutcome.UNKNOWN.asGoodOrBetter(GameOutcome.UNKNOWN));
  }

  @Test
  public void test_outcome_XWins_firstPlayer() {
    Player p = new HumanPlayer();
    p.startGame(true);
    GameOutcome outcome = p.endGame(GameState.XWIN);
    assertEquals(GameOutcome.WIN, outcome);
  }

  @Test
  public void test_outcome_XWins_secondPlayer() {
    Player p = new HumanPlayer();
    p.startGame(false);
    GameOutcome outcome = p.endGame(GameState.XWIN);
    assertEquals(GameOutcome.LOSE, outcome);
  }

  @Test
  public void test_outcome_OWins_firstPlayer() {
    Player p = new HumanPlayer();
    p.startGame(true);
    GameOutcome outcome = p.endGame(GameState.OWIN);
    assertEquals(GameOutcome.LOSE, outcome);
  }

  @Test
  public void test_outcome_OWins_secondPlayer() {
    Player p = new HumanPlayer();
    p.startGame(false);
    GameOutcome outcome = p.endGame(GameState.OWIN);
    assertEquals(GameOutcome.WIN, outcome);
  }

  @Test
  public void test_outcome_DRAW_anyPlayer() {
    Player p = new HumanPlayer();
    p.startGame(true);
    GameOutcome outcome = p.endGame(GameState.DRAW);
    assertEquals(GameOutcome.DRAW, outcome);

    p.startGame(false);
    outcome = p.endGame(GameState.DRAW);
    assertEquals(GameOutcome.DRAW, outcome);
  }

  @Test
  public void test_outcome_OtherStates_anyPlayer() {
    Player p = new HumanPlayer();
    p.startGame(true);
    GameOutcome outcome = p.endGame(GameState.PLAYING);
    assertEquals(GameOutcome.UNKNOWN, outcome);

    p.startGame(false);
    outcome = p.endGame(GameState.PLAYING);
    assertEquals(GameOutcome.UNKNOWN, outcome);
  }
}