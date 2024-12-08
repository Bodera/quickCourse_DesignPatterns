# Chain of Responsibility

## Method chain

What we're now going to implement is the simplest and most popular kind of example of a chain of responsibility so called the *method chain*. Basically setting up methods, so that one method calls the entire chain of methods.

Let's imagine we're once again making a computer game. On this game we define a `Creature` that's kind of just roaming the grounds, and it has some proper attributes.

```java
class Creature
{
    public String name;
    public int attack, defense, health;

    public Creature(String name, int attack, int defense)
    {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    @Override
    public String toString()
    {
        return "Creature{" +
                "name='" + name + '\'' +
                ", attack=" + attack +
                ", defense=" + defense +
                '}';
    }
}
```

Now let's suppose the creature goes around and keeps finding magic objects which improve its `attack` and `defense` values, or maybe the creature encounters a witch that cast some sort of spell which changes the creature's `attack` and `defense` values, increasing or decreasing them.

The question is, how if you have more than one modifier? Suppose the creature gets a bonus to attack from a magic sword, but it gets cursed, and the attack value goes down. The question remains, how do you set up all the modifications? How do you calculate all changes on the `Creature` values?

Well we start by defining a class called `CreatureModifier` that will for sure keep a reference to the `Creature` that we're going to modify. Keep in mind that `CreatureModifier's` can actually be chained together, so we can have several instances of `CreatureModifier` being applied to the same `Creature`, and in order to build this chain we're also going to store a reference to the next `CreatureModifier`.

So we have our `CreatureModifier`, and we have a way of navigating to whatever the next `CreatureModifier` is, and of course it can be null indicating no extra modifier on the end of the chain.

Now let's make a constructor which initializes just the `Creature`, and then let's have some sort of functionality for actually incorporating a new `CreatureModifier` towards the end of the chain.

```java
class CreatureModifier
{
    protected Creature creature;
    protected CreatureModifier next;

    public CreatureModifier(Creature creature)
    {
        this.creature = creature;
    }

    public void setNext(CreatureModifier next)
    {
        if (this.next != null) 
            this.next.setNext(next);
        else 
            this.next = next;
    }

    public void handle()
    {
        if (next != null) next.handle();
    }
}
```

Here on the `setNext()` method we basically stated that if our current `CreatureModifier` doesn't hold a reference to other modifier than a new one is appended to the end of the chain. However, in case the current `CreatureModifier` already has a valid reference to another modifier that means we already have established a chain following the current `CreatureModifier`, and it can go indefinitely, that's why we access the `next` modifier and calls its `setNext()` instead. So this becomes a recursive call because effectively you're calling it on the `next` object, and if it also has a reference to another `CreatureModifier` you're calling it indefinitely until you reach the end of the chain.

This is how we set it up, now the question is how do you actually apply all those modifications one after another in this big chain to the `Creature`? That's where the `handle()` method comes in, by checking if the modifier is available then what we're just going to do call `handle()`.

You'll notice that since `CreatureModifier` is a base class we're not performing any modifications on the `Creature` ourselves. However, once again we're following the entire chain, and calls the `handle()` method on each chain link.

So the idea of this set up is obviously for you to inherit from `CreatureModifier` and to provide actual functionality in the `handle()` method.

That's exactly what we're going to do now. The `DoubleAttackModifier` is responsible for doubling the attack value of the `Creature`, and the `FireDamageModifier` is responsible for making the `Creature` take damage.

```java
class DoubleAttackModifier extends CreatureModifier
{
    public DoubleAttackModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle() {
        System.out.println("Doubling " + creature.name + "'s attack");
        creature.attack *= 2;
        super.handle();
    }
}

