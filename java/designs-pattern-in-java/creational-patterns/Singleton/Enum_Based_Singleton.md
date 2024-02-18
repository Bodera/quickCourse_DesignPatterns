# Enum Based Singleton

Since JDK 1.5 we have access to a new kind of tool called `Enum` and surprisingly enough `enums` are a tool which can be used to make singletons, but not without it's problems set up. Essentially the idea is very simple, instead of making a class what you do is make a **Enum**, now a **Enum** is a sort of a class except it has a finite number of instances so all you can do is something like:

```java
enum EnumBasedSingleton
{
    INSTANCE;
}
```

And here you go, you've just created a singleton so far. Now this singleton doesn't have those problems that we had previously when we talked about things like reflection being able to call the private constructor, but **enums** are `Serializable` by default but unfortunately the kind of serialization that we're talking about here isn't the same one that will let you preserve the state of the singleton. And of course there are others concerns as well. First one, you cannot inherit from this class because it's an **enum** which basically does not support it, but apart from that you can actually use this approach provided that you don't need any state to be persisted, now let's see what I mean with regard to *persistence*.

```java
enum EnumBasedSingleton
{
    INSTANCE;

    private int value;

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
```

Now before we implement the constructor keep in mind that essentially an enum already has a private default constructor and if you don't add a constructor here the consumers are not going to be able to make new instances of the enum in any way, so it doesn't really matter. But you can make a constructor, and even don't need to add the `private` keyword because it will always be private by default that's how Java works with enums there is no way of making public constructors for enums. 

```java
enum EnumBasedSingleton
{
    INSTANCE;

    private int value;

    EnumBasedSingleton()
    {
        value = 42;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
```

The problem with what we've just done is if we go ahead and actually serialize this enum, the assigned value of `42` of our `value` member isn't going to be serialized because when you serialize enums the fields aren't serialized even though enums are implicitly serializable the only thing that is serialized is the name of the instance, and that's all. This in turn can lead to really confusing kind of problems that we can see as follow:

```java
enum EnumBasedSingleton
{
    //...
}

class Demo
{
    static void saveToFile(EnumBasedSingleton singleton, String fileName) throws Exception
    {
        //...
    }

    static EnumBasedSingleton readFromFile(String fileName) throws Exception
    {
        //...
    }

    public static void main(String[] args) throws Exception
    {
        String filename = "myfile.bin";

        EnumBasedSingleton singleton = EnumBasedSingleton.INSTANCE;
        singleton.setValue(123);
        saveToFile(singleton, filename);

        EnumBasedSingleton otherSingleton = readFromFile(filename);

        System.out.println(singleton.getValue());
        System.out.println(otherSingleton.getValue());
    }
}
```

Remember that the only thing that is serialized in an `enum` is the name of it. Since whe only have the name *INSTANCE* declared here that's the only thing that will be serialized, and when we recover it we're going to get the value that we set originally because as you should remember when we are creating new instances of `enum` we're in fact accessing an instance that already exists. Let's confirm it by the checking the output:

```txt
123
123
```

Now let's try to read from file in such scenario where `123` was never saved to it.

```java
public static void main(String[] args) throws Exception
    {
        String filename = "myfile.bin";

        EnumBasedSingleton otherSingleton = readFromFile(filename);

        System.out.println(otherSingleton.getValue());
    }
```

It print's the following:

```txt
42
```

The take away from this approach is even though you can use an `enum` with a single value of instance to implement a singleton, the problem is you cannot serialize it effectively because all the fields don't get serialized. And in addition you can't inherit from `enums` because how that's was designed to behave. So if you are ok with these limitations you can actually implement your singleton in terms of a `enum` with a single member called *Instance* or whatever you decide to call it.
