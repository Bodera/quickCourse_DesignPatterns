# Adapter

Ok, so the process of adaptation isn't really anything that's particularly difficult, we're going to look at a very simple example where we are given one kind of API, and we are also given a different set of objects which are incompatible thus we have to provide some sort of compatibility between them.

First, let's define some basic geometric classes.

```java
class Point
{
    public int x, y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "Point{x:" + x + ",y:" + y + "}";
    }
}

class Line
{
    public Point start, end;

    public Line(Point start, Point end)
    {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString()
    {
        return "Line{start:" + start + ",end:" + end + "}";
    }
}
```

As you might have guessed what we're hinting at is the idea of being able to draw images either in pixel form as a set of points (Raster image) or in vector form as a set of lines (straight lines in our case).

So let's suppose that we have an API which allows us to draw things only using points. So points is the only thing that you have. But you're also given a bunch of vector objects, and you need to draw those somehow.

Let's prepare our demo real quickly.

```java
class Demo
{
    public static void main(String[] args)
    {

    }
}
```

For now let's assume that we're given a bunch of lines, so we're just going to make a bunch of these vector objects as they might be called. Essentially, first we've to define further classes related to these vector objects.

A `VectorObject` is anything that is just made up of a set of lines. So it should look like this:

```java
class VectorObject extends ArrayList<Line> {}
```

So, for example, if you want to draw a rectangle defined in vector form you would have a class called `VectorRectangle` which would extend object `VectorObject`. And in the constructor you would actually make up the rectangle out of the appropriate line.

```java
class VectorRectangle extends VectorObject
{
    public VectorRectangle(int x, int y, int width, int height)
    {
        Point bottomLeft = new Point(x, y);
        Point topRight = new Point(x + width, y + height);
        Point topLeft = new Point(x, y + height);
        Point bottomRight = new Point(x + width, y);

        add(new Line(bottomLeft, bottomRight)); // border-bottom
        add(new Line(bottomLeft, topLeft)); // border-left
        add(new Line(bottomRight, topRight)); // border-right
        add(new Line(topLeft, topRight)); // border-top
    }
}
```

That's a pretty simple example if you wanted to make a rectangle which consists of the four different coordinates.

So, having made this what we can do now? For example, let's pretend that we have a bunch of lines that we need to work with, a bunch of rectangles in fact. Like this:

```java
class Demo
{
    private final static List<VectorObject> vectorObjects 
     = new ArrayList<VectorObject>(Arrays.asList(
        new VectorRectangle(1, 1, 10, 10), 
        new VectorRectangle(3, 3, 6, 6)
    ));

    public static void main(String[] args)
    {

    }
}
```

Now let's suppose that the only API which we have for drawing things is something like this:

```java
public static void drawPoint(Point p)
{
    System.out.println(".");
}
```

For now, we're not really going to do proper two-dimensional rendering of points, we'll just simulate it on the command line. But the idea here is that we are given a bunch of vectors `vectorObjects`, but in actual fact we only have the API for drawing points.

That's put us in a bit of trouble because we don't have a new way of converting the vector forms into points. And this is precisely why you would build an **adapter**. For the adapter pattern essentially what you would do is build an adapter which takes some sort of line, remember that we have the line as the fundamental construct for vectors in our case, and them converts it into a bunch of points.

Let's take a look at how to implement the adapter pattern in Java. First what we need is a brand-new class, some kind of `LineToPointAdapter`. For a given line, this class takes the line and converts it into an array of corresponding points.

```java
class LineToPointAdapter extends ArrayList<Point>
{
    private static int count = 0;

    public LineToPointAdapter(Line line)
    {
        System.out.println(
        String.format("%d: Generating points for line [%d,%d]-[%d,%d] (no caching)",
            ++count, line.start.x, line.start.y, line.end.x, line.end.y));

        int left = Math.min(line.start.x, line.end.x);
        int right = Math.max(line.start.x, line.end.x);
        int top = Math.min(line.start.y, line.end.y);
        int bottom = Math.max(line.start.y, line.end.y);
        int dx = right - left;
        int dy = line.end.y - line.start.y;

        if (dx == 0)
        {
            for (int y = top; y <= bottom; ++y)
            {
                add(new Point(left, y));
            }
        }
        else if (dy == 0)
        {
            for (int x = left; x <= right; ++x)
            {
                add(new Point(x, top));
            }
        }
    }
}
```

