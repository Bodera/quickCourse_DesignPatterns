# Decorator

## String Decorator

Let's think about the classes which are final, take `String` class for example, suppose you want to add new functionality like some kind of getter method called `countVowels()` which tells you the number of vowels which are present in that String. Unfortunately you cannot do this, you can't just write something like:

```java
class MagicString extends String
{

}
```

Guess what `String` is a final class, and you're not allowed to extend it. All you can do is you can have a field of type `String`, and then you can expose the members of `String` using delegation, similar to this:

```java
class MagicString
{
    private String str;
    public MagicString(String str)
    {
        this.str = str;
    }
}
```

Now `String` has a rather rich API, so we end up having to rewrite lots and lots of call like this:

```java
class MagicString
{
    private String str;

    public MagicString(String str)
    {
        this.str = str;
    }

    public int length()
    {
        return str.length();
    }

    public char charAt(int index)
    {
        return str.charAt(index);
    }

    public int indexOf(String str)
    {
        return str.indexOf(str);
    }

    // and so on...
}
```

We want some of these calls at least, if not all, of the `String` API to be available inside `MagicString`. Obviously generating this by hand is particularly tedious which is why sometimes your IDE can help you by using the `Delegate Methods` feature. What we're really interested here is to add new functionality of our own:

```java
class MagicString
{
    private String str;

    public MagicString(String str)
    {
        this.str = str;
    }

    public long getNumberOfVowels()
    {
        return str.chars().filter(c -> "aeiouAEIOU".indexOf(c) != -1).count();
    }

    @Override
    public String toString()
    {
        return str;
    }

    // delegated methods
    public int length()
    {
        return str.length();
    }
    public char charAt(int index)
    {
        return str.charAt(index);
    }
    public int indexOf(String str)
    {
        return str.indexOf(str);
    }
    //....
}
```

We can now create a simple demo for this:

```java
public class Demo
{
    public static void main(String[] args)
    {
        MagicString magicString = new MagicString("Hello World!");
        System.out.println(magicString.getNumberOfVowels());
    }
}
```

As an output we get:

```bash
3
```

This was a very trivial example, but good enough in the sense that sometimes you get these classes, like `String` for example, that you cannot inherit from. If you can not inherit from something, and yet you need to expand its functionality there is only really one way to go that is to aggregate the actual object, so you stick that object `A` in some other object `B` and have `B` pretend in way it's the inner object `A`. That's really up to you how many members you want to delegate, you might need only a fraction of the available ones, in such a case you can simply pick and choose which APIs you want to be generated for you, and which APIs you want to draw, maybe because they're simply confusing or just not relevant to your development needs.

## Dynamic Decorator

Okay, so when we previously discussed the idea of multiple inheritance we basically didn't give the `Dragon` any additional functionality over what the `Bird` and the `Lizard` provide. Now we're going to do a proper decorator which actually takes some object and gives it additional functionality.

Let's start with an interface called `Shape`, which provides a single method for printing some information about the shapes, so we're deliberately not using `toString()` to print the info about the shape.

```java
interface Shape
{
    String info();
}
```

Now let's add a bunch of shapes:

```java
class Circle implements Shape
{
    private float radius;

    public Circle(){}

    public Circle(float radius)
    {
        this.radius = radius;
    }

    void resize (float factor)
    {
        radius *= factor;
    }

    @Override
    public String info()
    {
        return "A circle of radius " + radius;
    }
}

class Square implements Shape
{
    private float side;

    public Square(){}

    public Square(float side)
    {
        this.side = side;
    }

    @Override
    public String info()
    {
        return "A square of side " + side;
    }
}
```

Now let's create the actual decorator. Suppose that you have a `Circle` and also a `Square`, you don't want to jump back and modify those classes in case of, for example, need to specify some color or enable transparency for the shapes, you want to be consistent with the __Open-Closed Principle__ because essentially if you have your objects already defined, and already tested, which means they are open for extension, and the *decorator* is one way of extending the object, but they are closed for modification.

We're going to assume that you're not allowed to go back into `Circle` and `Square` to modify them, but we've been asked to enable adding colors to them. How do you add color? Well you first start building a *decorator*.

First define a class `ColoredShape` as a decorator over `Shape` thus we need to implement it. In order to make our decorator act like a `Shape` we need to override the `Shape` methods, but also we need to specify which `Shape` we're decorating, so as it's color.

```java
class ColoredShape implements Shape
{
    private Shape shape;
    private String color;

    public ColoredShape(Shape shape, String color)
    {
        this.shape = shape;
        this.color = color;
    }

    @Override
    public String info()
    {
        return shape.info() + " has the color " + color;
    }
}
```

