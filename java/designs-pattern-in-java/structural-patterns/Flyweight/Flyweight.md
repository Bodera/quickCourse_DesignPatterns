# Flyweight

##  #1 Example - String repetition

Let's begin by looking at a very simple kind of illustration of the flyweight design pattern. Suppose that you have some sort of massively multiplayer online game with tons of users.

```java
class User
{
    private String fullName;

    public User(String fullName)
    {
        this.fullName = fullName;
    }
}
```

This might seem sensible to you but if you think about it, it's quite inefficient because if you have millions of users, you have millions of repetitions.

```java
class Demo
{
    public static void main(String[] args)
    {
        User user1 = new User("John Doe");
        User user2 = new User("Jane Doe");
    }
}
```

For sure those two are different strings, so we can't simply store them in a single location because they're not the same, they however share a common part *"Doe"*. In a scenario where you're using just ordinary UTF-8 for example, you're wasting five bytes by storing both of them, which is inefficient.

Flyweight is essentially a space optimization technique that helps you store large quantities of similar objects and in this case what we have is a similar object - the user's name. So, how will you build the flyweight design pattern for the users names while preserving this API of the `User` constructor method?

Essentially what you would do is keep some sort of static list of every single individual name to basically build a large cache of these names. You also would have a bunch of non-static indexes which the current user refers to these pointers into the list, and then write a utility function which is going to either get a value from the strings collection or it's going to add the value to this list. Whatever happens it's going to return us the index of the element that we're looking for. 

```java
class UserFlyweight
{
    private static List<String> namesCache = new ArrayList<>();
    private int[] namesReference;

    public UserFlyweight(String fullName)
    {
        Function<String, Integer> getOrAdd = (String name) -> {
            int idx = namesCache.indexOf(name);

            if (idx != -1) return idx;
            else {
                namesCache.add(name);
                return namesCache.size() - 1;
            }
        };

        namesReference = Arrays.stream(fullName.split(" "))
            .mapToInt(str -> getOrAdd.apply(s))
            .toArray();
    }
}
```

Now:

```java
class Demo
{
    public static void main(String[] args)
    {
        UserFlyweight user1 = new UserFlyweight("John Doe");
        UserFlyweight user2 = new UserFlyweight("Jane Doe");
    }
}
```

## #2 Example - Text formatting

For this next demonstration let's declare a few definitions for a bunch of formatted text styles and then try to apply it in different ways.

```java
class FormattedText
{
    private String plainText;

    public FormattedText(String plainText)
    {
        this.plainText = plainText;
    }
}
```

Seems pretty reasonable so far. What we can do now if we want to capitalize particular parts of the original text? 

One dumb and memory wasteful approach would be to make a boolean flag for every single letter inside `plainText` specifying whether we actually want that letter to be capitalized.

Like this:

```java
class FormattedText
{
    private String plainText;
    private boolean [] capitalized;

    public FormattedText(String plainText)
    {
        this.plainText = plainText;
        this.capitalized = new boolean[plainText.length()];
    }

    public void capitalize(int start, int end)
    {
        for (int i = start; i < end; ++i)
            capitalized[i] = true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plainText.length(); ++i)
        {
            if (capitalized[i])
                sb.append(Character.toUpperCase(plainText.charAt(i)));
            else
                sb.append(plainText.charAt(i));
        }
        return sb.toString();
    }
}
```

```java
class Demo
{
    public static void main(String[] args)
    {
        FormattedText ft = new FormattedText("Hello World!");
        ft.capitalize(0, 5);
        System.out.println(ft);
    }
}
```

```bash
> HELLO World!
```

We can see that we've got the output that we expected, however using a problematic approach to say at least. That's because we're allocating a boolean for every single character inside `plainText` even though it would be a lot more efficient to just specify the start and end of the capitalization range.

This time we're going to implement a more object-oriented and organized fashion strategy for the `capitalize()` method.

In order to add extra flexibility let's start by defining a new class called `FormattedTextImproved` and inside it, we will have a nested class that actually defines a text range. A `TextRange` is simply a specification of the start and end position, as well as any kind of formatting that we want to perform that you can manipulate as boolean flags. `TextRange` is also going to have a utility function which determines whether a particular position is covered by the original range. 

This `TextRange` is something that we're going to expose outside `FormattedTextImproved` API whenever somebody wants to do any kind of formatting. Now, besides `plainText`, what we're going to add is an unlimited number of `TextRange` to be used for different kinds of formatting. In order to expose the `TextRange` we can add another utility function called `getRange()`.

```java
class FormattedTextImproved
{
    private String plainText;
    private List<TextRange> formatting = new ArrayList<>();

    public FormattedTextImproved(String plainText)
    {
        this.plainText = plainText;
    }

    public TextRange getRange(int start, int end)
    {
        TextRange range = new TextRange(start, end);
        formatting.add(range);
        return range;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plainText.length(); ++i)
        {
            char c = plainText.charAt(i);
            for (TextRange range : formatting)
            {
                if (range.covers(i) && range.capitalize)
                    c = Character.toUpperCase(c);

                if (range.covers(i) && range.bold)
                    c = '\u001B[1m' + c + '\u001B[0m';

                if (range.covers(i) && range.italic)
                    c = '\u001B[3m' + c + '\u001B[0m';

                if (range.covers(i) && range.underline)
                    c = '\u001B[4m' + c + '\u001B[0m';

                sb.append(c);
            }
            return sb.toString();
        }
    }

    public class TextRange
    {
        private int start;
        private int end;
        public boolean capitalize, bold, italic, underline;

        public TextRange(int start, int end)
        {
            this.start = start;
            this.end = end;
        }

        public boolean covers(int position)
        {
            return position >= start && position < end;
        }
    }
}
```

```java
class Demo
{
    public static void main(String[] args)
    {
        FormattedTextImproved fti = new FormattedTextImproved("Hello World!");

        fti.getRange(6, 11).capitalize = true;

        System.out.println(fti);
    }
}
```

```bash
> Hello WORLD!
```

The flyweight on this example is the `TextRange` class, that's the class that save us memory on the implementation of the `FormattedTextImproved` class. As we saw we get the expected output without having this massive boolean array indicating every part of formatting.

The flyweight design pattern is precisely the mechanism that helps us here to conserve memory and also to get a better and more palatable API.