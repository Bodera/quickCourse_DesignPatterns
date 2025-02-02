import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ABPDemo {
    public static void main(String[] args)
    {
        Creature creature = new Creature();
        creature.setHealth(100);
        creature.setLevel(10);
        creature.setXp(100);
        
        System.out.println("Max stats: " + creature.maxStats());
        System.out.println("Total stats: " + creature.sumStats());
        System.out.println("Avg stat: " + creature.avgStats());
    }
}

enum CreatureStats
{
    HEALTH,
    LEVEL,
    XP;
}

class Creature implements Iterable<Integer>
{
    private int[] stats = new int[3];
    
    // getter
    public int getHealth() { return stats[CreatureStats.HEALTH.ordinal()]; }
    public int getLevel() { return stats[CreatureStats.LEVEL.ordinal()]; }
    public int getXp() { return stats[CreatureStats.XP.ordinal()]; }
    
    // setter
    public void setHealth(int health) { stats[CreatureStats.HEALTH.ordinal()] = health; }
    public void setLevel(int level) { stats[CreatureStats.LEVEL.ordinal()] = level; }
    public void setXp(int xp) { stats[CreatureStats.XP.ordinal()] = xp; }

    // statistic methods
    public int maxStats() { return Arrays.stream(stats).max().getAsInt(); }
    public int sumStats() { return Arrays.stream(stats).sum(); }
    public double avgStats() { return Arrays.stream(stats).average().getAsDouble(); }
    
    @Override
    public Iterator<Integer> iterator()
    {
        return Arrays.stream(stats).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action)
    {
        for (Integer stat : stats)
            action.accept(stat);
    }

    @Override
    public Spliterator<Integer> spliterator()
    {
        return Arrays.stream(stats).spliterator();
    }
}