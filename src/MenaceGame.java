public class MenaceGame {

  TicTacToe game;
  int[] boardOdds;
  int totalOdds;
  int currentPosition;

  /**
  * A menace game keeps an instance of a TicTacToe game
  * instead of extending it. We can chat about why
  * during our Monday meetups.
  *
  * Hint: Take a look at the implementation of a PerfectGame
  *       for ideas on how to structure a MenaceGame.
  */
  public MenaceGame(TicTacToe aGame) {

    game = aGame;
    currentPosition = 0;
    totalOdds = 0;
    boardOdds = new int[]{8, 8, 4, 4, 2, 2, 1, 1, 1};
    for (int i = 0; i < 9; i++) {
      if (game.board[i] != CellValue.EMPTY) {
        boardOdds[i] = 0;
      }
    }
    for (int i = 0; i < 9; i++) {
      totalOdds += boardOdds[i];
    }
  }

  /**
  * The game is over.
  * If you won, then add three beads to the current position's odds.
  * If you tied, only add 1 bead
  * If you lost, then remove a bead.
  *
  * Note: You can never have 0 beads in a game
  *       and do not forget to correctly update your totalOdds
  * @param outcome The outcome of the game.
  */
  public void setOutcome(GameOutcome outcome) {

    if (outcome == GameOutcome.WIN) {
      boardOdds[currentPosition - 1] += 3;
      totalOdds += 3;
    }
    else if (outcome == GameOutcome.DRAW) {
      boardOdds[currentPosition - 1] += 1;
      totalOdds += 1;
    }
    else if (outcome == GameOutcome.LOSE) {
      if (totalOdds > 1) {
        boardOdds[currentPosition - 1] += -1;
        totalOdds += -1;
      }
    }
    else {
      throw new IllegalStateException();
    }

  }

  /**
  * Roll the dice, and set the current position
  * If no positions are available, then return 0
  * (which is an invalid position)
  *
  * @return The current position
  */
  public int pickCurrentPosition() {

    currentPosition = calculatePosition(rollDice());
    return currentPosition;
  }

  /**
  * Generate a random number.
  *
  * Consider the following 3x3 board.
  *
  *    | X |
  * -----------
  *  O |   |
  * -----------
  *    |   |
  *
  * If we had the following beads (I left the Xs and Os off)
  *
  *  5 |  | 6
  * -----------
  *    | 1 | 1
  * -----------
  *  3 | 4 | 8
  *
  * Then our total odds are 28 (5+6+1+1+3+4+8) and we
  * want our random number generator to generate numbers
  * between 1 and 28.
  *
  * @return The random number rolled.
  */
  public int rollDice() {
    if (totalOdds > 1) {
      return (1 + (int)(Math.random() * totalOdds));
    }
    else {
      return 0;
    }
  }

  /**
  * Based on the diceRoll, calculate which position
  * on the board should be played based on the current odds (beads)
  * in each available cell.
  *
  * On a 3x3 board.
  *
  *    | X |
  * -----------
  *  O |   |
  * -----------
  *    |   |
  *
  * If we had the following beads (I left the Xs and Os off)
  *
  *  5 |  | 6
  * -----------
  *    | 1 | 1
  * -----------
  *  3 | 4 | 8
  *
  * Here are some expected outputs based on sample diceRolls
  *
  * diceRoll 3 => returns 1
  * diceRoll 11 => returns 3
  * diceRoll 12 => returns 5
  * diceRoll 14 => return 7
  *
  * @return int which position on the board should we choose
  */
  public int calculatePosition(int diceRoll) {
    if (diceRoll > totalOdds || diceRoll == 0) {
      return 0;
    }
    int sum = 0;
    for (int i = 0; i < boardOdds.length; i++) {
      sum += boardOdds[i];
      if (sum >= diceRoll) {
        return i + 1;
      }
    }
    return 0;

  }

  /**
  * Reset the odds back to an un-trained set based on
  * Menace algorithm.
  */
  public void resetOdds() {
    totalOdds = 0;
    boardOdds = new int[]{8, 8, 4, 4, 2, 2, 1, 1, 1};
    for (int i = 0; i < 9; i++) {
      if (game.board[i] != CellValue.EMPTY) {
        boardOdds[i] = 0;
      }
    }
    for (int i = 0; i < 9; i++) {
      totalOdds += boardOdds[i];
    }

  }



  public String toString() {
    StringBuilder b = new StringBuilder();
    int maxRowsIndex = game.numRows - 1;
    int maxColumnsIndex = game.numColumns - 1;

    String lineSeparator = Utils.repeat("---", game.numColumns) + Utils.repeat("-", game.numColumns - 1);

    b.append("POSITION: " + currentPosition + " (Odds " + totalOdds + ")\n");
    for (int i = 0; i < game.numRows; i++) {
      for (int j = 0; j < game.numColumns; j++) {
        int index = i * game.numColumns + j;

        b.append(" ");
        if (game.board[index] != CellValue.EMPTY) {
          b.append(game.board[index].toString());
        }
        else {
          b.append(boardOdds[index]);
        }
        b.append(" ");

        if (j < maxColumnsIndex) {
          b.append("|");
        }
      }

      // Line separator after each row, except the last
      if (i < maxRowsIndex) {
        b.append("\n");
        b.append(lineSeparator);
        b.append("\n");
      }
    }

    return b.toString();
  }

}