class FireDamageModifier extends CreatureModifier
{
    public FireDamageModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle() {
        System.out.println("Fire damage to " + creature.name);
        creature.defense -= 3;
        super.handle();
    }
}
```

A critical matter to notice here, when overriding the `handle()` we're calling `super.handle()`. Want to know why? Remember that `super.handle()` is designed specifically for the traversal of the entire chain of responsibility, the idea of calling the base class is to check the next element in the chain, and when another modifier is available it calls the defined `handle()` on it.

So you're effectively building a kind of chain of handlers for the different modifiers applied to the creature.

Now we can actually build a demo to put everything together.

```java
class Demo {
    public static void main(String[] args) {
        Creature goblin = new Creature("Goblin", 2, 6);
        System.out.println(goblin);
    }
}
```

Something interesting is about to happen here, because we're going to instantiate the root class `CreatureModifier`. It may cause some confusion doing that, after all this class doesn't have any functionality on its own, well the reason why we're doing this is that we want to have a starting point which we add all those `next` elements to traverse.

So the root element is going to be simply `CreatureModifier`, so now we're able to chain some other modifiers to it.

```java
class Demo {
    public static void main(String[] args) {
        Creature goblin = new Creature("Goblin", 2, 6);
        System.out.println(goblin);

        CreatureModifier rootModifier = new CreatureModifier(goblin);

        System.out.println("Applying attack modifier...");
        rootModifier.setNext(new DoubleAttackModifier(goblin));

        System.out.println("Applying defense modifier...");
        rootModifier.setNext(new FireDamageModifier(goblin));

        rootModifier.handle();
        System.out.println("Updated creature stats: " + goblin);
    }
}
```

The call of `rootModifier.handle()` is vital to apply all modifications on the `Creature`, traversing the entire chain of responsibility.

Output:

```
Creature{name='Goblin', attack=2, defense=6}
Applying attack modifier...
Applying defense modifier...
Doubling Goblin's attack
Fire damage to Goblin
Updated creature stats: Creature{name='Goblin', attack=4, defense=3}
```

Everything works as expected. There is one more thing that I want to show you which is how to actually disrupt the chain of responsibility, and how to cancel it outright.

Suppose that our goblin is walking through the forest, and he encounters a witch that curses him and ensures that he cannot get any modifiers, and so all the modifiers are inapplicable to that goblin. How would you implement this?

Well you can start by defining another modifier and as soon as you apply this modifier all the other bonus on the creature will be disabled.

```java
class CurseModifier extends CreatureModifier
{
    CurseModifier(Creature creature) {
        super(creature);
    }

