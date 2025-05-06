# Acyclic Visitor

One of the downsides of the classic double dispatch approach is the tight coupling between the expression and the visitors. In particular, the problem is that we have this cyclic dependency between the **visitor** and the **expressions**.

You'll notice that as soon as we start adding a new expression, what happens is that we end up implementing a new `accept()` method, which leads to a new `visit()`. And this cyclic dependency isn't always great, sometime we want a little more flexibility.

Robert C. Martin (Uncle Bob) presented us with the idea of an _acyclic visitor_ and that's what we're going to implement here.

Unfortunately, in Java, what happens with the acyclic visitor is that we end up instead of just having a single interface, we end up having a lot of interfaces for visiting the different parts of the component. Essentially, it goes like the interface segregation principle in practice, the argument is that instead of sticking the _visitation_ for every single kind of element in the hierarchy, you don't concern yourself with hierarchies anymore. Leaving you without this tight requirement on having every element in the hierarchy implementing the something.

We start by defining an interface called `Visitor`, which will be a **marker** interface without any members at all. The only reason why we're going to have this is so that subsequently we can pass it in as an argument.

```java
interface Visitor {} // marker interface
```

The idea is the following, we're going to make different visitors for each `Expression` types and indeed we can make a visitor for the base abstract class as well. That's essentially interface segregation.

```java
interface Visitor {} // marker interface

interface ExpressionVisitor extends Visitor 
{
    void visit(Expression obj);
}
```

We define a visitor like this not just for the `Expression`, but also for `DoubleExpression`, `SumExpression`, and any kind of other expressions that you might have.

```java
interface Visitor {} // marker interface

interface ExpressionVisitor extends Visitor 
{
    void visit(Expression obj);
}

interface DoubleExpressionVisitor extends ExpressionVisitor 
{
    void visit(DoubleExpression obj);
}

interface SumExpressionVisitor extends ExpressionVisitor 
{
    void visit(SumExpression obj);
}
```

You may have noticed that the `visit()` method is not part of some interface that is extended, so even though we have the base interface called `Visitor`, it doesn't have `visit()` defined on it, so each of these `visit()` implementations are kind of voluntary, they are not related with each other, thus they are independent.

What happens now is that every single class has a possibility of implementing an `accept()` method which in turn takes a `Visitor` (the marker interface).

Check it out:

```java
abstract class Expression
{
    abstract void accept(Visitor visitor)
    {
        if (!(visitor instanceof ExpressionVisitor))
            return;

        ((ExpressionVisitor) visitor).visit(this);
    }
}
```

That's how we implement the `accept()` method in the acyclic visitor.

```java
class DoubleExpression extends Expression 
{
    // ...

    @Override
    abstract void accept(Visitor visitor)
    {
        if (!(visitor instanceof DoubleExpression))
            return;

        ((DoubleExpression) visitor).visit(this);
    }
}

class SumExpression extends Expression 
{
    // ...

    @Override
    abstract void accept(Visitor visitor)
    {
        if (!(visitor instanceof SumExpression))
            return;

        ((SumExpression) visitor).visit(this);
    }
}
```

Noticed how the `Expression` abstract class is the only one without the `@Override` annotation? It's really up to your scenario whether to work with a base class implementation or not, sometimes it might not make sense at all to have a visitor for an abstract class. The upside of this approach is that a visitor for an abstract class can serve as a catch-all scenario, in case you didn't implement a visitor for a particular type, you still want some general sort of catch-all handling of the visitor.

Okay, now we can build the `ExpressionPrinter` once again, implementing only the interfaces that we're actually interested in (here it will be only `DoubleExpressionVisitor` and `SumExpressionVisitor`).

```java
class ExpressionPrinter 
    implements DoubleExpressionVisitor, 
                SumExpressionVisitor
{
    private StringBuilder sb = new StringBuilder();

    @Override
    public void visit(DoubleExpression de) 
    {
        sb.append(de.getValue());
    }

    @Override
    public void visit(SumExpression se) 
    {
        sb.append("(");
        se.getLeft().accept(this);
        sb.append(" + ");
        se.getRight().accept(this);
        sb.append(")");
    }

    @Override
    public String toString() 
    {
        return sb.toString();
    }
}
```

Now that we have this kind of setup, we can continue using the whole structure as before.

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        SumExpression e = new SumExpression(
            new DoubleExpression(1), 
            new SumExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);

        System.out.println(ep);
    }
}
```

As an output, we get:

```txt
(1.0 + (2.0 + 3.0))
```

The acyclic visitor really shines in a scenario where you decide that you don't want to work with `DoubleExpression` anymore, you just remove the implementation and the proper `@Override` for it on your `ExpressionPrinter` class, and it's still going to work.

That's a plus flexibility we get in the sense that we might want to temporarily disable a part of the visitor, or we may don't want to implement if it doesn't make sense in the first place.

The real price that we are paying for all this is to end up following the interface segregation principle too hard which might be a bit tedious. But hey, if you're never going to visit a particular class, you don't need an interface for it.
