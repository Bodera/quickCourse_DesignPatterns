# Classic Visitor (double dispatch)

Now we're going to implement the so-called double dispatch approach, which is the classic implementation of the visitor. By classic, we mean it's what most people actually do in real world.

It's time to intrude into the base `Expression` class, once and for all.

Let's suppose we've some interface called `ExpressionVisitor`, that is the interface that every visitor must implement in our application. So if we want to print all the expressions, we make an `ExpressionPrinter` visitor which implements `ExpressionVisitor`, and if we want to evaluate all the expressions, we make an `ExpressionEvaluator` visitor which also implements `ExpressionVisitor`, and so on and so forth.

For our example let's say that this new interface is pervasive, any visitor has to implement it.

```java
interface ExpressionVisitor {}

abstract class Expression
{
    abstract void accept(ExpressionVisitor ev);
}
```

Every single element in the hierarchy now has to accept an `ExpressionVisitor`. Moving on, we have to implement this method in every single element in the hierarchy in the same way.

```java
class DoubleExpression extends Expression 
{
    // ...

    @Override
    public void accept(ExpressionVisitor ev) 
    {
        ev.visit(this);
    }
}

class AdditionExpression extends Expression 
{
    // ...

    @Override
    public void accept(ExpressionVisitor ev) 
    {
        ev.visit(this);
    }
}
```

It's about time to define the `visit()` method in our `ExpressionVisitor` interface. You might have realized that we need to define two methods, one which takes an `AdditionExpression` and one which takes a `DoubleExpression` as a parameter.

```java
interface ExpressionVisitor 
{
    void visit(DoubleExpression de);
    void visit(AdditionExpression ae);
}
```

Why did we just do all of this? Well, now that we've propagated the `accept()` method throughout the hierarchy, this means that we can extend the entire hierarchy using any number of visitors. So as soon as we have a class which implements the `ExpressionVisitor` interface, you can stick it into the entire hierarchy and have it go through it and, well, accumulate values for example, if that's what you want to do.

Now we can rewrite the `ExpressionPrinter` class like so:

```java
class ExpressionPrinter implements ExpressionVisitor 
{
    private StringBuilder sb = new StringBuilder();

    @Override
    public void visit(DoubleExpression de) 
    {
        sb.append(de.getValue());
    }

    @Override
    public void visit(AdditionExpression ae) 
    {
        sb.append("(");
        ae.getLeft().accept(this);
        sb.append("+");
        ae.getRight().accept(this);
        sb.append(")");
    }

    @Override
    public String toString() 
    {
        return sb.toString();
    }
}
```

The upside of this approach is we're now overloading on the actual argument effectively. The only way that we can actually overload on the argument and get it to work at runtime is through the double dispatch approach.

Now we're going to run the program and see what happens.

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        AdditionExpression e = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);
        System.out.println(ep.toString());
    }
}
```

As an output we get:

```txt
(1.0+(2.0+3.0))
```

Remember when we say that the fact of adding new visitors is not a particular problem because it doesn't require any modification of the hierarchy? Well, let's suppose we decide to evaluate the final numeric expression result. So for a given `1 + (2 + 3)` we expect a `6` as a result.

Let's define a class called `ExpressionCalculator` which implements the `ExpressionVisitor` interface.

```java
class ExpressionCalculator implements ExpressionVisitor 
{
    public double result;

    @Override
    public void visit(DoubleExpression de) 
    {
        result = de.getValue();
    }

    @Override
    public void visit(AdditionExpression ae) 
    {
        ae.getLeft().accept(this);
        double leftResult = result;

        ae.getRight().accept(this);
        double rightResult = result;

        result = leftResult + rightResult;
    }
}
```

Let's try it out:

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        AdditionExpression e = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);

        ExpressionCalculator ec = new ExpressionCalculator();
        ec.visit(e);

        System.out.println(ep + " = " + ec.result);
    }
}
```

As an output we get:

```txt
(1.0+(2.0+3.0)) = 6.0
```

This demo basically shows the classic approach, the double dispatch approach that most people actually use when they need a visitor to sort of augment the behaviors of a hierarchy of some kind. In our case, we are rigidly bound to a hierarchy of different expressions, having a fixed definition of every single `visit()` method for the expression. In that way, if you were to introduce another kind of expression, like a `SubtractionExpression`, you'd be forced to implement `accept()` and inside it, you would be expected to call `visitor.visit(this)`, as a result the `ExpressionVisitor` would be forced to create another overload with a new argument for `SubtractionExpression`.

That's it, the double dispatch visitor actually enforces this idea of tight coupling to a particular hierarchy. The remaining question is whether it's good or not. In certain cases that might be a bit too rigid, sometimes you might want to ignore a particular element in the hierarchy and, in such a case, you need a different kind of visitor.