    @Override
    public void handle() {
        // nothing
        System.out.println("No bonuses for you!");
    }
}
```

By not calling the `super.handle()` method we're effectively disabling the entire chain of modifiers. Let's check it out.

```java
class Demo {
    public static void main(String[] args) {
        Creature goblin = new Creature("Goblin", 2, 6);
        System.out.println(goblin);

        CreatureModifier rootModifier = new CreatureModifier(goblin);

        System.out.println("Applying defense modifier...");
        rootModifier.setNext(new FireDamageModifier(goblin));

        System.out.println("Oh no! Curse modifier...");
        rootModifier.setNext(new CurseModifier(goblin));

        System.out.println("Applying attack modifier...");
        rootModifier.setNext(new DoubleAttackModifier(goblin));

        rootModifier.handle();
        System.out.println("Updated creature stats: " + goblin);
    }
}
```

And then we get the following output:

```
Creature{name='Goblin', attack=2, defense=6}
Applying defense modifier...
Oh no! Curse modifier...
Applying attack modifier...
Fire damage to Goblin
No bonuses for you!
Updated creature stats: Creature{name='Goblin', attack=2, defense=3}
```

## Command query separation

Another worth mentioning topic is something called *command query separation*. The idea is that whenever we operate on objects we separate all the invocations into two different concepts which are called **commands** and **queries**.

A command is something that you send when you're asking for an action or change. For example when you want to set the attack value of a creature to a new value you're sending a command which specifies that you need to update the current state of something.

A query is basically asking for information without necessarily changing anything. For example by requesting to the system to give you the current timestamp.

And so we have something called `CQS`, which means that you have separate means of sending **commands** and **queries**. So instead of directly accessing a field of a particular class what you do is send a request to the system provide you either the content or the field itself, or you send a command which states the new value to be set on that particular field. And thanks to chain of responsibility you can also have other listeners to this command being sent, and they can override the behaviour of the actual command or indeed the query.

## Broker chain

So one of the limitations that we looked at in the previous demo is that you have to explicitly apply all the modifiers to a `Creature`, why is it not possible to simply track the presence of an object in the system and apply the modifier only while the modifier is actually there?

Well, now what we're going to do is a sort of combination demo where we're going to couple several patterns together, which are:

- Chain of responsibility
- Observer
- Mediator
- Memento

There are a lot of code to do, so let's not waste more time and start by implementing some sort of event class. We want to be able to generate events, the idea is to keep an `Event` class which is going to notify us on queries. So we're going to be processing queries and the query is going to typically ask us to provide an attack or defense value for a `Creature` just like in the previous lecture. On the `Event` class we're going to have an ability to subscribe, unsubscribe, and also fire another events.

In order to do that we start by specifying who exactly are the consumers of the events, these are our subscribers, and they are going to be effectively functions which handle whenever a particular event is fired, and it's provided a certain number of arguments. Another information that we need to track is the index, so we can increment and use that as a key into the map to find an appropriate consumer. After that we can make some sort of method for subscribing to the event, then for sure another method for unsubscribing, and finally another method for firing the event, for that we must establish some logic which is going to actually trigger the event and notify each of the subscribers that something happened.

```java
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// Command query separation
class Event<Args>
{
    private int index = 0;
    private Map<Integer, Consumer<Args>> subscribers = new HashMap<>();

