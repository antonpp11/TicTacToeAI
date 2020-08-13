import org.junit.*;
import org.junit.rules.*;
import java.io.*;

import java.util.Arrays;
import static org.junit.Assert.*;

public class TestTrackWinsLosesDraws {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Test
  public void test_noGamesPlayed() {
    Player p = new HumanPlayer();

    String expected = "This player has won 0 games, lost 0 games, and 0 were draws.";
    assertEquals(expected, p.toString());
  }

  @Test
  public void test_oneWin() {
    Player p = new HumanPlayer();

    p.startGame(true);
    p.endGame(GameState.XWIN);

    String expected = "This player has won 1 games, lost 0 games, and 0 were draws.";
    assertEquals(expected, p.toString());
  }

  @Test
  public void test_oneLoss() {
    Player p = new HumanPlayer();

    p.startGame(false);
    p.endGame(GameState.XWIN);

    String expected = "This player has won 0 games, lost 1 games, and 0 were draws.";
    assertEquals(expected, p.toString());
  }

  @Test
  public void test_oneDraw() {
    Player p = new HumanPlayer();

    p.startGame(false);
    p.endGame(GameState.DRAW);

    String expected = "This player has won 0 games, lost 0 games, and 1 were draws.";
    assertEquals(expected, p.toString());
  }

  @Test
  public void test_manyGamesLessThanWindow() {
    Player p = new HumanPlayer();

    // record 11 wins as X
    for (int i=0; i<11; i++) {
      p.startGame(true);
      p.endGame(GameState.XWIN);
    }

    // record 10 wins as O
    for (int i=0; i<10; i++) {
      p.startGame(false);
      p.endGame(GameState.OWIN);
    }

    // record 9 loses as X
    for (int i=0; i<9; i++) {
      p.startGame(true);
      p.endGame(GameState.OWIN);
    }

    // record 10 loses as O
    for (int i=0; i<10; i++) {
      p.startGame(false);
      p.endGame(GameState.XWIN);
    }

    // record 10 ties
    for (int i=0; i<10; i++) {
      p.startGame(i % 2 == 0);
      p.endGame(GameState.DRAW);
    }

    String expected = "This player has won 21 games, lost 19 games, and 10 were draws.";
    assertEquals(expected, p.toString());
  }

  @Test
  public void test_manyGamesMoreThanWindow() {
    Player p = new HumanPlayer();

    // record 50 wins as X
    for (int i=0; i<50; i++) {
      p.startGame(true);
      p.endGame(GameState.XWIN);
    }

    // record 11 wins as O
    for (int i=0; i<11; i++) {
      p.startGame(false);
      p.endGame(GameState.OWIN);
    }

    // record 9 loses as O
    for (int i=0; i<9; i++) {
      p.startGame(false);
      p.endGame(GameState.XWIN);
    }

    // record 5 ties
    for (int i=0; i<5; i++) {
      p.startGame(i % 2 == 0);
      p.endGame(GameState.DRAW);
    }

    String expected = "This player has won 61 games, lost 9 games, and 5 were draws. Over the last 50 games, this player has won 36, lost 9, and tied 5.";
    assertEquals(expected, p.toString());
  }

}