# Serialization Problems

At this point you're probably wondering what is the problem with the singleton implementation that we've built previously. In fact there are two problems with it, problem number one has to do with reflection, you should have guessed that even though we have a private constructor it can be defeated, you can use Reflections to access this constructor and actually call it making it effectively public and then creates new instances of this basic `Singleton` and that's is what we wanted to prevent in the first place. So that's problem number one, but it requires explicit use and the developer has to know that he wants to defeat the mechanism of our `BasicSingleton`. However there is another way in which we can actually make additional copies of that singleton and then breaking the whole `Singleton` contract without even figuring out that we're actually doing it. We're talking about now __serialization__, what happens about it's when you deserializes an object the JVM doesn't really care that your constructor is private, it's still going to construct the object anyway.

Let's check that in practice by adding a couple of methods for serializing and deserializing our `BasicSingleton`. Let's star by changing our `Demo` class:

```java
class BasicSingleton implements Serializable
{
    //...
}

class Demo
{

    static void saveToFile(BasicSingleton singleton,
                            String fileName) throws Exception
    {
        //performing serialization
        try (FileOutputStream fileOutput = new FileOutputStream(fileName);
                ObjectOutputStream objOut = new ObjectOutputStream(fileOutput)) 
        {
            objOut.writeObject(singleton);
        }
    }

    static BasicSingleton readFromFile(String fileName) throws Exception
    {
        //performing deserialization
        try (FileInputStream fileInput = new FileInputStream(fileName);
                ObjectInputStream objInp = new ObjectInputStream(fileInput))
        {
            return (BasicSingleton) objInp.readObject();
        }
    }

    public static void main(String[] args) throws Exception
    {
        BasicSingleton carrier = BasicSingleton.getInstance();
        carrier.setSecret(12345);

        String testSerializationFileName = "carrier.bin";
        saveToFile(carrier, testSerializationFileName);

        carrier.setSecret(54321);
        
        BasicSingleton newCarrier = readFromFile(testSerializationFileName);

        System.out.println(carrier == newCarrier);

        System.out.println(carrier.getSecret());
        System.out.println(newCarrier.getSecret());
    }
}
```

And that's the output this program produces:

```bash
false
54321
12345
```

As we can see, that program above is performing a non-singleton behavior, we getting the value of false which indicates that the instances of our `BasicSingleton` are in fact different, futhermore the value of `secret` of each instance aren't the same, because one is holding the value of __54321__ while the other one stores the value of __12345__ so that implementation is not good at all, we don't have a `Singleton` in actual fact.

Just for curiosity, let's check an approach were we can easily fix this issue:

```java
class BasicSingleton implements Serializable
{
    //...

    protected Object readResolve()
    {
        return INSTANCE;
    }
}
```

The purpose of the `readResolve()` method is to tell the JVM a hint so that whenever a serialization routine happens in out `BasicSingleton`, it must have to happen on a given context, in that case the `INSTANCE` context. The result of this action is that instead of making a new instance of the entity, we will reuse the already created one.

If we rerun our program we can notice a different output, which is illustrated as follows:

```bash
true
54321
54321
```

Now we're finally getting the correct behavior that we expected in the first place, as we can see both instances of `BasicSingleton` refer to the same memory address and that's why it's printing the value of `true` and also the value of `secret` is equal on both.

Now that we've saw how __serialization__ can actually creates instances behind the scenes that we may not be aware of, and how to fix it to ensure proper behavior of the _Singleton Pattern_. 
