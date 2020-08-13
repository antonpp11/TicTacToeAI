import org.junit.*;
import org.junit.rules.*;
import java.io.*;

import java.util.Arrays;
import static org.junit.Assert.*;

public class TestMenaceGame {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Test
  public void test_constructor_setOdds_emptyGame() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    assertEquals(8, menace.boardOdds[0]);
    assertEquals(8, menace.boardOdds[1]);
    assertEquals(4, menace.boardOdds[2]);
    assertEquals(4, menace.boardOdds[3]);
    assertEquals(2, menace.boardOdds[4]);
    assertEquals(2, menace.boardOdds[5]);
    assertEquals(1, menace.boardOdds[6]);
    assertEquals(1, menace.boardOdds[7]);
    assertEquals(1, menace.boardOdds[8]);
  }

  @Test
  public void test_constructor_setOdds_partialGame() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(3);
    game.play(5);

    MenaceGame menace = new MenaceGame(game);

    assertEquals(0, menace.boardOdds[0]);
    assertEquals(8, menace.boardOdds[1]);
    assertEquals(0, menace.boardOdds[2]);
    assertEquals(4, menace.boardOdds[3]);
    assertEquals(0, menace.boardOdds[4]);
    assertEquals(2, menace.boardOdds[5]);
    assertEquals(1, menace.boardOdds[6]);
    assertEquals(1, menace.boardOdds[7]);
    assertEquals(1, menace.boardOdds[8]);
  }

  @Test
  public void test_setOutcome_win() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.boardOdds[0] = 3;
    menace.boardOdds[2] = 4;

    menace.currentPosition = 1;
    menace.setOutcome(GameOutcome.WIN);
    assertEquals(6, menace.boardOdds[0]);

    menace.currentPosition = 3;
    menace.setOutcome(GameOutcome.WIN);
    assertEquals(7, menace.boardOdds[2]);
  }

  @Test
  public void test_setOutcome_draw() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.boardOdds[0] = 3;
    menace.boardOdds[2] = 4;

    menace.currentPosition = 1;
    menace.setOutcome(GameOutcome.DRAW);
    assertEquals(4, menace.boardOdds[0]);

    menace.currentPosition = 3;
    menace.setOutcome(GameOutcome.DRAW);
    assertEquals(5, menace.boardOdds[2]);
  }

  @Test
  public void test_setOutcome_lose() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.boardOdds[0] = 3;
    menace.boardOdds[2] = 4;

    menace.currentPosition = 1;
    menace.setOutcome(GameOutcome.LOSE);
    assertEquals(2, menace.boardOdds[0]);

    menace.currentPosition = 3;
    menace.setOutcome(GameOutcome.LOSE);
    assertEquals(3, menace.boardOdds[2]);
  }

  @Test
  public void test_setOutcome_neverGoToZero() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.totalOdds = 1;
    menace.boardOdds[0] = 1;
    menace.boardOdds[1] = 0;
    menace.boardOdds[2] = 0;
    menace.boardOdds[3] = 0;
    menace.boardOdds[4] = 0;
    menace.boardOdds[5] = 0;
    menace.boardOdds[6] = 0;
    menace.boardOdds[7] = 0;
    menace.boardOdds[8] = 0;


    menace.currentPosition = 1;
    menace.setOutcome(GameOutcome.LOSE);
    assertEquals(1, menace.boardOdds[0]);

    menace.currentPosition = 2;
    menace.setOutcome(GameOutcome.LOSE);
    assertEquals(0, menace.boardOdds[1]);
  }

  @Test
  public void test_setOutcome_updateOverallSum() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.totalOdds = 100;
    menace.currentPosition = 1;

    menace.setOutcome(GameOutcome.WIN);
    assertEquals(103, menace.totalOdds);

    menace.setOutcome(GameOutcome.DRAW);
    assertEquals(104, menace.totalOdds);

    menace.setOutcome(GameOutcome.LOSE);
    assertEquals(103, menace.totalOdds);
  }

  @Test
  public void test_resetOdds() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(3);
    game.play(5);

    MenaceGame menace = new MenaceGame(game);

    assertEquals(0, menace.boardOdds[0]);
    assertEquals(8, menace.boardOdds[1]);
    assertEquals(0, menace.boardOdds[2]);
    assertEquals(4, menace.boardOdds[3]);
    assertEquals(0, menace.boardOdds[4]);
    assertEquals(2, menace.boardOdds[5]);
    assertEquals(1, menace.boardOdds[6]);
    assertEquals(1, menace.boardOdds[7]);
    assertEquals(1, menace.boardOdds[8]);

    // Change the board odds a bit
    menace.totalOdds = 126;
    menace.boardOdds[0] = 10;
    menace.boardOdds[1] = 11;
    menace.boardOdds[2] = 12;
    menace.boardOdds[3] = 13;
    menace.boardOdds[4] = 14;
    menace.boardOdds[5] = 15;
    menace.boardOdds[6] = 16;
    menace.boardOdds[7] = 17;
    menace.boardOdds[8] = 18;
    menace.currentPosition = 1;

    menace.resetOdds();

    assertEquals(0, menace.boardOdds[0]);
    assertEquals(8, menace.boardOdds[1]);
    assertEquals(0, menace.boardOdds[2]);
    assertEquals(4, menace.boardOdds[3]);
    assertEquals(0, menace.boardOdds[4]);
    assertEquals(2, menace.boardOdds[5]);
    assertEquals(1, menace.boardOdds[6]);
    assertEquals(1, menace.boardOdds[7]);
    assertEquals(1, menace.boardOdds[8]);
  }

  @Test
  public void test_toString_empty() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    String expected = "" +
      "POSITION: 0 (Odds 31)\n" +
      " 8 | 8 | 4 \n" +
      "-----------\n" +
      " 4 | 2 | 2 \n" +
      "-----------\n" +
      " 1 | 1 | 1 ";

    assertEquals(expected.trim(), menace.toString().trim());
  }

  @Test
  public void test_toString_showXsAndOs() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);

    MenaceGame menace = new MenaceGame(game);

    String expected = "" +
      "POSITION: 0 (Odds 11)\n" +
      " X | O | X \n" +
      "-----------\n" +
      " 4 | 2 | 2 \n" +
      "-----------\n" +
      " 1 | 1 | 1 ";

    assertEquals(expected.trim(), menace.toString().trim());
  }

  @Test
  public void test_toString_showRotated() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);

    game.next();
    game.next();

    MenaceGame menace = new MenaceGame(game);

    String expected = "" +
      "POSITION: 0 (Odds 11)\n" +
      " 1 | 4 | X \n" +
      "-----------\n" +
      " 1 | 2 | O \n" +
      "-----------\n" +
      " 1 | 2 | X ";

    assertEquals(expected.trim(), menace.toString().trim());
  }

  @Test
  public void test_toString_currentPosition() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.currentPosition = 4;

    String expected = "" +
      "POSITION: 4 (Odds 31)\n" +
      " 8 | 8 | 4 \n" +
      "-----------\n" +
      " 4 | 2 | 2 \n" +
      "-----------\n" +
      " 1 | 1 | 1 ";

    assertEquals(expected.trim(), menace.toString().trim());
  }

  @Test
  public void test_rollDice_between1AndTotalOdds() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.totalOdds = 10;
    for (int i = 0; i < 100; i++) {
      int random = menace.rollDice();
      assertEquals(true, random >= 1 && random <= 10);
    }

    menace.totalOdds = 15;
    for (int i = 0; i < 100; i++) {
      int random = menace.rollDice();
      assertEquals(true, random >= 1 && random <= 15);
    }
  }

  @Test
  public void test_rollDice_noMovesLeft() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);
    game.play(4);
    game.play(5);
    game.play(6);
    game.play(7);
    game.play(8);
    game.play(9);

    MenaceGame menace = new MenaceGame(game);

    for (int i = 0; i < 100; i++) {
      assertEquals(0, menace.rollDice());
    }
  }

  @Test
  public void test_rollDice_coverSpectrum() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.totalOdds = 10;
    int[] allOddsHit = new int[10];

    for (int i = 0; i < 1000; i++) {
      int random = menace.rollDice();
      allOddsHit[random - 1] += 1;
    }

    for (int numTimes : allOddsHit) {
      assertEquals(true, numTimes >= 3);
    }
  }

  @Test
  public void test_rollDice_allow() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    menace.totalOdds = 10;
    int[] allOddsHit = new int[10];

    for (int i = 0; i < 1000; i++) {
      int random = menace.rollDice();
      allOddsHit[random - 1] += 1;
    }

    for (int numTimes : allOddsHit) {
      assertEquals(true, numTimes >= 3);
    }
  }

  @Test
  public void test_calculatePosition_noneLeft() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);
    game.play(1);
    game.play(2);
    game.play(3);
    game.play(4);
    game.play(5);
    game.play(6);
    game.play(7);
    game.play(8);
    game.play(9);

    assertEquals(0, menace.calculatePosition(1));
  }

  @Test
  public void test_calculatePosition_invalidRoll() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);
    assertEquals(0, menace.calculatePosition(999));
  }

  @Test
  public void test_calculatePosition_doNotUpdateCurrentPosition() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    assertEquals(1, menace.calculatePosition(1));
    assertEquals(0, menace.currentPosition);
  }

  @Test
  public void test_calculatePosition_basedOnRoll() {
    TicTacToe game = new TicTacToe(3, 3, 3);
    MenaceGame menace = new MenaceGame(game);

    // First 8 go to position 1, as the odds are 8 out of 31
    assertEquals(1, menace.calculatePosition(1));
    assertEquals(1, menace.calculatePosition(8));

    // 8/31
    assertEquals(2, menace.calculatePosition(9));
    assertEquals(2, menace.calculatePosition(16));

    // 4/31
    assertEquals(3, menace.calculatePosition(17));
    assertEquals(3, menace.calculatePosition(20));

    // 4/31
    assertEquals(4, menace.calculatePosition(21));
    assertEquals(4, menace.calculatePosition(24));

    // 2/31
    assertEquals(5, menace.calculatePosition(25));
    assertEquals(5, menace.calculatePosition(26));

    // 2/31
    assertEquals(6, menace.calculatePosition(27));
    assertEquals(6, menace.calculatePosition(28));

    // 1/31 for the rest
    assertEquals(7, menace.calculatePosition(29));
    assertEquals(8, menace.calculatePosition(30));
    assertEquals(9, menace.calculatePosition(31));
  }

  @Test
  public void test_calculatePosition_onlyOpenSpots() {
    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(3);
    game.play(5);

    MenaceGame menace = new MenaceGame(game);

    // Odds are now out of 17
    assertEquals(0, menace.boardOdds[0]);
    assertEquals(8, menace.boardOdds[1]);
    assertEquals(0, menace.boardOdds[2]);
    assertEquals(4, menace.boardOdds[3]);
    assertEquals(0, menace.boardOdds[4]);
    assertEquals(2, menace.boardOdds[5]);
    assertEquals(1, menace.boardOdds[6]);
    assertEquals(1, menace.boardOdds[7]);
    assertEquals(1, menace.boardOdds[8]);

    // Position 1, 3 and 5 not available

    // First 8 go to position 2, as the odds are 8 out of 17
    assertEquals(2, menace.calculatePosition(1));
    assertEquals(2, menace.calculatePosition(8));

    // 4/17
    assertEquals(4, menace.calculatePosition(9));
    assertEquals(4, menace.calculatePosition(12));

    // 2/17
    assertEquals(6, menace.calculatePosition(13));
    assertEquals(6, menace.calculatePosition(14));

    // 1/17 for the rest
    assertEquals(7, menace.calculatePosition(15));
    assertEquals(8, menace.calculatePosition(16));
    assertEquals(9, menace.calculatePosition(17));
  }

  @Test
  public void test_pickCurrentPosition_rollAndFindNextPosition() {

    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(3);
    game.play(5);

    MenaceGame menace = new MenaceGame(game);

    for (int i = 0; i < 100; i++) {
      int position = menace.pickCurrentPosition();
      assertEquals(true, position != 1 || position != 3 || position != 5);
      assertEquals(position, menace.currentPosition);
    }
  }

  @Test
  public void test_pickCurrentPosition_noSpotsLeft() {

    TicTacToe game = new TicTacToe(3, 3, 3);

    game.play(1);
    game.play(2);
    game.play(3);
    game.play(4);
    game.play(5);
    game.play(6);
    game.play(7);
    game.play(8);
    game.play(9);

    MenaceGame menace = new MenaceGame(game);
    assertEquals(0, menace.pickCurrentPosition());
  }

}