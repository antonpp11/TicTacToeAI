import org.junit.*;
import org.junit.rules.*;
import java.io.*;

import java.util.Arrays;
import static org.junit.Assert.*;

public class TestOpenPositionsOnRotatedGame {

  @Rule
  public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

  @Rule
  public TestName testName = new TestName();

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Test
  public void testEmptyPositionsNewGame()
  {
    TicTacToe game = new TicTacToe(2,2,2);
    assertArrayEquals(new int[] {1,2,3,4}, game.emptyPositions());
  }

  @Test
  public void testEmptyPositionsGamePlayed()
  {
    TicTacToe game = new TicTacToe(2,2,2);

    game.play(2);
    assertArrayEquals(new int[] {1,3,4}, game.emptyPositions());

    game.play(1);
    assertArrayEquals(new int[] {3,4}, game.emptyPositions());

    game.play(4);
    assertArrayEquals(new int[] {3}, game.emptyPositions());

    game.play(3);
    assertArrayEquals(new int[0], game.emptyPositions());
  }

  @Test
  public void testEmptyPositionsRotated()
  {
    TicTacToe game = new TicTacToe(2,2,2);

    game.play(2);

    assertArrayEquals(new int[] {1,3,4}, game.emptyPositions());

    game.next();
    assertArrayEquals(new int[] {1,3,4}, game.emptyPositions());

    game.next();
    assertArrayEquals(new int[] {1,2,3}, game.emptyPositions());

    game.next();
    assertArrayEquals(new int[] {1,2,4}, game.emptyPositions());

    game.next();
    assertArrayEquals(new int[] {2,3,4}, game.emptyPositions());
  }

}