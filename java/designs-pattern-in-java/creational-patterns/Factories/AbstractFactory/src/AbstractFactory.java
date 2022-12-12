import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

interface HotDrink
{
    void consume();
}

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

class HotDrinkMachine
{
    private List<Entry<String, HotDrinkFactory>> namedFactories = new ArrayList<>();

    public HotDrinkMachine() throws Exception
    {
        Set<Class<? extends HotDrinkFactory>> types = new HashSet<>();
        types.add(CoffeeFactory.class);
        types.add(TeaFactory.class);
        
        //Now imagine that we've implemented a DrinkMachine that could provide us
        //both hot and could drinks. To discover all available types of drinks
        //in our application we could use the Reflections trick.
        //In the future I intent to update this example using reflections to discover
        //at runtime the subtypes of HotDrinkFactory class (i.e. all implementations of this interface)

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

class Demo
{
    public static void main(String[] args) throws Exception
    {
        HotDrinkMachine hdm = new HotDrinkMachine();
        HotDrink hd = hdm.makeDrink();
        hd.consume();
    }
}
