import java.util.ArrayList;
import java.util.List;

public class FacadeDemo
{
    public static void main(String[] args)
    {
        Console console = Console.createConsole(6, 25);
        console.render();
    }
}

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
        characters[y * lineWidth + x] = c;
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

    public char[] getCharacters() {
        return characters;
    }
}

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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}

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
            for (int y = 0; y < viewport.getHeight(); y++)
            {
                for (int x = 0; x < viewport.getWidth(); x++)
                {
                    System.out.print(viewport.charAt(x, y));
                }
                System.out.println();
            }
        }
    }

    public static Console createConsole(int width, int height)
    {
        Buffer buffer = new Buffer(width, height);
        populateBuffer(buffer);
        Viewport viewport = new Viewport(buffer, width, height, 0, 0);
        Console console = new Console(width, height);
        console.addViewport(viewport);
        return console;
    }

    /**
     * Populate the buffer with sample stock market data.
     * 
     * This function is used by the createConsole() function to populate the
     * buffer with some sample data. The data is a list of strings each
     * representing a stock and its price. The data is then iterated over and
     * each character of the string is set in the buffer at the appropriate
     * position.
     * 
     * @param buffer The buffer to populate.
     */
    private static void populateBuffer(Buffer buffer) {
        // Sample stock market data
        String[] stocks = {
            "AAPL: 175.25",
            "GOOGL: 2800.30",
            "MSFT: 300.45",
            "AMZN: 3400.20",
            "TSLA: 700.60"
        };
        
        // Populate the buffer with stock market data
        for (int y = 0; y < stocks.length; y++) {
            String stock = stocks[y];
            for (int x = 0; x < stock.length(); x++) {
                buffer.setChar(x, y, stock.charAt(x));
            }
        }
    }
}