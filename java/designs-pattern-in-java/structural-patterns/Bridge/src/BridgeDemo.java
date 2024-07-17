import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class BridgeDemo
{
    public static void main(String[] args)
    {

        Injector injector = Guice.createInjector(new ShapeModule());
        Circle circle = injector.getInstance(Circle.class);
        circle.radius = 5.0f;
        circle.draw();
        circle.resize(2);
        circle.draw();

    }
}

interface Renderer {

    void renderCircle(float radius);

    //void square();

    //void triangle();
}

class VectorRenderer implements Renderer {
    @Override
    public void renderCircle(float radius) {
        System.out.println("Vector rendering circle of radius " + radius);
    }
}

class RasterRenderer implements Renderer {
    @Override
    public void renderCircle(float radius) {
        System.out.println("Raster rendering circle of radius " + radius);
    }
}

abstract class Shape
{
    protected Renderer renderer;

    public Shape(Renderer renderer)
    {
        this.renderer = renderer;
    }

    public abstract void draw();

    public abstract void resize(float factor);
}

class Circle extends Shape
{

    public float radius;

    @Inject
    public Circle(Renderer renderer)
    {
        super(renderer);
    }

    public Circle(Renderer renderer, float radius)
    {
        super(renderer);
        this.radius = radius;
    }

    @Override
    public void draw()
    {
        renderer.renderCircle(radius);
    }

    @Override
    public void resize(float factor)
    {
        radius *= factor;
    }
}

class ShapeModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(Renderer.class).to(RasterRenderer.class);
    }
}