In addition to `ColoredShape` we can have yet another decorator, for example, to specify a transparency value:

```java
class TransparentShape implements Shape
{
    private Shape shape;
    private int transparency;

    public TransparentShape(Shape shape, int transparency)
    {
        this.shape = shape;
        this.transparency = transparency;
    }

    @Override
    public String info()
    {
        return shape.info() + " has " + transparency + "% transparency";
    }
}
```

We can also have a `Shape` capable to have both color and transparency:

```java
class Demo
{
    public static void main(String[] args)
    {
        Shape square = new ColoredShape(new Square(2.0f), "red");
        System.out.println(square.info());
        Shape circle = new TransparentShape(new ColoredShape(new Circle(2.0f), "blue"), 50);
        System.out.println(circle.info());
    }
}
```

We get the following output:

```bash
A circle of radius 2.0 has the color red
A square of side 2.0 has the color blue has 50% transparency
```

The takeaway from this example is that ultimately what you end up doing is instead of modifying the original objects you end up making these *decorators* which take your original object in the constructor, so they're aggregated in a private variable, and they provide additional information as well. Also, by having a common interface what you can do is get the *decorator* behave as the underlying object. 

One thing you have to realize from this example is that ultimately the `circle` variable is not itself an instance of `Circle`, it just implements the `Shape` interface, so the call for `circle.resize()` is not valid because is not part of the decorator API, we don't have this inheritance relationship between them, so there is no way to access the underlying methods.

## Static Decorator

The previous decorator that we've built is what's called a dynamic decorator, the reason why it's dynamic is you can always build new decorators at runtime which is very convenient if that's the kind of thing that you need to do but, sometimes, you know for a fact that you're going to need a `TransparentShapeOfColoredShapeOfCircle`. What if you could bake all of those things into a single type? You can, thanks to generics, but unfortunately because of [Java's Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html), you're going to jump through hoops if you want to actually preserve the type information. Let's discover together how this can be implemented.

We're going to leave the implementation of `Square` and `Circle` as they are, but we're going to destroy the decorator's that we've built so far, so we must delete the `ColoredShape` and `TransparentShape` classes. Now we're going to do things in a static way, by that I mean you're not going to be able to construct these kinds of classes at runtime, but you're going to be able to define those at runtime. Let's see how this happens in action.

First we're going to build a new colored shape specifying the underlying shape as a type argument. The actual implementation is going to be very similar to the previous one, except for the constructor.

```java
class ColoredShape<T extends Shape> implements Shape
{
    private Shape shape;
    private String color;

    public ColoredShape(?)
    {

    }

    @Override
    public String info()
    {
        return shape.info() + " has the color " + color;
    }
}
```

We need to initialize the shape somehow, and we cannot do it via constructor because we know that `T` is something that inherits from `Shape` but the following declaration is not valid.

```java
//nope
public ColoredShape(Shape shape, String color)
{
    this.shape = shape;
    this.color = color;
}
```

The following is also invalid:

```java
//nope
public ColoredShape(String color)
{
    this.shape = new T();
    this.color = color;
}
```

Here we need to cheat, and one way of cheating is to take a `Supplier`, what's essentially going to be provided is a lambda function which takes a supplier of the type argument of whatever that inherits from `T`. Just like that:

```java
public ColoredShape(Supplier<? extends T> supplier, String color)
{
    this.shape = supplier.get();
    this.color = color;
}
```

We're now constructing the shape from the supplier that we're given. They provide us a way of building a particular shape, and then we use that to call the constructor in order to initialize the object. Our decorators should like this now:

```java
class ColoredShape<T extends Shape> implements Shape
{
    private Shape shape;
    private String color;

    public ColoredShape(Supplier<? extends T> supplier, String color)
    {
        this.shape = supplier.get();
        this.color = color;
    }

    @Override
    public String info()
    {
        return shape.info() + " has the color " + color;
    }
}

class TransparentShape<T extends Shape> implements Shape
{
    private Shape shape;
    private int transparency;

    public TransparentShape(Supplier<? extends T> supplier, int transparency)
    {
        this.shape = supplier.get();
        this.transparency = transparency;
    }

    @Override
    public String info()
    {
        return shape.info() + " has " + transparency + "% transparency";
    }
}
```

We have build our static decorators, let's see how they work together.

