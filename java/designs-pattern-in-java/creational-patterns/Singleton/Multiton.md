# Multiton

Now we're going to take a look at a variation of the __singleton pattern__. As you remember, singleton design pattern has one goal: to provide a single instance of a particular object. That means a single instance of a class through your whole application lifecycle.

The goal of __multiton pattern__ is different, it doesn't try to force the existence of a single instance. Instead, it concerns that there are a finite set of instances, and for each instance you'll be able to implement additional benefits such as lazy loading for example.

It might sound cryptic because if you are allowing many instances why not simply enable a public constructor? The fact is that our intention here is restrict the number of instances that are created or maybe have those instances associated with some sort of `enum` for example.

And now we're going explore how to implement it. Let's say our system has a classic old printer that should exists in a finite set of subsystems, and we'll wrap those subsystems into a `enum`. In addiction we also want some sort of lazy loading in that case if nobody needs a particular system then isn't even created. 

```java
class Printer
{
    //a key-value structure is also called a dictionary
    private static Map<Subsystem, Printer> instances = new HashMap<Subsystem, Printer>();

    private Printer() {}

    public static Printer get(Subsystem ss)
    {

    }
}

enum Subsystem
{
    PRIMARY, 
    AUXILIARY,
    FALLBACK;
}
```

Remember the idea is to provide a subsystem to retrive a __singleton__ for that subsystem, in this way there always be only one printer to each subsystem. So first thing to do is check whether or not a printer associated with the subsystem has already been created, in that case we do no create another instance we provide the already existing one. In that way we can implement a lazy loading behavior.

```java
class Printer
{
    //...
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

//...
```

Now a printer is only created when somebody actually asks for it and also we defined a maximum of three printers in the whole system because there are just three enumeration members that are used as key in our dictionary of singletons.

It's time to add some diagnostic information so we know how many instances of `Printer` have been created.

```java
class Printer
{
    private static int instanceCount = 0;
    private Printer() 
    {
        ++instanceCount;
        System.out.println("Total of " + instanceCount + " instances.");
    }
    //...
}
```

Let's check it out.

```txt
Total of 1 instances.
Total of 2 instances.
```

As we can see, __multiton__ revolves around a lazy-loading key-value store, so it's not a strong pattern like __singleton__, because it doesn't enforce things as tightly except the lifecycle.
