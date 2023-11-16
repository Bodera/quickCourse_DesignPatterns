# Laziness initialization and Thread safety

In this lecture we'll cover two particular problems involving Singleton, first one is _Lazy Initialization_ and the other is _Thread Safety_.

Essentially the problem with lazy initialization is that sometimes you only want the singleton to be initialized whenever someone calls to `getInstance()` and you don't want it to be created in a static block or a static context, that means it will be created just when it is really needed.

In fact that is very simple to do:

```java
class LazySingleton
{
    private static LazySingleton instance;

    private LazySingleton()
    {
        System.out.println("Initializing lazy singleton.");
    }

    public static LazySingleton getInstance()
    {
        if (Objects.isNull(instance))
        {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```

The idea of lazy instantiation is that instead of creating it inside a final variable we have to wait until somebody actually request this instance, so that we use the null check, and that's pretty much all.

Now the problem that follows this approach is that we introduce a potential race condition if you have several threads accessing the `getInstance()` method and calling it at the same time, that could lead to a situation where you have threads in different states so one of them checks for null and the other also performs the same check and they both see the variable equals to __null__ so they both ends up initializing our singleton calling the constructor twice. So your singleton might be sensitive to the idea of calling the constructor twice.

So how can we actually protect the singleton from being instantiated more than once? One way in which you can go is to just make the `getInstance()` method __synchronized__.

```java
class LazySingleton
{
    private static LazySingleton instance;

    private LazySingleton()
    {
        System.out.println("Initializing lazy singleton.");
    }

    public static synchronized LazySingleton getInstance()
    {
        if (Objects.isNull(instance))
        {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```

You must know that synchronized methods has an performance implication.

Synchronized statements however are more performance friendly, and we can explore an approach called the __double-check locking__ (which is outdated and we're covering it here just for the sake of demonstration, using [volatile](https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html) is cleverer).

```java
class LazySingleton
{
    private static LazySingleton instance;

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
```

Now the reason it is called double-check locking it is because we perform the check both outside and inside a synchronized block of code. But this could be better if we just declare our `instance` variable as _volatile_. 

```java
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
```

These are some ways in which you can implement a singleton that is both lazy as well thread safe.
