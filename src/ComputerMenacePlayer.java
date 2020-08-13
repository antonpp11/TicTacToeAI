import java.util.*;

public class ComputerMenacePlayer extends Player {

  // WRITE CODE HERE
  // Hint: if you need to track something about this player
  //       then you need instance variables
  private LinkedList<LinkedList<TicTacToe>> games;
  private LinkedList<LinkedList<MenaceGame>> winnable;
  private ComputerRandomPlayer backup;

  /**
  * A menace player needs to know the size of the game before starting
  * Only optimized for a 3x3 board
  *
  * @param aNumRows the number of lines in the game
  * @param aNumColumns the number of columns in the game
  * @param aSizeToWin the number of cells that must be aligned to win.
  */
  public ComputerMenacePlayer(int aNumRows, int aNumColumns, int aSizeToWin) {
    TicTacToeEnumerations enums = new TicTacToeEnumerations(aNumRows, aNumColumns, aSizeToWin);
    games = enums.generateAllGames();
    int numLevels = games.size();

    winnable = new LinkedList<LinkedList<MenaceGame>>();
    for (int i = 0; i < numLevels; i++) {
      LinkedList<MenaceGame> levelWinnable = new LinkedList<MenaceGame>();
      winnable.add(levelWinnable);
      for (TicTacToe game : games.get(i)) {
        levelWinnable.add(new MenaceGame(game));
      }
    }

    // Below here is what I couldn't manage to make work

    // for (int level = numLevels - 1; level >= 0; level-- ) {
    //   LinkedList<MenaceGame> levelWinnable = winnable.get(level);
    //   for (MenaceGame menace : winnable.get(level)) {
    //     if (menace.game.gameState == GameState.PLAYING) {
    //       for (int position : menace.game.emptyPositions()) {
    //         TicTacToe comparable = menace.game.cloneNextPlay(position);
    //         for (MenaceGame template : winnable.get(level + 1)) {
    //           if (template.game.equals(comparable)) {
    //             menace.setOutcome(template, position);
    //             break;
    //           }
    //         }
    //       }
    //     }
    //   }
    // }

  }

  // Hint: You might need to overwrite
  //       endGame to help tell your MenanceGame
  //       about the outcome of the game
  //       (so it can learn)

  public boolean play(TicTacToe game) {

    for (MenaceGame menaceGame : winnable.get(game.numRounds)) {
      if (menaceGame.game.alignAndEquals(game)) {

        int[] openSpots = menaceGame.game.emptyPositions();
        if (openSpots.length == 0) {
          return false;
        }

        int bestPosition = 0;
        int bestNumMoves = openSpots.length;
        GameOutcome bestOutcome = GameOutcome.UNKNOWN;


        // below here is what i couldn't get to compile

        // for (int position : openSpots) {
        //   int index = position - 1;
        //   int transformIndex = menaceGame.game.boardIndexes[index];
        //   int numMoves = menaceGame.moveRemainings[transformIndex];
        //   GameOutcome outcome = menaceGame.moveOutcomes[transformIndex];
        //
        //   boolean isBetterPosition = false;
        //
        //   // If we have no idea about the outcome then ignore it
        //   if (outcome.isBetter(bestOutcome)) {
        //     isBetterPosition = true;
        //
        //   // If we are going to lose, then picking the longest time to lose if best
        //   } else if (outcome == GameOutcome.LOSE && bestOutcome == GameOutcome.LOSE) {
        //     isBetterPosition = numMoves > bestNumMoves;
        //
        //   // If the number of moves is fewer than ou best number of moves
        //   // so far, then take it.  We either stop a on coming LOSS
        //   // or finding a better solution
        //   } else if (numMoves < bestNumMoves && outcome != GameOutcome.LOSE && outcome.asGoodOrBetter(bestOutcome)) {
        //     isBetterPosition = true;
        //
        //   }
        //
        //   if (isBetterPosition) {
        //     bestPosition = position;
        //     bestNumMoves = numMoves;
        //     bestOutcome = outcome;
        //   }
        // }

        if (bestPosition > 0) {
          game.play(bestPosition);
          return true;
        }
      }
    }

    // Nothing found, so play randomly
    return backup.play(game);
  }

}
