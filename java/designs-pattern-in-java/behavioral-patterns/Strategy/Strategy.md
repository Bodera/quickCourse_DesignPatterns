# Strategy

## Dynamic Strategy

Let's imagine that you're making some sort of publishing system which is supposed to output different formats such as `HTML`, or `Markdown`.

```java
enum OutputFormat {
    MARKDOWN, HTML
}
```

Now we can define what kind of constructs we want to build, let's take a list for start, and for sure lists are defined differently in `Markdown` and `HTML`:

```
<ul> 
    <li>Item 1</li> 
    <li>Item 2</li> 
    <li>Item 3</li>
</ul>

- Item 1
- Item 2
- Item 3
```

Our goal is to be able to switch from one output to another. To do that we're going to do is to define the strategy interface for actually doing a list in both `Markdown` and `HTML`.

```java
interface ListStrategy 
{
    void start(StringBuilder sb);
    void addListItem(StringBuilder sb, String item);
    void end(StringBuilder sb);
}
```

This is the interface that you would expect to implement for both `Markdown` and `HTML`, and any kind of new output system that you want to support. 

Notice that for `Markdown` there is no start and no end for lists, knowing that what we can do is stick default empty bodies for those methods.

```java
interface ListStrategy 
{
    default void start(StringBuilder sb) {}
    void addListItem(StringBuilder sb, String item);
    default void end(StringBuilder sb) {}
}
```

Okay now we're ready to build different strategies for making a list in both `Markdown` and `HTML`.

```java
class MarkdownListStrategy implements ListStrategy 
{
    @Override
    public void addListItem(StringBuilder sb, String item) {
        sb.append(" - ").append(item)
            .append(System.lineSeparator());
    }
}

class HTMLListStrategy implements ListStrategy 
{
    @Override
    public void start(StringBuilder sb) {
        sb.append("<ul>").append(System.lineSeparator());
    }

    @Override
    public void addListItem(StringBuilder sb, String item) {
        sb.append("  <li>")
            .append(item)
            .append("</li>")
            .append(System.lineSeparator());
    }

    @Override
    public void end(StringBuilder sb) {
        sb.append("</ul>").append(System.lineSeparator());
    }
}
```

Next step is the actual text process, we'll define a class which takes a bunch of text and basically tries to make a list out of it.

```java
class TextProcessor 
{
    private StringBuilder sb = new StringBuilder();
    private ListStrategy listStrategy;

}
```

The kind of strategy we're going to use depends on the output format, and that is going to be determined at runtime.

```java
class TextProcessor 
{
    private StringBuilder sb = new StringBuilder();
    private ListStrategy listStrategy;

    public TextProcessor(OutputFormat format) 
    {
        setOutputFormat(format);
    }

    public void setOutputFormat(OutputFormat format) 
    {
        switch (format) {
            case MARKDOWN:
                listStrategy = new MarkdownListStrategy();
                break;
            case HTML:
                listStrategy = new HTMLListStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown output format");
        }
    }
}
```

Now before we finish let's define a method for actually adding items to the list.

```java
class TextProcessor 
{
    // ...

    public void appendList(List<String> items)
    {
        listStrategy.start(sb);
        for (String item : items) {
            listStrategy.addListItem(sb, item);
        }
        listStrategy.end(sb);
    }
}
```

And that's it! Let's also define some helper methods that enable us to clear the string builder and get the final result.

```java
class TextProcessor 
{
    // ...

    public void clear() {
        sb.setLength(0);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
```

Let's try it out:

```java
public class Demo
{
    public static void main(String[] args)
    {
        TextProcessor tp = new TextProcessor(OutputFormat.MARKDOWN);
        tp.appendList(List.of("Item 1", "Item 2", "Item 3"));
        System.out.println(tp);

        tp.clear();
        tp.setOutputFormat(OutputFormat.HTML);
        tp.appendList(List.of("Item A", "Item B", "Item C"));
        System.out.println(tp);
    }
}
```

As an output we get:

```
 - Item 1
 - Item 2
 - Item 3

<ul>
  <li>Item A</li>
  <li>Item B</li>
  <li>Item C</li>
</ul>
```

The takeaway here is that the strategy design pattern essentially allows us to define the different parts of an algorithm that we want to reuse in different ways, by defining different strategies that you can plug into a particular object and the base class will delegate the actual work to the strategy. This pattern is very convenient to use especially if you're using dependency injection.

## Static Strategy

The dynamic `ListStrategy` that we've looked at is very flexible because it allows you to switch from one strategy to another at runtime. Now what if you don't want this, what if you want to bake in the exact strategy that you want to use at compile time? The static strategy is that exact kind of approach.

The tricky here is to adapt the `TextProcessor` class to use generics and specifying the argument type, like so:

```java
class TextProcessor<LS extends ListStrategy> 
{
    private StringBuilder sb = new StringBuilder();
    private LS listStrategy;
    // ...
}
```

This states that we're going to provide an argument and keep it as a variable. This is where things get a bit unpleasant because of Java's type erasure, we just can't state something like this:

```java
private LS listStrategy = new LS();
```

We need to specify somebody to not just provide the generic argument but also to provide a supplier inside the constructor, that's the only way that you can persist the type information.

So we get rid of the whole `setOutputFormat()` method, because everything is now going to be dependent on compile time arguments therefore less flexible, and do this:

```java
class TextProcessor<LS extends ListStrategy> 
{
    private StringBuilder sb = new StringBuilder();
    private LS listStrategy;

    public TextProcessor(Supplier<? extends LS> constructor)
    {
        this.listStrategy = constructor.get();
    }

    // ...
}
```

Now let's try it out:

```java
public class Demo
{
    public static void main(String[] args)
    {
        TextProcessor<MarkdownListStrategy> tp = 
            new TextProcessor<>(MarkdownListStrategy::new);
        tp.appendList(List.of("Item 1", "Item 2", "Item 3"));
        System.out.println(tp);

        TextProcessor<HTMLListStrategy> tp2 = 
            new TextProcessor<>(HTMLListStrategy::new);
        tp2.appendList(List.of("Item A", "Item B", "Item C"));
        System.out.println(tp2);
    }
}
```

As an output we get:

```
 - Item 1
 - Item 2
 - Item 3

<ul>
  <li>Item A</li>
  <li>Item B</li>
  <li>Item C</li>
</ul>
```
