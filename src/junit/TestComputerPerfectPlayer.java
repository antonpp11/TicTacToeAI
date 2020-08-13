import org.junit.*;
import org.junit.rules.*;
import java.io.*;
import static org.junit.Assert.*;

public class TestComputerPerfectPlayer {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(10); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  @Test
  public void test_pickWinner_wins() {
    ComputerPerfectPlayer p = new ComputerPerfectPlayer(3, 3, 3);
    TicTacToe game = new TicTacToe(3,3,3);
    game.play(1);
    game.play(4);
    game.play(5);
    game.play(3);

    p.startGame(true);

    p.play(game);

    assertEquals(9, game.lastPlayedPosition);
    assertEquals(GameState.XWIN, game.gameState);
  }

  @Test
  public void test_pickWinner_rotated() {
    ComputerPerfectPlayer p = new ComputerPerfectPlayer(3, 3, 3);

    TicTacToe game = new TicTacToe(3,3,3);
    game.play(4);
    game.play(7);
    game.play(6);
    game.play(9);
    game.play(2);

    p.startGame(false);

    assertEquals(true, p.play(game));

    assertEquals(GameState.OWIN, game.gameState);
    assertEquals(8, game.lastPlayedPosition);

    game = new TicTacToe(3,3,3);
    game.play(4);
    game.play(1);
    game.play(6);
    game.play(3);
    game.play(8);

    p.startGame(false);

    assertEquals(true, p.play(game));

    assertEquals(GameState.OWIN, game.gameState);
    assertEquals(2, game.lastPlayedPosition);
  }

  @Test
  public void test_pickWinner_goForDraw() {
    ComputerInOrderPlayer other = new ComputerInOrderPlayer();
    ComputerPerfectPlayer p = new ComputerPerfectPlayer(3, 3, 3);

    TicTacToe game = new TicTacToe(3,3,3);
    game.play(1);
    game.play(3);
    game.play(2);
    game.play(4);
    game.play(5);
    game.play(8);
    game.play(7);

    p.startGame(false);
    assertEquals(true, p.play(game));

    assertEquals(GameState.PLAYING, game.gameState);
    assertEquals(9, game.lastPlayedPosition);
  }

  @Test
  public void test_avoidLosing() {
    ComputerInOrderPlayer other = new ComputerInOrderPlayer();
    ComputerPerfectPlayer p = new ComputerPerfectPlayer(3, 3, 3);

    TicTacToe game = new TicTacToe(3,3,3);
    game.play(1);
    game.play(2);
    game.play(4);

    p.startGame(false);
    assertEquals(true, p.play(game));

    assertEquals(GameState.PLAYING, game.gameState);
    assertEquals(7, game.lastPlayedPosition);
  }

  @Test
  public void test_chooseLongerDrawThanLosing() {
    ComputerInOrderPlayer other = new ComputerInOrderPlayer();
    ComputerPerfectPlayer p = new ComputerPerfectPlayer(3, 3, 3);

    TicTacToe game = new TicTacToe(3,3,3);
    game.play(1);
    game.play(3);
    game.play(2);
    game.play(5);

    p.startGame(false);
    assertEquals(true, p.play(game));

    assertEquals(GameState.PLAYING, game.gameState);
    assertEquals(7, game.lastPlayedPosition);
  }

}