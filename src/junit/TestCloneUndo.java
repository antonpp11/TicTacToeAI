import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.rules.TestName;
import org.junit.Rule;

import java.util.Arrays;
import static org.junit.Assert.*;

public class TestCloneUndo {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  @Before
  public void printTestMethod() {
    System.out.println("\t" + testName.getMethodName());
  }

  @Test
  public void testcloneUndoPlayInvalid() {
    TicTacToe game = new TicTacToe(2,2,2);
    assertEquals(null, game.cloneUndoPlay(99));
  }

  @Test
  public void testcloneUndoEmptySpot() {
    TicTacToe game = new TicTacToe(2,2,2);
    assertEquals(null, game.cloneUndoPlay(1));
  }

  @Test
  public void testcloneUndoWrongPiece() {
    TicTacToe game = new TicTacToe(2,2,2);
    game.play(1);
    game.play(2);
    game.play(3);

    assertEquals(GameState.XWIN, game.gameState);

    assertEquals(null, game.cloneUndoPlay(2));
  }

  @Test
  public void testcloneUndoLastMove() {
    TicTacToe game = new TicTacToe(2,2,2);
    game.play(1);
    game.play(2);
    game.play(3);

    assertEquals(GameState.XWIN, game.gameState);

    TicTacToe cloned = game.cloneUndoPlay(3);
    assertEquals(GameState.PLAYING, cloned.gameState);

    assertEquals(2, cloned.numRounds);
    assertEquals(0, cloned.lastPlayedPosition);
    assertEquals(CellValue.X, cloned.nextPlayer());

    assertEquals(CellValue.X, game.board[0]);
    assertEquals(CellValue.O, game.board[1]);
    assertEquals(CellValue.X, game.board[2]);
    assertEquals(CellValue.EMPTY, game.board[3]);

    assertEquals(CellValue.X, cloned.board[0]);
    assertEquals(CellValue.O, cloned.board[1]);
    assertEquals(CellValue.EMPTY, cloned.board[2]);
    assertEquals(CellValue.EMPTY, cloned.board[3]);
  }

  @Test
  public void testcloneUndoAnyValidMove() {
    TicTacToe game = new TicTacToe(2,2,2);
    game.play(1);
    game.play(2);
    game.play(3);

    assertEquals(GameState.XWIN, game.gameState);

    TicTacToe cloned = game.cloneUndoPlay(1);
    assertEquals(GameState.PLAYING, cloned.gameState);

    assertEquals(2, cloned.numRounds);
    assertEquals(0, cloned.lastPlayedPosition);
    assertEquals(CellValue.X, cloned.nextPlayer());

    assertEquals(CellValue.X, game.board[0]);
    assertEquals(CellValue.O, game.board[1]);
    assertEquals(CellValue.X, game.board[2]);
    assertEquals(CellValue.EMPTY, game.board[3]);

    assertEquals(CellValue.EMPTY, cloned.board[0]);
    assertEquals(CellValue.O, cloned.board[1]);
    assertEquals(CellValue.X, cloned.board[2]);
    assertEquals(CellValue.EMPTY, cloned.board[3]);
  }

}