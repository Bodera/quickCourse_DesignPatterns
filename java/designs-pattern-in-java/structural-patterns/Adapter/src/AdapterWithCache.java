import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

class AdapterWithCache 
{
    private final static List<MyVector> myVectors = 
    new ArrayList<>(Arrays.asList(
        new VectorRectangle(1, 1, 10, 10), 
        new VectorRectangle(3, 3, 6, 6)
    ));

    public static void drawPoint(Point p)
    {
        System.out.println(".");
    }

    private static void draw()
    {
        
        for (MyVector vector : myVectors)
        {
            for (Line line : vector)
            {
                LineToPointAdapter adapter = new LineToPointAdapter(line);
                adapter.forEach(AdapterWithCache::drawPoint);
            }
        }
    }

    public static void main(String[] args)
    {
        draw();
        draw();
    }
}

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

class MyVector extends ArrayList<Line> {}

class VectorRectangle extends MyVector
{
    /**
     * Constructs a VectorRectangle with the given coordinates and dimensions.
     *
     * @param x the x-coordinate of the bottom-left corner of the rectangle
     * @param y the y-coordinate of the bottom-left corner of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
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

class VectorSquare extends MyVector
{
    /**
     * Constructs a new VectorSquare object with the specified coordinates and size.
     *
     * @param x The x-coordinate of the bottom-left corner of the square.
     * @param y The y-coordinate of the bottom-left corner of the square.
     * @param size The length of each side of the square.
     */
    public VectorSquare(int x, int y, int size)
    {
        Point bottomLeft = new Point(x, y);
        Point topRight = new Point(x + size, y + size);
        Point topLeft = new Point(x, y + size);
        Point bottomRight = new Point(x + size, y);

        add(new Line(bottomLeft, bottomRight)); // border-bottom
        add(new Line(bottomLeft, topLeft)); // border-left
        add(new Line(bottomRight, topRight)); // border-right
        add(new Line(topLeft, topRight)); // border-top
    }
}

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

