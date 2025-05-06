# Intrusive Visitor

Let's get ourselves acquainted with the visitor design pattern. One situation that we're going to simulate is the idea of printing or indeed evaluating later on a tree of numerical expressions.

Like so:

```java
abstract class Expression {}
```

Initially we're going to have two types of expressions:

```txt
// 1 + 2
```

The expression above consists in two components, a `DoubleExpression` and an `AdditionExpression`.

```java
class DoubleExpression extends Expression 
{
    private double value;

    public DoubleExpression(double value) 
    {
        this.value = value;
    }
}

class AdditionExpression extends Expression 
{
    private Expression left, right;

    public AdditionExpression(Expression left, 
                                Expression right) 
    {
        this.left = left;
        this.right = right;
    }
}
```

Let's sort out how we can define an expression like `1 + (2 + 3)` in a demo:

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        // 1 + (2 + 3)
        AdditionExpression addExp = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)
            )
        );
    }
}
```

We've just defined it in object-oriented form, what if we want to print it out as `(1 + (2 + 3))`, how do we go about adding functionality to every single element in the hierarchy? Currently, we have two types of `Expression`, but we might have ten components inheriting from it. How do we add the **print** functionality?

The most simple approach is what's called an _intrusive visitor_. And by intrusive, we mean that we're going to jump into classes we've already written, and already tested maybe, and we're going to modify the entire hierarchy. This is something you typically want to avoid because it violates the open closed principle and because it also violates the single responsibility principle. Remember, if we have a concern such as printing, for example, it should probably be in a separate class, not spread throughout ten, twenty, however many classes in the hierarchy.

We're going to do it in the bad way first, because is important to you grasp the idea of how you would implement intrusive printing in our example.

Okay, we must go into our **abstract base class** and add, and abstract void method called `print()` which takes a `StringBuilder` as a parameter.

```java
abstract class Expression
{
    abstract void print(StringBuilder sb);
}

class DoubleExpression extends Expression 
{
    // ...

    @Override
    public void print(StringBuilder sb) 
    {
        sb.append(value);
    }
}

class AdditionExpression extends Expression 
{
    // ...

    @Override
    public void print(StringBuilder sb) 
    {
        sb.append("(");
        left.print(sb);
        sb.append("+");
        right.print(sb);
        sb.append(")");
    }
}
```

Now we can test it:

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        // 1 + (2 + 3)
        AdditionExpression addExp = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)
            )
        );

        StringBuilder sb = new StringBuilder();
        addExp.print(sb);
        System.out.println(sb);
    }
}
```

As an output, we get:

```txt
(1.0+(2.0+3.0))
```

We got what we wanted, but there is a huge problem with this entire approach, it is currently breaking both the open-closed principle by changing existing classes, and the single responsibility principle because **printing** is a separate concern thus you might want to put all the printing logic for all the elements in the hierarchy into a separate class.
