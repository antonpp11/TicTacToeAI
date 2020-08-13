import org.junit.*;
import org.junit.rules.*;
import java.io.*;

import java.util.Arrays;
import static org.junit.Assert.*;

public class TestPerfectGame {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Test
  public void test_setOutcome_GameState_basedOnNextPlayer() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    PerfectGame perfect = new PerfectGame(game);

    game.play(1);
    assertEquals(CellValue.O, game.nextPlayer());

    // The outcome is based on the next player
    // so if we decide that X just won and it's Os turn
    // then this game is a "LOSE"
    perfect.setOutcome(GameState.XWIN);
    assertEquals(GameOutcome.LOSE, perfect.overallOutcome);
    assertEquals(0, perfect.overallRemaining);

    // And if "OWIN" and it's Os turn, then it's a WIN
    perfect.setOutcome(GameState.OWIN);
    assertEquals(GameOutcome.WIN, perfect.overallOutcome);
    assertEquals(0, perfect.overallRemaining);
  }

  @Test
  public void test_setOutcome_GameState_aDrawIsADraw() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    PerfectGame perfect = new PerfectGame(game);

    game.play(1);

    perfect.setOutcome(GameState.DRAW);
    assertEquals(GameOutcome.DRAW, perfect.overallOutcome);
    assertEquals(0, perfect.overallRemaining);

    game.play(2);

    perfect.setOutcome(GameState.DRAW);
    assertEquals(GameOutcome.DRAW, perfect.overallOutcome);
    assertEquals(0, perfect.overallRemaining);
  }

  @Test
  public void test_setOutcome_NextMove() {
    TicTacToe nextGame = new TicTacToe(3, 3, 3);
    PerfectGame nextMove = new PerfectGame(nextGame);
    nextMove.setOutcome(GameState.XWIN);

    TicTacToe thisGame = new TicTacToe(3, 3, 3);
    PerfectGame thisPerfect = new PerfectGame(thisGame);

    // We will look at the next move, which is a "WIN"
    // That means this move is a "LOSE" (because we swap people)
    thisPerfect.setOutcome(nextMove, 3);
    assertEquals(GameOutcome.LOSE, thisPerfect.overallOutcome);
    assertEquals(1, thisPerfect.overallRemaining);

    // Also track the individual moves and what they represent
    assertEquals(GameOutcome.LOSE, thisPerfect.moveOutcomes[2]);
    assertEquals(1, thisPerfect.moveRemainings[2]);
  }

  @Test
  public void test_toString_empty() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    PerfectGame perfect = new PerfectGame(game);

      String expected = "" +
        "OVERALL: UNKNOWN(0)\n" +
        "   |   |   \n" +
        "-----------\n" +
        "   |   |   \n" +
        "-----------\n" +
        "   |   |   ";

      assertEquals(expected.trim(), perfect.toString().trim());
  }

  @Test
  public void test_toString_showXsAndOs() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);

    PerfectGame perfect = new PerfectGame(game);

      String expected = "" +
        "OVERALL: UNKNOWN(0)\n" +
        " X | O | X \n" +
        "-----------\n" +
        "   |   |   \n" +
        "-----------\n" +
        "   |   |   ";

      assertEquals(expected.trim(), perfect.toString().trim());
  }

  @Test
  public void test_toString_showRotated() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);

    game.next();
    game.next();

    PerfectGame perfect = new PerfectGame(game);

      String expected = "" +
        "OVERALL: UNKNOWN(0)\n" +
        "   |   | X \n" +
        "-----------\n" +
        "   |   | O \n" +
        "-----------\n" +
        "   |   | X ";

      assertEquals(expected.trim(), perfect.toString().trim());
  }

  @Test
  public void test_toString_showWinLose() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);


    game.next();
    game.next();

    PerfectGame perfect = new PerfectGame(game);

      String expected = "" +
        "OVERALL: UNKNOWN(0)\n" +
        "   |   | X \n" +
        "-----------\n" +
        "   |   | O \n" +
        "-----------\n" +
        "   |   | X ";

      assertEquals(expected.trim(), perfect.toString().trim());
  }

  @Test
  public void test_toString_showWinLossDrawInfo() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    PerfectGame perfect = new PerfectGame(game);

    game.play(1);
    game.play(2);
    game.play(3);

    perfect.overallOutcome = GameOutcome.WIN;
    perfect.overallRemaining = 0;

    perfect.moveOutcomes[5] = GameOutcome.WIN;
    perfect.moveOutcomes[6] = GameOutcome.WIN;
    perfect.moveOutcomes[7] = GameOutcome.LOSE;
    perfect.moveOutcomes[8] = GameOutcome.DRAW;

    perfect.moveRemainings[5] = 4;
    perfect.moveRemainings[6] = 1;
    perfect.moveRemainings[7] = 2;
    perfect.moveRemainings[8] = 3;

      String expected = "" +
        "OVERALL: WIN(0)\n" +
        " X | O | X \n" +
        "-----------\n" +
        "   |   | W 4 \n" +
        "-----------\n" +
        " W 1 | L 2 | D 3 ";

      assertEquals(expected.trim(), perfect.toString().trim());
  }

}