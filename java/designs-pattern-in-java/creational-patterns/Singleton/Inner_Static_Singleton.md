# Inner Static Singleton

Here we will demonstrate a similar Singleton approach which has the same behavior os the past one in terms of supporting the concept of thread safety but it actually doesn't require the `synchronized` keyword.

This is a very simple trick to implement we're going to basically reduce our singleton to a nested static class and inside it we'll have a static member that is going to be exposed, as shown:

```java
class InnerStaticSingleton
{
    private InnerStaticSingleton(){}

    private static class Impl
    {
        private static final InnerStaticSingleton INSTANCE = new InnerStaticSingleton();
    }
}
```

As you should remember inner classes can access private members of outer classes which is precisely what's happening here. So we have a static class `Impl` which has a static final variable `INSTANCE` that then is initialized with the outside variables/class constructor.

So we've made this instance and once again we need to expose it somehow:

```java
class InnerStaticSingleton
{
    //...

    public InnerStaticSingleton getInstance()
    {
        return Impl.INSTANCE;
    }
}
```

And that's all. Curiously enough this approach actually avoids the problem of synchronization so it guarantees effectively that whenever you initialize the instance you're only going to get one instance whatever happens. So there is no need to take care of thread safety here, and I'm pretty sure you will agree that this implementation is a lot simpler that the previous one.
 