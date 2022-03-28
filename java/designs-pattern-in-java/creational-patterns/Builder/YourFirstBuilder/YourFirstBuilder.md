# Your First Builder

We will come back to the example that we did in the previous lesson and kind of build up on it, introducing an even more dedicated construction. So even though we're working with HTML righ now we're still using Stings, so we migh want to go the Object-Oriented route and actually define some sort of ordinary classes essentially for storing different HTML tags and related stuff that's in them. Let's do exactly that and hopefully this going to help us make a different builder which can do a lot more and can be a lot more general.

So to start we're going to model a single HTML element such as a list tag for example inside a separate class. Let's name our class as `HtmlElement` and it will have two `Strings` fields that are `name` and `text` where name will be the abreviated name of the tag such as `li` for *list item*, and text would be whatever is in between the tags provided, it is a text and not some other tag because if it has some other tag we're going to store it in a different kind of field. We're also going to have an `ArrayList` of `HtmlElement` so this is going to be a recursive structure afterall essentially you can have an element with any number of elements inside it to almost infinite depth, and we'll name it as `elements`. And of course we're going to add a few utility things, let's create a `int` field that stores the size of the indentation because poor indentation sucks and we're going to specify how many characters we're going to indent by at each level of depth. 

```java
import static java.lang.String.format;
import java.util.ArrayList;
import java.util.stream.Stream;

class YourFirstBuilder {

    class HtmlElement
    {
        public String name, text;
        public ArrayList<HtmlElement> elements = new ArrayList<>();
        private final int indentSize = 2;
        private final String newLine = System.lineSeparator();
    }

    public static void main(String[] args) 
    {
        String hello = "hello";
        System.out.println(format("<p>%s</p>%n", hello));

        String [] words = {"hello", "world"};
        StringBuilder sb = new StringBuilder();
        sb.append(format("<ul>%n"));
        
        Stream.of(words).forEach(word -> {
            sb.append(format("  <li>%s</li>%n", word));
        });
        sb.append("</ul>");

        System.out.println(sb);
    }
}
```

Now let's add a couple of constructors, one will be left empty, and a different one will initializes the name as well as the text. The fun part about this is how do you actually build the whole thing, how do you turn this HTML element into a String. It isn't easy shall we say, because the algorithm has to take care of both the fact that you can have a text, but you can also have nested elements in which case the whole thing has to be recursive. So obviously our goal here needs to have some sort of `toString()` thas does something nice but instead of returning a `super.toString()` we want to do a recursive implementation which goes in-depth into the elements and their elements and so on. So we have our `toStringImpl(int depth)` which takes an indent level aka the level o depth as we're traversing this tree if you will and it goes into the whole thing and actually prints it nicely. This is a discussion that we aren't going to delve because it's not directly relevant to the Builder design pattern, of course it is relevant but is more about you know, semantics. Well so `toString()` will simply return `toStringImpl(0)`. Study the code of `toStringImpl()` but be aware that there is nothing critical going on there.

```java
private toStringImpl(int depth)
{
    StringBuilder sb = new StringBuilder();
    String indent = join("", Collections.nCopies(depth * indentSize, " "));
    sb.append(format("%s<%s>%s", indent, name, newLine));
    
    if (null != text && !text.isEmpty())
    {
        sb.append(join("", Collections.nCopies(indentSize * (depth + 1), "")))
            .append(text)
            .append(newLine);

        elements.forEach(element -> sb.append(element.toStringImpl(depth + 1)));

        sb.append(format("%s</%s>%s", indent, name, newLine));
        return sb.toString();
    }
}
```

Essentially what's happening is, we created a `StringBuilder` so behing the scenes we still creating a `String`, and we're not just exposing it anymore, we're exposing a different builder that you're going to see in just a moment. What we're trying to do here is to get the indentation correct and this is why all the calls to `Collections.nCopies()` which returns a immutable list that contains *n* copies of determined object. Then we've appended the tags with the indentation within the line separator, if there is some text in here then we append the `text` with a `newLine` but we also check recursively for the elements that are inside the tag (when we iterate through the elements to print children as well). So this whole set up make sure that whatever type of element we have the `print.out` is nice and neat.

Now the question is: what's next? The next part it to build an HTML Builder which is dedicated to build up HTML elements. We're not going to have too much functionality in our HTML builder because it's just a small demo, so let's just see how we can set it up.

The idea of our `HtmlBuilder` is that it has some root element, the root HTML element which contains everything and whenever somebody actually adds elements using the builder they are simply augmenting this through element. We will have a `rootName` field because if we want to reset the builder later on and still preserve the name of the root it will be here, and also we will have the HTML root itself, by default we will use the empty constructor that we can subsequently build up and add more things to it. So for the constructor of the builder we're going to take the `rootName`, we've already talked that it can be something like `<p>` or `<ul>` or `<li>`, is the top level element name, we've also assign the name of the root HTML element.

Now we want some sort of API for actually adding a child to this so now it can go all to any kind of depth but here we'll just going to do it at the top level meaning we're going to have our root then we're going to add children to that root. We're not going to allow to add children of children because that would require more changes to the builder. We have `addChild()` that adds child to the current root, and the child element has to specify two things it has to specify the name and the text. We can also have all sort of utility fuctions, let's suppose we want to clear the `HtmlBuilder` for reseting the builder so it can be used the function `clear()`.

Still missing the code that generates the actual object from the builder because if you remember a builder is like construction element it has to at some point have a method which returns the final object. Since we're returning a `String` what we can do is, we can actually just do an override of `toString()` where we just calls the `toString()` of our root element. Remember we had `toStringImpl()` implementation which actually does all the work and it's exposed by `root.toString()` so this is how the whole thing works.

```java
class HtmlBuilder
{
    private String rootName;
    private HtmlElement root = new HtmlElement();

    public HtmlBuilder(String rootName)
    {
        this.rootName = rootName;
        root.name = rootName;
    }

    public void addChild(String childName, String childText)
    {
        HtmlElement child = new HtmlElement(childName, childText);
        root.elements.add(child);
    }

    public void clear()
    {
        // keeps the type of root that it is but still resets the actual content.
        root = new HtmlElement();
        root.name = rootName; 
    }

    @Override
    public String toString()
    {
        return root.toString();
    }
}
```

Now how we can put things to work together?

```java
public static void main(String[] args)
{
    HtmlBuilder builder = new HtmlBuilder("ul");
    builder.addChield("li", "hello");
    builder.addChield("li", "world");
    System.out.println(builder.toString());
}
```

As output we get:

```txt
<ul>
  <li>
    hello
  </li>
  <li>
    world
  </li>
</ul>
```

We got an unordered list with list items nicely formatted and nicely indented text which is what we wanted in the first place. So what we've done here on this particular example is made a dedicated builder, specifically for HTML that you could subsequently augment with more HTML specific things, try it out.
