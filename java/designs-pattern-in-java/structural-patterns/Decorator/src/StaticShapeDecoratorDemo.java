public class StaticShapeDecoratorDemo 
{
    public static void main(String[] args)
    {
        Shape circle = new ColoredShape(new Circle(2.0f), "red");
        System.out.println(circle.info());
        Shape square = new TransparentShape(new ColoredShape(new Square(2.0f), "blue"), 50);
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

class ColoredShape implements Shape
{
    private Shape shape;
    private String color;

    public ColoredShape(Shape shape, String color)
    {
        this.shape = shape;
        this.color = color;
    }

    @Override
    public String info()
    {
        return shape.info() + " has the color " + color;
    }
}

class TransparentShape implements Shape
{
    private Shape shape;
    private int transparency;

    public TransparentShape(Shape shape, int transparency)
    {
        this.shape = shape;
        this.transparency = transparency;
    }

    @Override
    public String info()
    {
        return shape.info() + " has " + transparency + "% transparency";
    }
}
