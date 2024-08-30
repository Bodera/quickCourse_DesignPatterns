# Façade

Let's take look at a façade from a practical perspective. To build something that's reasonably low-level in order to see how to use this low-level API and how to build a proper façade around it.

We're going to move to the financial sphere and, we're going to build a console, a terminal which outputs huge amounts of information that comes from the stock market.

First start by defining a `Buffer` that stores the data that is actually coming in to be presented on the screen. So we're going to define an array of characters `characters` and also another member which indicates how much space the buffer takes horizontally (the `lineWidth`) so we can know where the line breaks is expected to be. To specify the size of the buffer we're going to create a constructor which takes `lineWidth` and `lineLength` and initializes the `characters` by multiplying `lineWidth` by `lineLength`, by doing this we're actually representing a two-dimensional grid (or matrix) as a one-dimensional array.

We can also specify a low-level way of retrieving the data from the buffer, like a `charAt()` method which converts 2D grid coordinates into a 1D array index.

```java
class Buffer
{
    private char[] characters;
    private int lineWidth;

    public Buffer(int lineLength, int lineWidth)
    {
        this.characters = new char[lineLength * lineWidth];
        this.lineWidth = lineWidth;
    }

    /**
     * The method takes three parameters:
     * @param x: The x-coordinate (horizontal position) in the 2D grid.
     * @param y: The y-coordinate (vertical position) in the 2D grid.
     * @param c: The character to set at the specified coordinates.
     *
    **/
    public void setChar(int x, int y, char c)
    {
        characters[charAt(x, y)] = c;
    }

    /**
     * The method takes two parameters, x and y, which are the coordinates of the character to retrieve.
     * 
     * y * lineWidth + x: This calculates the index in the one-dimensional array that corresponds to the 2D coordinates (x, y) in a grid.
     * lineWidth: Represents the number of characters in each row (or line width) of the grid.
     * y * lineWidth: Calculates the starting index of the row y in the one-dimensional array.
     * + x: Adds the offset within the row to get the exact index.
     *
    **/
    public char charAt(int x, int y)
    {
        return characters[y * lineWidth + x];
    }
}
```

These buffers they can be presented in many locations continuously, and also the buffer can be bigger than the viewport that's presenting it, so you might have another class for representing the `Viewport`. A `Viewport` takes a `Buffer` that it needs to present, but it also takes the `height` and the `width` of the viewport itself. And it also takes the offsets of how we want to address into the buffer because we might want to show only a portion of the buffer, and in this case we need to specify the optional `x` and `y` offsets. And of course we have another implementation of `charAt()` that takes these offsets into account.

```java
class Viewport
{
    private final Buffer buffer;
    private final int height;
    private final int width;
    private final int offsetX;
    private final int offsetY;

    public Viewport(Buffer buffer, int height, int width, int offsetX, int offsetY)
    {
        this.buffer = buffer;
        this.height = height;
        this.width = width;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public char charAt(int x, int y)
    {
        return buffer.charAt(x + offsetX, y + offsetY);
    }
}
```

Then finally you want to build the console where you simply keep a list of the active viewports. You also want to specify the within height of the entire console, remember that viewports can take different portions of the console and that's how you present market information from different sources, as a matter of fact it's even more complicated than that because typically what we do is use multiple monitors as well allowing us to spread out all the data around these monitors. For the sake of simplicity let's just define members `width` and `height` of the current console where we're going to initialize using the constructor call. Then next let's define some API to manipulate the viewports, such as a `addViewport()` method which takes a `Viewport` and adds it to the list of active viewports, we can also define a `clearScreen()` method which clears the entire console, and finally a `render()` method which prints the data in the viewports in the console.

```java
class Console
{
    private List<Viewport> viewports = new ArrayList<>();
    private int width, height;

    public Console(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void addViewport(Viewport viewport)
    {
        viewports.add(viewport);
    }

    public void clearScreen()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void render()
    {
        for (Viewport viewport : viewports)
        {
            for (int y = 0; y < viewport.height; y++)
            {
                for (int x = 0; x < viewport.width; x++)
                {
                    System.out.print(viewport.charAt(x, y));
                }
                System.out.println();
            }
        }
    }
}
```

This is the kind of pseudocode like implementation of the console system where we have three different low-level classes: the `Buffer` which stores the characters in a raw array and provides an API for accessing this array with the `charAt()` method, then we have a `Viewport` which is essentially a view into the `Buffer` at a particular offset and with a particular size as well providing also a `charAt()` method that takes the offsets into account, then finally we have the `Console` which has a rather unfriendly interface because of the list of `Viewport` and the dependency off the caller define the size of the console on the constructor. In most cases when the user actually wants to do something very simple with a console they don't really want to manually create the `Buffer`, neither the `Viewport`, they just want to make a `Console` which has a single `Viewport` which matches the `Console` and a single `Buffer` which matches the `Viewport`. All that initialized automatically.

Here's a demo of how we have to operate the `Console` so far:

```java
public class Demo
{
    public static void main(String[] args)
    {
        Buffer buffer = new Buffer(30, 25);
        Viewport viewport = new Viewport(buffer, 30, 25, 0, 0);
        Console console = new Console(30, 25);
        console.addViewport(viewport);
        console.render();
    }
}
```

The pain here is that the user should not be forced to go through all of this low-level API, and this is precisely where the façade design pattern comes in. It essentially provides a simplified API over a set of subsystems, and here we have the subsystem for the `Buffer`, `Viewport`, and `Console`, which replaces those bunch of lines with a single API which provides a default implementation. Certainly we need an API for going deep and editing the `Viewport`, etc. But let's keep it simple.

Let's move to the `Console` class and put all the implementation there like a factory method:

```java
class Console
{
    //...

    public static Console createConsole(int width, int height)
    {
        Buffer buffer = new Buffer(width, height);
        Viewport viewport = new Viewport(buffer, width, height, 0, 0);
        Console console = new Console(width, height);
        console.addViewport(viewport);
        return console;
    }
}
```

As a consequence now we have this façade:

```java
public class Demo
{
    public static void main(String[] args)
    {
        Console console = Console.createConsole(30, 25);
        console.render();
    }
}
```

As we've sawn a façade is to simply provide a simplified API because the user might not necessarily be interested in the fact that buffers and viewports even exist, they might not even care one bit about it. They just want the console DOS-like with a single console, and a single viewport, no embellishments, and by using this façade they're actually going to get it.
