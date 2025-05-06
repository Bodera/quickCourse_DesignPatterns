# Reflective Visitor

Let's continue working on our `Expression` example by getting rid of the intrusiveness. We'll now work on a new class `ExpressionPrinter`.

```java
class ExpressionPrinter {}
```

One thing that we generally cannot do is create overloads like so:

```java
// Thats is not recommended
class ExpressionPrinter 
{
    public static void print(DoubleExpression e, StringBuilder sb) {}
    public static void print(AdditionExpression e, StringBuilder sb) {}
}
```

That's just not viable. What we're going to do is to generalize both of these methods into a single one which takes an `Expression` as a parameter.

```java
class ExpressionPrinter 
{
    public static void print(Expression e, StringBuilder sb) {}
}
```

That's the best that we can manage so far.

The kind of visitor that we're making right now is called a _reflective visitor_. The reason why it's reflective is because, unfortunately, we're going to have to use reflection to figure out exactly what type of `Expression` we actually got. The idea consists in checking the class type of the `Expression` 

```java
class ExpressionPrinter 
{
    public static void print(Expression e, StringBuilder sb) 
    {
        if (e.getClass() == DoubleExpression.class) 
        {
            DoubleExpression de = (DoubleExpression)e;
            sb.append(de.value);
        }
        else if (e.getClass() == AdditionExpression.class) 
        {
            AdditionExpression ae = (AdditionExpression)e;
            sb.append("(");
            print(ae.left, sb); // recursive call
            sb.append("+");
            print(ae.right, sb); // recursive call
            sb.append(")");
        }
    }
}
```

> Do you know when to use `instanceOf` and when to use `getClass()`?

We need to make sure that the members `value`, `left`, and `right` are public in order for reflection to work.

Now adapting our `Demo` class:

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        Expression e = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));
        StringBuilder sb = new StringBuilder();
        ExpressionPrinter.print(e, sb);
        System.out.println(sb);
    }
}
```

As an output, we get:

```txt
(1.0+(2.0+3.0))
```

We're still getting the correct results, the ones that we expected. So we're improving our implementation. However, there are plenty of issues here, first our code performance is not optimal, the usage of `getClass()` or even `instanceOf` for type checking on each call in a recursive way is a performance hit. Another thing that we've lost is the verification that every single element in the hierarchy has been covered, so we are susceptible to forget to add a new type of expression to our visitor. How can we enforce our visitor to cover every single case? Unfortunately, to solve this issue we're going to intrude our visitor into the hierarchy, the catch here is that we're going to do this just once, after that we can build extra visitors like `ExpressionPrinter` or `ExpressionEvaluator` or whatever without any further intrusions.
