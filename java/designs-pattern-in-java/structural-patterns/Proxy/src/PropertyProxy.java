import java.util.Objects;
import java.util.logging.Logger;

public class PropertyProxy {

    public static void main(String[] args) {
        Creature creature = new Creature(10);
        creature.setAgility(10);
        System.out.println("Creature agility is: " + creature.getAgility());
    
        NewCreature newCreature = new NewCreature();
        newCreature.setAgility(112);
        System.out.println("CreatureProxy agility is: " + newCreature.getAgility());
    }

}

class Creature
{
    private int agility;

    public Creature(int agility)
    {
        this.agility = agility;
    }

    public int getAgility()
    {
        return agility;
    }

    public void setAgility(int agility)
    {
        this.agility = agility;
    }
}

class Property<T>
{
    private T value;
    private String alias;
    private static final Logger LOGGER = Logger.getLogger(Property.class.getName());

    public Property(T value, String alias)
    {
        this.value = value;
        this.alias = alias;

        LOGGER.info("Initialized " + alias + " having " + value + " as initial value.");
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
        LOGGER.info("Assigned: " + value + " to value.");
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        LOGGER.info("Assigned: " + alias + " to alias.");
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property<?> property = (Property<?>) o;
        return Objects.equals(value, property.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }
}

class NewCreature
{
    private Property<Integer> agility = new Property<>(5, "agility");

    public int getAgility()
    {
        return agility.getValue();
    }

    public void setAgility(int agility)
    {
        this.agility.setValue(agility);
    }
}
