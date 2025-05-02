# Template Method

Both the strategy and template method patterns are about defining a skeleton algorithm with the details to be filled in by the implementer. The only difference is how they do it. In the case of the strategy design pattern what happens is you're allowed to plug in an object and there is an expectation on that objects interface, so that different parts of that object's interface are triggered, and you can sort of substitute one for another in the case of the dynamic strategy.

Now in the case of the template method something completely different happens because essentially instead of just plugging in an object what you do is you use inheritance, so you're expected to inherit from a particular algorithmic class. Let's explore that in more detail.

Suppose you want to model a board game between two or more people, we start defining our class `Game`. It won't hurt if we have a constructor where you can specify the number of players.

```java
abstract class Game 
{
    public Game(int numberOfPlayers) 
    {
        this.numberOfPlayers = numberOfPlayers;
    }

    protected int currentPlayer;
    protected final int numberOfPlayers;
}
```

Then we can start defining the skeleton algorithm for playing a game, and it doesn't matter which game it is because most of the games that we're going to talk about are all going to employ pretty much the same kind of algorithms. Let's define a method `play()` which is going to be the skeleton algorithm for playing a game.

```java
abstract class Game 
{
    // ... 

    public void play() 
    {
        start();
        while (!isGameOver()) 
        {
            turnToNextPlayer();
        }
        System.out.println("Player " + getWinningPlayer() + " wins!");
    }
}
```

So this is how the skeleton algorithm looks like. Now let's discover where do we define the `start()`, `isGameOver()`, `turnToNextPlayer()` and `getWinningPlayer()` methods. Once again, since we're in an abstract class we let the implementers take care of that.

```java
abstract class Game 
{
    // ... 

    protected abstract void start();
    protected abstract boolean isGameOver();
    protected abstract void turnToNextPlayer();
    protected abstract int getWinningPlayer();
}
```

Now if you want to make your own game what you do is you simply inherit from the game class, and then you implement these abstract methods here. So for example, if we're going to make a game of chess, we would define it like this:

```java
class Chess extends Game 
{
    @Override
    protected void start() 
    {
        // ...
    }

    @Override
    protected boolean isGameOver() 
    {
        // ...
    }

    @Override
    protected void turnToNextPlayer() 
    {
        // ...
    }

    @Override
    protected int getWinningPlayer() 
    {
        // ...
    }
}
```

Let's define some private members in our chess game class and also add a public constructor to it.

```java
class Chess extends Game 
{

    private final int maxTurns = 10;
    private int currentTurn = 1;

    public Chess() 
    {
        super(2);
    }

    // ...
}
```

For the sake of simplicity let's fill the overridden methods with some dummy code.

```java
class Chess extends Game 
{
    // ...

    @Override
    protected void start() 
    {
        System.out.println("Starting a game of chess.");
    }

    @Override
    protected boolean isGameOver() 
    {
        return currentTurn == maxTurns;
    }

    @Override
    protected void turnToNextPlayer() 
    {
        System.out.println("Turn " + (currentTurn++) + " taken by player " + currentPlayer);
        currentPlayer = (currentPlayer + 1) % numberOfPlayers;
    }

    @Override
    protected int getWinningPlayer() 
    {
        return currentPlayer;
    }
}
```

Now that we've all set up in our chess game class, let's create an instance of it and call the `play()` method on it.

```java
public class Demo
{
    public static void main(String[] args) 
    {
        Game game = new Chess();
        game.play();
    }
}
```

As an output we get:

```
Starting a game of chess.
Turn 1 taken by player 0
Turn 2 taken by player 1
Turn 3 taken by player 0
Turn 4 taken by player 1
Turn 5 taken by player 0
Turn 6 taken by player 1
Turn 7 taken by player 0
Turn 8 taken by player 1
Turn 9 taken by player 0
Player 1 wins!
```

This is how you implement the template method design pattern, nothing particularly sophisticated about it. Basically you have to define a base class as the skeleton for an algorithm, and then you let the implementers take care of the actual implementation details of the algorithm at the high level.
