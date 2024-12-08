public class MethodChain {
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