```java
class Demo 
{
    public static void main(String[] args)
    {
        ColoredShape<Circle> circle = new ColoredShape<>(
            () -> new Circle(2.0f),
            "red");
        System.out.println(circle.info());

        TransparentShape<ColoredShape<Square>> square = new TransparentShape<>(
            () -> new ColoredShape<>(
                () -> new Square(2.0f), 
                "blue"),
            50);
        System.out.println(square.info());
    }   
}
```

As an output we get the following:

```bash
A circle of radius 2.0 has the color red
A square of side 2.0 has the color blue has 50% transparency
```

This was an illustration on how you can kind of determine the types at compile time as opposed to runtime if that's what you want to do. Unfortunately this does not fix the problem of the unavailability of the underlying API, which means that you still can't call `resize()` on the `circle` variable. Our decorators aren't actual instances of the underlying classes.

In some programming languages the solution could be to inherit from the type argument of `<T extends Shape>` instead of implementing `Shape`. But in Java this is not possible, which limits the amount of things that we can do with these decorator approaches.

## Adapter Decorator

So far we've been basically building decorators, so we can have new functionality on the side while sort of preserving the API to some degree. It's also possible to build something called an *adapter-decorator* which is essentially a merge of the __Adapter__ and __Decorator__ patterns. The decorator provides additional functionality while the adapter tries to mimic some interfaces or make some things a bit easier to use.

Supposing that you want to make your own `StringBuilder`.

```java
//nope
class MyStringBuilder extends StringBuilder
{
    
}
```

Then you realize that `StringBuilder` it's also a final class, thus it's impossible to extend from it. What you have to do is aggregate it and then provide all the API.

```java
class MyStringBuilder
{
    private StringBuilder sb;

    public MyStringBuilder()
    {
        sb = new StringBuilder();
    }

    public MyStringBuilder(String str)
    {
        sb = new StringBuilder(str);
    }
}
```

How do you expose all the APIs of `sb`? You use the `Delegate Methods` functionality of your IDE. After generating those, there are some new problems to address. The first problem is that we get some annotations which are just not useful for us like `@HotSpotIntrinsicCandidate`, after deleting those annotations there is a much bigger problem left by this co-generated code. You're meant to get a fluent interface, so the `append()` calls inside the `StringBuilder` itself, if the API is supposed to be fluent `append()` should return an instance of `MyStringBuilder` instead of `StringBuilder`.

```java
//nope
class MyStringBuilder
{
    private StringBuilder sb;

    public MyStringBuilder()
    {
        sb = new StringBuilder();
    }

    public MyStringBuilder(String str)
    {
        sb = new StringBuilder(str);
    }

    // delegated methods
    public StringBuilder append(Object obj)
    {
        return sb.append(obj);
    }

    public StringBuilder append(String str)
    {
        return sb.append(str);
    }

    public StringBuilder append(StringBuffer sb)
    {
        return this.sb.append(sb);
    }
}
```

At first sight you can modify the return of the method.

```java
// delegated methods
public StringBuilder append(Object obj)
{
    sb.append(obj);
    return this;
}

public StringBuilder append(String str)
{
    sb.append(str);
    return this;
}

public StringBuilder append(StringBuffer sb)
{
    this.sb.append(sb);
    return this;
}
```

Now you have to build a regular expression to actually update every single delegated method inside `MyStringBuilder` to replace the return lines with `this`.

Moving on to the adaptive part of this lesson. Suppose that we want our `MyStringBuilder` to act as a bit of `StringBuilder`, so we want some APIs which are not part of `StringBuilder`, but they're a part of `String` instead. Like the `concat()` method, for example.

```java
class MyStringBuilder
{
    private StringBuilder sb;

    public MyStringBuilder()
    {
        sb = new StringBuilder();
    }

    public MyStringBuilder(String str)
    {
        sb = new StringBuilder(str);
    }

    // adapted methods (adapter part)
    public MyStringBuilder concat(String str)
    {
        return new MyStringBuilder(sb.toString().concat(str));
    }

    public MyStringBuilder appendLine(String str)
    {
        sb.append(str).append(System.lineSeparator());
        return this;
    }

    public String toString()
    {
        return sb.toString();
    }

    // delegated methods (decorator part)
    //....

}
```

Now you want to see it in action.

```java
class Demo
{
    public static void main(String[] args)
    {
        MyStringBuilder sb = new MyStringBuilder("Hello");
        sb.appendLine("World");
        System.out.println(sb);
    }
}
```

We get the following output:

```bash
HelloWorld

```

The takeaway from this example is that your decorator doesn't just have to be a decorator, it can also be other things as well, like a decorator and also an adapter, and all sort of other things, like a facade or something like that.
