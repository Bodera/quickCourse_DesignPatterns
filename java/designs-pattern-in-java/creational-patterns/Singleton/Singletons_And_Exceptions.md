# The Static Block Singleton

Now let's see one simple variation of the _Singleton Pattern_ called __Static Block Singleton__ which is useful when the private constructor of your Singleton is prone to throw some kind of Exception. And if you have exceptions inside of the Singleton constructor that actually is a problem, so one way to mitigate this is using a *static* block.

Let's see how to run this:

```java
class StaticBlockSingleton 
{
    private StaticBlockSingleton() throws IOException
    {
        System.out.println("Singleton is initializing.");
        File.createTempFile(".", ".");
    }
}
```

Now that we have a private constructor which throws an Exception, in order to initialize our Singleton we can no longer just have a private final static value to hold the Singleton instance. 

What we can do instead is declare a static block and initialize everything inside it.

```java
class StaticBlockSingleton 
{
    //...

    private static StaticBlockSingleton instance; //no longer final

    static 
    {
        try 
        {
            instance = new StaticBlockSingleton();
            System.out.println("Singleton is now initialized.");
        } 
        catch(Exception e)
        {
            //handle exception here.
            System.err.println("Failed to initialize Singleton.");
        }
    }

    public static StaticBlockSingleton getInstance()
    {
        return instance;
    }
}
```

And that is basically the idea we want to explore in this class, when in such a situation where you may have a Exception that could be throw then some changes need to be fulfill. And in addition this kind of approach is also useful if you want additional kinds of behaviors in terms of accessing the actual Singleton. so suddenly you can have it you can have additional behaviors inside that get instance like for example if you're implementing __object pooling__ where there isn't a single object but there is a finite number of objects that you're returning so you could do it in `getInstance()`.

But in terms of initialization of the object specifically the initialization of the singleton has to be customized somehow you can put everything inside a static block which you can treat as a kind of static constructor effectively so this is where you would customize and do try catch and do whatever other things you need before the singleton is actually constructed.
