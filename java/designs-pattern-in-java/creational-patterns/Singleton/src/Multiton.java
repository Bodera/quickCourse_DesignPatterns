import java.util.HashMap;
import java.util.Map;

class Multiton {
    public static void main(String[] args) {
        Printer main = Printer.get(Subsystem.PRIMARY);
        Printer aux = Printer.get(Subsystem.AUXILIARY);
        Printer aux_2 = Printer.get(Subsystem.AUXILIARY);
    }
}

class Printer
{
    private static int instanceCount = 0;
    private static Map<Subsystem, Printer> instances = new HashMap<Subsystem, Printer>();

    private Printer() {
        ++instanceCount;
        System.out.println("Total of " + instanceCount + " instances.");
    }

    public static Printer get(Subsystem ss)
    {
        if (instances.containsKey(ss))
        {
            return instances.get(ss);
        }

        Printer instance = new Printer();
        instances.put(ss, instance);
        return instance;
    }
}

enum Subsystem
{
    PRIMARY, 
    AUXILIARY,
    FALLBACK;
}