Well that's really not trivial conversion. First, we want to keep a count of the number of points that we've actually produced, so we've used a `count` variable. Each call of the constructor will produce a new point and print it to the console. Notice we're telling that this is a *no caching* implementation because that's something that is going to be relevant later on. The problem we're facing right now is that we're generating temporary information and storing it in our `ArrayList`.

The idea for this algorithm is that we grab the line passed as an argument to it, then figure out the left, right, top, bottom, delta x, and delta y parts. Next we generate a set of points, one per integral unit. In this case we're only using integers for everything, so that's fine.

We've generated our points, add them to ourselves because remember the `LineToPointAdapter` actually extends `ArrayList<Point>`, so we have the `add` method inherited which we can use to add points to ourselves.

Having all this set up what we can try drawing all of our objects using this adapter. Like this:

```java
private static void draw()
{
    for (VectorObject vector : vectorObjects)
    {
        for (Line line : vector)
        {
            LineToPointAdapter adapter = new LineToPointAdapter(line);
            adapter.forEach(Demo::drawPoint);
        }
    }
}
```

This method goes through every single `VectorObject` inside the collection `vectorObjects` then for each line, because a vector is made of a bunch of lines, it instantiates a new adapter that performs the conversion and finally calls the `drawPoint` method to actually draw the points. 

You should end with something similar to this:

```java
public static void main(String[] args)
{
    draw();
}
```

That's it! Take a look at the output we are getting.

```bash
1: Generating points for line [1,1]-[11,1] (no caching)
.
2: Generating points for line [1,1]-[1,11] (no caching)
.
3: Generating points for line [11,1]-[11,11] (no caching)
.
4: Generating points for line [1,11]-[11,11] (no caching)
.
5: Generating points for line [3,3]-[9,3] (no caching)
.
6: Generating points for line [3,3]-[3,9] (no caching)
.
7: Generating points for line [9,3]-[9,9] (no caching)
.
8: Generating points for line [3,9]-[9,9] (no caching)
.
```

So we're generating points for a line going from coordinates (1,1) to (11,1), remember its size 10 rectangle is. So we are just printing the points to the terminal to sort of simulate the actual generation of the different points. We got 8 lines because we have two rectangles with 4 sides each.

Our adapter is working just fine. Or maybe is not? In fact our adapter is generating temporary objects and this is something that happens quite often when you build an adapter for some sort of API. 

Essentially what we're trying to do is to adapt lines to points, right? Unfortunately what happens is that we're recreating a lot of points on every different request. What this means? If you go back into the main method, and duplicate the call for `draw()` method, you'll notice that basically we're now doing double the work even though we don't really need to, because those temporaries could be reused, which just doesn't happen in this case.

```bash
1: Generating points for line [1,1]-[11,1] (no caching)
.
2: Generating points for line [1,1]-[1,11] (no caching)
.
3: Generating points for line [11,1]-[11,11] (no caching)
.
4: Generating points for line [1,11]-[11,11] (no caching)
.
5: Generating points for line [3,3]-[9,3] (no caching)
.
6: Generating points for line [3,3]-[3,9] (no caching)
.
7: Generating points for line [9,3]-[9,9] (no caching)
.
8: Generating points for line [3,9]-[9,9] (no caching)
.
9: Generating points for line [1,1]-[11,1] (no caching)
.
10: Generating points for line [1,1]-[1,11] (no caching)
.
11: Generating points for line [11,1]-[11,11] (no caching)
.
12: Generating points for line [1,11]-[11,11] (no caching)
.
13: Generating points for line [3,3]-[9,3] (no caching)
.
14: Generating points for line [3,3]-[3,9] (no caching)
.
15: Generating points for line [9,3]-[9,9] (no caching)
.
16: Generating points for line [3,9]-[9,9] (no caching)
.
```

