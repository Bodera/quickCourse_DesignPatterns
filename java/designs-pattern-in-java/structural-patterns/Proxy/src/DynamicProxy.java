import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

interface Human
{
    void talk();
    void walk();
}

class Person implements Human
{
    @Override
    public void walk()
    {
        System.out.println("I am walking");
    }

    @Override
    public void talk()
    {
        System.out.println("I am talking");
    }
}

class LoggingHandler implements InvocationHandler
{

    private final Object target;
    private Map<String, Integer> methodCalls = new HashMap<>();

    public LoggingHandler(Object target)
    {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        String methodName = method.getName();

        if (methodName.equals("toString"))
        {
            return methodCalls.toString();
        }

        methodCalls.merge(methodName, 1, Integer::sum); //what happens if this line is before the if statement?
        return method.invoke(target, args);
    }
}

public class DynamicProxy
{

    @SuppressWarnings("unchecked")
    public static <T> T withLogging(T target, Class<T> interfaceType)
    {
        return (T) Proxy.newProxyInstance(
            interfaceType.getClassLoader(), 
            new Class<?>[] {interfaceType},
            new LoggingHandler(target)
        );
    }

    public static void main(String[] args)
    {
        Person person = new Person();
        Human logged = withLogging(person, Human.class);
        logged.walk();
        logged.walk();
        logged.talk();

        System.out.println(logged);
    }
}