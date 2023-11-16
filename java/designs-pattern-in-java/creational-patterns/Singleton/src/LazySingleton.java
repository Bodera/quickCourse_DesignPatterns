import java.util.Objects;

class LazySingleton
{
    private static volatile LazySingleton instance;

    private LazySingleton()
    {
        System.out.println("Initializing lazy singleton.");
    }

    public static LazySingleton getInstance()
    {
        if (Objects.isNull(instance))
        {
            synchronized (LazySingleton.class) //synchronized (this) will only work on a static context
            {
                if (Objects.isNull(instance))
                {    
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}