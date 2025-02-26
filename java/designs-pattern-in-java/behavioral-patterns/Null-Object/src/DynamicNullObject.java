import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
class DynamicNullObject 
{
    public static void main(String[] args)
    {
        Log log = noOp(Log.class); // This is a null object
        BankAccount bankAccount = new BankAccount(log);

        bankAccount.deposit(100);
    }

    public static <T> T noOp(Class<T> type)
    {
        return (T) Proxy.newProxyInstance(
            type.getClassLoader(), 
            new Class[]{type}, 
            (proxy, method, args) -> {
                if (method.getReturnType().equals(Void.TYPE)) return null;

                return method.getReturnType().getConstructor().newInstance();
            });    
    }
}

final class NullLog implements Log 
{
    @Override
    public void info(String message) {}

    @Override
    public void error(String message) {}

    @Override
    public void warn(String message) {}
}

class ConsoleLog implements Log 
{
    @Override
    public void info(String message) {
        System.out.println("INFO: " + message);
    }

    @Override
    public void error(String message) {
        System.err.println("ERROR: " + message);
    }

    @Override
    public void warn(String message) {
        System.out.println("WARN: " + message);
    }
}

interface Log 
{
    void info(String message);
    void error(String message);
    void warn(String message);
}

class BankAccount 
{
    private final Log log;
    private int balance;

    public BankAccount(Log log) {
        this.log = log;
    }

    public void deposit(int amount) {
        balance += amount;
        log.info("Depositing " + amount);
    }
}
