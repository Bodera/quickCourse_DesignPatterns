import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


class VectorTriangle extends MyVector
{
    /**
     * Constructs a new VectorTriangle object with the specified coordinates of the three points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @param x3 the x-coordinate of the third point
     * @param y3 the y-coordinate of the third point
     */
    public VectorTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        Point a = new Point(x1, y1);
        Point b = new Point(x2, y2);
        Point c = new Point(x3, y3);

        add(new Line(a, c));
        add(new Line(a, b));
        add(new Line(b, c));
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

class LineToPointAdapter extends ArrayList<Point>
{
    private static int count = 0;

    public LineToPointAdapter(Line line)
    {
        System.out.println(
        String.format("%d: Generating points for line [%d,%d]-[%d,%d] (no caching)",
            ++count, line.start.x, line.start.y, line.end.x, line.end.y));

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
        //missing logic to proper handle diagonal lines (triangle rasterization)
    }
}

class AdapterWithoutCacheV2 
{
    private static final int GRID_SIZE = 10;
    private static String[][] grid = new String[GRID_SIZE][GRID_SIZE];

    static {
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(grid[i], " ");
        }
    }

    private final static List<MyVector> myVectors = 
    new ArrayList<>(Arrays.asList(
        new VectorRectangle(1, 1, 8, 4),
        //new VectorTriangle(1, 2, 2, 4, 3, 2),
        new VectorSquare(1, 1, 3)
    ));
    //missing logic to proper handle negative coordinates

    public static void drawPoint(Point p)
    {
        grid[p.y][p.x] = "\033[35;49m*"; // fill grid with colored *
    }

    private static void draw()
    {
        
        for (MyVector vector : myVectors)
        {
            for (Line line : vector)
            {
                LineToPointAdapter adapter = new LineToPointAdapter(line);
                adapter.forEach(AdapterWithoutCacheV2::drawPoint);
            }
        }

        // Print the grid to the console, from top row to bottom row
        for (int i = GRID_SIZE - 1; i >= 0; i--) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        draw();
    }
}