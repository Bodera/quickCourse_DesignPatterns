import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BrokerChain {
    public static void main(String[] args)
    {
        Game game = new Game();
        Creature darkElf = new Creature(game, "Dark elf", 15, 20);

        System.out.println(darkElf);
        FireDamageModifier fireDamageModifier = new FireDamageModifier(game, darkElf);
        DoubleAttackModifier doubleAttackModifier = new DoubleAttackModifier(game, darkElf);

        try (fireDamageModifier; doubleAttackModifier) {
            System.out.println("Creature stats during battle: " + darkElf);
        }

        System.out.println("Updated creature stats after recovery: " + darkElf);
    }
}

class Game
{
    public Event<Query> queries = new Event<>();
}

class Creature
{
    private Game game;
    public String name;
    public int baseAttack, baseDefense;

    public Creature(Game game, String name, int baseAttack, int baseDefense)
    {
        this.game = game;
        this.name = name;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
    }

    int getAttack()
    {
        Query query = new Query(name, Query.Argument.ATTACK, baseAttack);
        game.queries.fire(query);
        return query.result;
    }

    int getDefense()
    {
        Query query = new Query(name, Query.Argument.DEFENSE, baseDefense);
        game.queries.fire(query);
        return query.result;
    }

    @Override
    public String toString()
    {
        return "Creature{" +
                "name='" + name + '\'' +
                ", attack=" + getAttack() +
                ", defense=" + getDefense() +
                '}';
    }
}

class Event<Args>
{
    private int index = 0;
    private Map<Integer, Consumer<Args>> subscribers = new HashMap<>();

    public int subscribe(Consumer<Args> consumer) 
    {
        int i = index++;
        subscribers.put(i, consumer);
        return i;
    }

    public void unsubscribe(int key) 
    {
        subscribers.remove(key);
    }

    public void fire(Args args) 
    {
        for (Consumer<Args> consumer : subscribers.values()) {
            consumer.accept(args);
        }
    }
}

class Query
{
    public String creatureName;

    enum Argument
    {
        ATTACK, DEFENSE;
    }

    public Argument argument;
    public int result;

    public Query(String creatureName, Argument argument, int result) 
    {
        this.creatureName = creatureName;
        this.argument = argument;
        this.result = result;
    }
}

// modifiers

class CreatureModifier
{
    protected Game game;
    protected Creature creature;

    public CreatureModifier(Game game, Creature creature)
    {
        this.game = game;
        this.creature = creature;
    }
}

class DoubleAttackModifier 
    extends CreatureModifier
    implements AutoCloseable
{
    private final int token;

    public DoubleAttackModifier(Game game, Creature creature) {
        super(game, creature);
        token = game.queries.subscribe(query -> {
            if (creature.name.equals(query.creatureName) 
                && query.argument == Query.Argument.ATTACK) 
            {
                query.result *= 2;
            }
        });
    }

    @Override
    public void close()
    {
        game.queries.unsubscribe(token);
    }
}

class FireDamageModifier 
    extends CreatureModifier
    implements AutoCloseable
{
    private final int token;

    public FireDamageModifier(Game game, Creature creature) {
        super(game, creature);
        token = game.queries.subscribe(query -> {
            if (creature.name.equals(query.creatureName) 
                && query.argument == Query.Argument.DEFENSE) 
            {
                query.result -= 3;
            }
        });
    }

    @Override
    public void close()
    {
        game.queries.unsubscribe(token);
    }
}