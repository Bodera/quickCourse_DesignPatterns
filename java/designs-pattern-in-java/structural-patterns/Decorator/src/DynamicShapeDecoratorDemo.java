import java.util.function.Supplier;

public class DynamicShapeDecoratorDemo 
{
    public static void main(String[] args)
    {
        ColoredShape<Circle> circle = new ColoredShape<>(
            () -> new Circle(2.0f),
            "red");
        System.out.println(circle.info());

        TransparentShape<ColoredShape<Square>> square = new TransparentShape<>(
            () -> new ColoredShape<>(
                () -> new Square(2.0f), 
                "blue"),
            50);
        System.out.println(square.info());
    }   
}

interface Shape
{
    String info();
}

class Circle implements Shape
{
    private float radius;

    public Circle(){}

    public Circle(float radius)
    {
        this.radius = radius;
    }

    void resize(float factor)
    {
        radius *= factor;
    }

    @Override
    public String info()
    {
        return "A circle of radius " + radius;
    }
}

class Square implements Shape
{
    private float side;

    public Square(){}

    public Square(float side)
    {
        this.side = side;
    }

    @Override
    public String info()
    {
        return "A square of side " + side;
    }
}

class ColoredShape<T extends Shape> implements Shape
{
    private Shape shape;
    private String color;

    public ColoredShape(Supplier<? extends T> supplier, String color)
    {
        this.shape = supplier.get();
        this.color = color;
    }

    @Override
    public String info()
    {
        return shape.info() + " has the color " + color;
    }
}

class TransparentShape<T extends Shape> implements Shape
{
    private Shape shape;
    private int transparency;

    public TransparentShape(Supplier<? extends T> supplier, int transparency)
    {
        this.shape = supplier.get();
        this.transparency = transparency;
    }

    @Override
    public String info()
    {
        return shape.info() + " has " + transparency + "% transparency";
    }
}