As you can see we're generating points 16 times even tough we're working with the same rectangles, on same coordinates. Our algorithm should be generating the points just 8 times. What to do in this case? How do we avoid this kind of duplication?

Our goal is to make sure that the points for the particular lines we've generated are not being regenerated if they haven't changed. To do this, we need to implement `hashCode()` and `equals()` methods for both `Point` and `Line`.

```java
class Point
{
    //...

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode()
    {
        return 31 * x + y;
    }
}

class Line
{
    //...
 
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Line other = (Line) obj;
        return start.equals(other.start) && end.equals(other.end);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(start, end);
    }
}
```

The next step is to build some sort of caching mechanism inside the `LineToPointAdapter`. For that we're going to use a `Map` to store the points for each line we've generated, and also we need to keep track of the current line, that's where `line` variable comes in.

```java
class LineToPointAdapter extends ArrayList<Point>
{
    private static int count = 0;
    private static Map<Line, List<Point>> cache = new HashMap<Line, List<Point>>();
    private Line line;
}
```

The idea is when you come to adapt a line to a series of points the first thing to do is to check if our map `cache` already contains the line. If it does, then we're going to return *void*. If it doesn't, we're going to generate the points and store them in the map.

After adapting `LineToPointAdapter`, it should look like this:

```java
class LineToPointAdapter implements Iterable<Point>
{
    private static int count = 0;
    private static Map<Line, List<Point>> cache = new HashMap<>();
    private Line line;

    public LineToPointAdapter(Line line)
    {
        this.line = line;
        if (cache.get(line) != null) return;

        System.out.println(
        String.format("%d: Generating points for line [%d,%d]-[%d,%d] (with caching)",
            ++count, line.start.x, line.start.y, line.end.x, line.end.y));

        ArrayList<Point> points = new ArrayList<>();


        int left = Math.min(line.start.x, line.end.x);    // Minimum x-coordinate
        int right = Math.max(line.start.x, line.end.x);   // Maximum x-coordinate
        int top = Math.min(line.start.y, line.end.y);     // Minimum y-coordinate
        int bottom = Math.max(line.start.y, line.end.y);  // Maximum y-coordinate
        int dx = right - left;                            // Horizontal distance (delta x - measures the run or horizontal length of the line) 
        int dy = line.end.y - line.start.y;               // Vertical distance (delta y -  measures the rise or vertical length of the line)

        if (dx == 0)
        {
            for (int y = top; y <= bottom; ++y)
            {
                points.add(new Point(left, y));
            }
        }
        else if (dy == 0)
        {
            for (int x = left; x <= right; ++x)
            {
                points.add(new Point(x, top));
            }
        }

        cache.put(line, points);
    }

    @Override
    public Iterator<Point> iterator()
    {
        return cache.get(line).iterator();
    }

    @Override
    public void forEach(Consumer<? super Point> action)
    {
        cache.get(line).forEach(action);
    }

    @Override
    public Spliterator<Point> spliterator()
    {
        return cache.get(line).spliterator();
    }
}
```

Now if we rerun the program, our output should look a bit different:

```bash
1: Generating points for line [1,1]-[11,1] (no caching)
.
2: Generating points for line [1,1]-[1,11] (no caching)
.
3: Generating points for line [11,1]-[11,11] (no caching)
.
4: Generating points for line [1,11]-[11,11] (no caching)
.
5: Generating points for line [3,3]-[9,3] (no caching)
.
6: Generating points for line [3,3]-[3,9] (no caching)
.
7: Generating points for line [9,3]-[9,9] (no caching)
.
8: Generating points for line [3,9]-[9,9] (no caching)
.
.
.
.
.
.
.
.
.
```

We have the same amount of points without generating duplicates because we're using a cache, thus no unnecessary calculations. So the takeaway from this entire lesson is: if you implement the **Adapter** pattern, sometimes the adapter will generate temporary objects and if you want to avoid extra work then you build a cache. It's a performance optimization that you might have to do when implementing your own adapter's.