    public int subscribe(Consumer<Args> consumer) 
    {
        int i = index;
        subscribers.put(++i, consumer);
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
```

By now you're probably wondering if we're just talking about modifying a `Creature` why do we have `consumers`? The reason is that we want to **layer** the query operation for a creature's attack or defense into an event that gets handled by whatever modifier that wants to apply itself to a `Creature`. So now we're going to build a query class, because we want to stick with this idea of *Command Query Separation*, and this class is going to specify what creature's attribute you acquiring.

```java
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
```

The `result` is the value that the handlers are able to modify to their heart's content, and the final result will be given to the consumer. That's pretty much we have to define for the `Query` class. 

The next question to answer is what else do we need to accomplish our goal? We obviously need the `Creature` and the creature statistics. But for that we're going to define a *mediator* called `Game`, so we have all the creatures participating in some type of game, and in terms of the `Game` the reason why we're making the *mediator* is that we want to make a central location where the `Query` event is kept.

```java
class Game
{
    public Event<Query> queries = new Event<>();
}
```

The idea behind this is that we now have a central location where **any** modifier can subscribe itself to queries on the `Creature` and modify the creature's attack or defense value. Is time to define the `Creature` itself.

```java
class Creature
{
    private Game game; //the game where the creature's is in.
    public String name;
    public int baseAttack, baseDefense;

    public Creature(Game game, String name, int baseAttack, int baseDefense)
    {
        this.game = game;
        this.name = name;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
    }
}
```

Now of course we want the `getters` and `setters` for attack and defense. In fact, defining the `setters` here don't make any sense if we are trying to apply the `Command Query Separator` concern. So in order to retrieve the base values what we need to do is take the base value plus each modifier which also has a reference to the event able to modify that value, and for doing that we perform a `Query`.

```java
class Creature
{
    //...

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
}
```

The `game.queries.fire(query)` is the instruction that lets any modifier subscribed to the event handle it and modify the query result. One more thing we need to define inside `Creature` is the `toString` method which is going to print out the creature's stats and doesn't make much sense printing the `baseAttack` and `baseDefense`.

```java
class Creature
{
    //...

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
```

Finally, we need to build the modifiers. The modifiers also reference our *event broker* which in our case is the `Game`, so when defining the `CreatureModifier` class we need to specify the references for the `Creature` and the `Game`.

```java
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
```

Now we start defining the derived classes.

```java
class DoubleAttackModifier extends CreatureModifier
{
    public DoubleAttackModifier(Game game, Creature creature) {
        super(game, creature);
    }
}

class FireDamageModifier extends CreatureModifier
{
    public FireDamageModifier(Game game, Creature creature) {
        super(game, creature);
    }
}
```

Aside calling the `super()` constructor, we want to take our *mediator*, which is our `Game`, and we want to subscribe to any queries that query that value. See how we can do this:

```java
class DoubleAttackModifier extends CreatureModifier
{
    public DoubleAttackModifier(Game game, Creature creature) {
        super(game, creature);
        game.queries.subscribe(query -> { //we specify the consumer as a lambda expression because it takes a Supplier as a parameter
            if (creature.name.equals(query.creatureName) 
                && query.argument == Query.Argument.ATTACK) 
            {
                query.result *= 2;
            }
        });
    }
}
```

But that's not all, we can store the returning **token** so that when the modifier finishes performing its job we can unsubscribe it from the events. This trick requires us to implement the `AutoCloseable` interface and override the `close()` method.

```java
class DoubleAttackModifier 
    extends CreatureModifier
    implements AutoCloseable
{
    private final int token;

    public DoubleAttackModifier(Game game, Creature creature) {
        super(game, creature);
        token = game.queries.subscribe(query -> { //we specify the consumer as a lambda expression because it takes a Supplier as a parameter
            if (creature.name.equals(query.creatureName) 
                && query.argument == Query.Argument.ATTACK) 
            {
                query.result *= 2;
            }
        });
    }

    /**
     * for the sake of the example we're going to ignore the exception 
     * so that we don't have to propagate any additional exception specifications
     * when we use the `close()` method
     */
    @Override
    public void close() //throws Exception 
    {
        game.queries.unsubscribe(token);
    }
}
```

That's how we unsubscribe from handling any kind of changes to the underlying creature.

So far so good, I think it's time to put it all together and actually look at how the *event broker* can be used.

```java
class Demo
{
    public static void main(String[] args)
    {
        Game game = new Game(); //initializing the event broker
        Creature darkElf = new Creature(game, "Dark elf", 15, 20); //instantiate the creature

        System.out.println(darkElf);

        //let's pile on some modifiers
        //as soon as we have initialized these objects the modifier has been automatically applied
        //because whenever we query the creature's stats, we're going to get the modifier taking part
        //because it's subscribed to the event inside the event broker

        DoubleAttackModifier doubleAttackModifier = new DoubleAttackModifier(game, darkElf);
        FireDamageModifier fireDamageModifier = new FireDamageModifier(game, darkElf);

        try (doubleAttackModifier; fireDamageModifier) {
            System.out.println("Creature stats during battle: " + darkElf);
        }

        System.out.println("Updated creature stats after recovery: " + darkElf);
    }
}
```

Check out the output:

```
Creature{name='Dark elf', attack=15, defense=20}
Creature stats during battle: Creature{name='Dark elf', attack=30, defense=17}
Updated creature stats after recovery: Creature{name='Dark elf', attack=15, defense=20}
```

Inside the `try-with-resources` the creature's stats get updated and as soon as we exit it, the modifiers are automatically unsubscribed from the event broker.

This has been an illustration of how to build an *event broker* and thereby once again we implemented this idea of the `Chain-of-Responsibility` pattern, plus using the *event broker* we've acquired extra flexibility because now objects can go in and out of the system, and they no longer are required to keep references to each other like we did before with the `Method Chain`, that's the benefit of using the `Mediator` pattern represented by the *event broker* called `Game`.
