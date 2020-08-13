import org.junit.*;
import org.junit.rules.*;
import java.io.*;
import static org.junit.Assert.*;

public class TestComputerMenacePlayer {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(30); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  @Test
  public void test_play_noSpotsLeft() {
    ComputerMenacePlayer p = new ComputerMenacePlayer(3, 3, 3);
    TicTacToe game = new TicTacToe(3,3,3);
    game.play(1);
    game.play(2);
    game.play(3);
    game.play(4);
    game.play(5);
    game.play(6);
    game.play(7);
    game.play(8);
    game.play(9);

    p.startGame(true);
    assertEquals(false, p.play(game));

    assertEquals(9, game.lastPlayedPosition);
  }

  @Test
  public void test_play_pickAvailableSpot() {
    ComputerMenacePlayer p = new ComputerMenacePlayer(3, 3, 3);
    TicTacToe game = new TicTacToe(3,3,3);
    game.play(1);
    game.play(2);
    game.play(3);
    game.play(4);
    game.play(5);
    game.play(6);
    game.play(8);
    game.play(7);

    p.startGame(true);
    assertEquals(true, p.play(game));
    assertEquals(9, game.lastPlayedPosition);
  }

  @Test
  public void test_play_learns_randomDoesNotLearn() {

    int improvement = 0;
    for (int i = 0; i < 10; i++) {
      Player student = new ComputerRandomPlayer();
      Player opponent = new ComputerPerfectPlayer(3, 3, 3);
      improvement += runExperiment(student, opponent);
    }

    assertEquals(true, improvement < 10);
  }

  @Test
  public void test_play_learns_menaceLearnsAgainstPerfectPlayer() {

    int improvement = 0;
    for (int i = 0; i < 10; i++) {
      Player student = new ComputerMenacePlayer(3, 3, 3);
      Player opponent = new ComputerPerfectPlayer(3, 3, 3);
      improvement += runExperiment(student, opponent);
    }

    assertEquals(true, improvement >= 20);
  }

  private int runExperiment(Player student, Player opponent) {
    GameState[] earlyGames = new GameState[10];
    for (int i=0; i < 10; i++) {
      TicTacToe game = new TicTacToe(3,3,3);
      GameMain.playGame(game, student, opponent);
      earlyGames[i] = game.gameState;
    }

    for (int i = 0; i < 500; i++) {
      TicTacToe game = new TicTacToe(3,3,3);
      GameMain.playGame(game, student, opponent);
    }

    GameState[] lateGames = new GameState[10];
    for (int i = 0; i < 10; i++) {
      TicTacToe game = new TicTacToe(3,3,3);
      GameMain.playGame(game, student, opponent);
      lateGames[i] = game.gameState;
    }

    int earlyWins = 0;
    int lateWins = 0;
    for (int i = 0; i < 10; i++) {
      earlyWins += inc(earlyGames[i]);
      lateWins += inc(lateGames[i]);
    }

    return lateWins - earlyWins;
  }

  private int inc(GameState state) {
    switch (state) {
      case XWIN:
        return 3;
      case DRAW:
        return 1;
      case OWIN:
      default:
        return 0;
    }
  }

}