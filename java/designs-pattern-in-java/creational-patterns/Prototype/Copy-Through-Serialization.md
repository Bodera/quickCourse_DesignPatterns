# Copy Through Serialization

One of the massive problems that we may encounter when using the copy constructors is that you have to build a copy constructor for every single type that you have. And if you have a hierarchy of 20 different types for example, you have to make 20 different copy constructors. That's because otherwise one of those objects will not be copied correctly, it will not perform a deep copy it will perform a shallow copy instead, and that's not the desirable behavior that we want.

So the question of one million dollars is: given the complicated hierarchy of types how can you actually copy those objects without jumping through the hoops and implementing series of copy constructors? Well why not to use serialization for that?

Let's check how it works without building a complex hierarchy for the sake of simplicity.

We'll have a class called `Foo` that implements `Serializable`. As a consequence, no matter how many members we declare inside `Foo`, they are all able now to be serializable.

```java
import java.io.Serializable;

class Foo implements Serializable
{
    public int number;
    public String text;

    public Foo(int number, String text)
    {
        this.number = number;
        this.text = text;
    }

    @Override
    public String toString()
    {
        return "Foo{" +
        "number=" + number +
        ", text='" + text + '\''
        '}'+
    }
}
```

The next step is take this object and perform a deep copy of it using serialization.
```java
import org.apache.commons.lang3.SerializationUtils;

class Demo
{
    public static void main(String[] args)
    {
        Foo original = new Foo(42, "life");
        Foo copy = SerializationUtils.roundtrip(original);

        copy.number = 23;
        copy.text = "laugh";
        System.out.println(original);
        System.out.println(copy);
    }
}
```

To compile the program run:

```java
$ export CLASSPATH=$CLASSPATH:/home/<ordinary-user>/<ordinary-path>/libs/commons-lang3-3.13.0.jar 

$ javac ExampleCopySerialization.java

$ java ExampleCopySerialization
```

The `roundtrip()` method does essentially two things, it serializes an object and then deserializes it. By doing that, the outcome is obviously a brand new copy because it actually takes care of serializing the entire object graph behind the scenes. So if your object contains other objects they all get serialized/deserialized. It performs what we refer as copy by value.

By this very simple approach shows one of tons of different kinds of serialization utilities, there are libraries which use reflections instead and in that case you don't even need to implement the `Serializable` interface at all.

Well, so far we've learned that to implement the Prototype pattern you don't necessarily have to specify explicitly what exactly it is what you're copying because you can take this approach of serialization, basically saving the entire object graph to some store and then reading from that, so then you have a brand new copy of everything on it. Now you no longer refers to the original object and that's what we want in the first place, we want a deep copy regardless of how complicated that object happens to be.
