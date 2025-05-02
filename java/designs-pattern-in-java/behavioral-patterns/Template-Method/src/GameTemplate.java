public class GameTemplate 
{
    public static void main(String[] args) 
    {
        Game game = new Chess();
        game.play();
    }
}

abstract class Game 
{
    public Game(int numberOfPlayers) 
    {
        this.numberOfPlayers = numberOfPlayers;
    }

    protected int currentPlayer;
    protected final int numberOfPlayers;

    /**
     * Plays the game, starting from the initial state,
     * until the game is over, then announces the winner.
     */
    public void play() 
    {
        start();
        while (!isGameOver()) 
        {
            turnToNextPlayer();
        }
        System.out.println("Player " + getWinningPlayer() + " wins!");
    }

    protected abstract void start();
    protected abstract boolean isGameOver();
    protected abstract void turnToNextPlayer();
    protected abstract int getWinningPlayer();
}

class Chess extends Game 
{
    private final int maxTurns = 10;
    private int currentTurn = 1;

    public Chess() 
    {
        super(2);
    }

    /**
     * Starts a game of chess.
     * <p>
     * This implementation prints a message to the console
     * indicating that a game of chess has started.
     */
    @Override
    protected void start() 
    {
        System.out.println("Starting a game of chess.");
    }

    /**
     * Checks if the game of chess is over.
     * <p>
     * This method returns true if the current turn equals the maximum number 
     * of turns allowed in the game, indicating that the game has concluded.
     *
     * @return true if the game is over, false otherwise.
     */
    @Override
    protected boolean isGameOver() 
    {
        return currentTurn == maxTurns;
    }

    /**
     * Advances the game to the next player's turn.
     * <p>
     * This method increments the current turn counter and outputs a message to the console
     * indicating which player has taken their turn.
     */
    @Override
    protected void turnToNextPlayer() 
    {
        System.out.println("Turn " + (currentTurn++) + " taken by player " + currentPlayer);
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
    }

    /**
     * Determines the winning player in the game of chess.
     * <p>
     * This implementation returns the current player as the winner.
     *
     * @return the current player as the winning player.
     */
    @Override
    protected int getWinningPlayer() 
    {
        return currentPlayer;
    }
}
