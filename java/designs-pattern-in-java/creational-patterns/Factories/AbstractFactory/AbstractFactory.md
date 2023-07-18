# Abstract Factory

Now we are going to talk about the abstract factory design pattern. This design pattern isn't particularly common, there's hardly any cases where you're likely to be using it but for sure there are situations where you want to use an abstract factory.

So what's an abstract factory all about? Well the abstract factory is essentially a pattern which makes as correspondence between the hierarchy of objects that you have. We are going to see this in practice using drink machines as an example. Let's suppose you are modeling a drink machine, you can have `Tea` and `Coffe` both implementing some hot drink interface, it makes correspondence between hierarchy of objects in a hierarchy of factories which are used to construct those objects and this is precisely the kind of them that we're going to be doing here.

```java
interface HotDrink
{
    void consume();
}
```

Now let's make some drinks.

```java
class Tea implements HotDrink
{
    @Override
    public void consume() {
        System.out.println("Gulp, gulp. Ah, this tea is delicious.");
    }
}

class Coffee implements HotDrink
{
    @Override
    public void consume() {
        System.out.println("Gulp, gulp. Ah, this coffee is delicious.");
    }
}
```

Now we can make a corresponding hierarchy of factories which would construct those objects, in that way we will have the `TeaFactory` and the `CoffeFactory` and both will implement a common interface, let's name it the `HotDrinkFactory` and add a single method that returns a `HotDrink` object.

```java
interface HotDrinkFactory
{
    HotDrink prepare(int amount);
}

class TeaFactory implements HotDrinkFactory 
{
    @Override
    public HotDrink prepare(int amount) {
        System.out.println(
            "Put in tea bag, boil water, pour "
            + amount + "ml, add lemon, enjoy!"
        );
        return new Tea();
    }
}

class CoffeeFactory implements HotDrinkFactory
{
    @Override
    public HotDrink prepare(int amount) {
        System.out.println(
            "Grind some beans, boil water, pour "
            + amount + "ml, add cream and sugar, enjoy!"
        );
        return new Coffee();
    }
}
```

See how now we can make a corresponding hierarchy of factories which would construct those `Tea` and `Coffe` objects? `TeaFactory` and `CoffeFactory` both implement `HotDrinkFactory` interface. They don't share a common base class but they share a common interface.

To keep it simple we're returning new instances of `Tea` and `Coffe`, but typically what you would do is maybe customize these objects a little because that's the whole point of having a factory in the first place, so you would specify very peculiar arguments to the constructor or whatever it is that you have to do essentially.

Having set this up, we now can build a hot drink machine which is going to operate just like an ordinary drinks machine.

```java
class HotDrinkMachine
{
    private List<Entry<String, HotDrinkFactory>> namedFactories = new ArrayList<>();

    public HotDrinkMachine() throws Exception
    {
        Set<Class<? extends HotDrinkFactory>> types = new HashSet<>();
        types.add(CoffeeFactory.class);
        types.add(TeaFactory.class);

        for (Class<? extends HotDrinkFactory> type : types) 
        {
            namedFactories.add(new AbstractMap.SimpleEntry<>(
                type.getSimpleName().replace("Factory", ""),
                type.getDeclaredConstructor().newInstance()));
        }
    }

    public HotDrink makeDrink() throws Exception 
    {
        System.out.println("Available drinks:");
        for (int i = 0; i < namedFactories.size(); ++i) {
            Entry<String, HotDrinkFactory> item = namedFactories.get(i);
            System.out.println("" + i + ":" + item.getKey());
        }

        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));

        while (true) 
        {
            String stg;
            int key, amount;

            if ((stg = bfr.readLine()) != null && (key = Integer.parseInt(stg)) >= 0 && key < namedFactories.size()) 
            {
                System.out.println("Specify amount:");
                stg = bfr.readLine();
                
                if (stg != null && (amount = Integer.parseInt(stg)) > 0) {
                    return namedFactories.get(key).getValue().prepare(amount);
                }
            }

            System.out.println("Incorrect input, try again.");
        }
    }
}
```

The idea of the algorithm is to find out all the factories which are capable of performing/baking hot drinks and store them all into a list of some kind. We enabled a public method that returns hot drinks so we can ask what kind of drinks that are available the user want.

```java
class Demo
{
    public static void main(String[] args) throws Exception
    {
        HotDrinkMachine hdm = new HotDrinkMachine();
        HotDrink hd = hdm.makeDrink();
        hd.consume();
    }
}
```
