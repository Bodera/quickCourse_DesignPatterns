# Bridge

The bridge design pattern doesn't have anything particularly complicated at all. But before we dive deep into it, let's recap why this pattern exists. Take the following example: you have a base class called `Shape`, and different types of it.

```java
class Shape {

}

class Circle extends Shape {
    
}

class Square extends Shape {
    
}

class Triangle extends Shape {
    
}
```

Now suppose that you have different ways of rendering these shapes kind of like we had in the adapter design pattern. So when it comes to rendering you can have vector rendering, raster rendering, etc. 

```java
interface Renderer {
    
}

class VectorRenderer implements Renderer {
    
}

class RasterRenderer implements Renderer {
    
}
```

The problem is that if you were to go ahead and do a kind of direct solution to this problem you will end up with lots of renderers.

```java
class CircleVectorRenderer extends VectorRenderer {
    
}

class SquareVectorRenderer extends VectorRenderer {
    
}

class TriangleVectorRenderer extends VectorRenderer {
    
}

class CircleRasterRenderer extends RasterRenderer {
    
}

class SquareRasterRenderer extends RasterRenderer {
    
}

class TriangleRasterRenderer extends RasterRenderer {
    
}
```

Well, as demonstrated above, when working with a 3 by 2 scenario we end up with 6 classes. And if we have 5 different shapes and 3 different renderers we end up with 15 classes. That is just not viable, and this is why the bridge pattern exists.

What you do instead is, for our example, you stick the render right inside the shape. Our interface `Renderer` will have a couple of methods that are common to render different shapes.

```java
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
```

Now we can define our shape hierarchy, that's where the bridge pattern comes in. So that the base class `Shape` as well as any inheritor of it is going to require us to explicitly specify the kind of renderer that we're going to be using when it comes to the `draw` method of the shape.

```java
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
```

Now that we have better defined our `Shape` class, we can try to make a `Circle`. The `Circle` class will inherit from the `Shape` class, it will also have to implement a constructor which takes a `Renderer` to propagate it up the hierarchy.

```java
class Circle extends Shape
{

    public float radius;

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
```

So far so good, we've got a circle which is actually dependent upon a render. What we can do now is we can start using it by providing a renderer as an argument to the constructor.

```java
class Demo
{
    public static void main(String[] args)
    {

        RasterRenderer raster = new RasterRenderer();
        VectorRenderer vector = new VectorRenderer();

        Circle circle = new Circle(vector, 5);
        circle.draw();
        circle.resize(2);
        circle.draw();

    }
}
```

The code above produces the following output:

```bash
Vector rendering circle of radius 5.0
Vector rendering circle of radius 10.0
```

Okay, that's what we expected. This approach might seem a bit clunky, after all you have to keep adding the renderer in all sort of locations. But, imagine if you have a very complicated application, and you actually want to supply a single `Renderer` to every single object that's being constructed, how we can make this palatable?

One of the way to make this palatable is to use a proper dependency injection framework, and that's what we're going to do here. We're going to use the [Google Guice Framework](https://github.com/google/guice) as our dependency injection framework.

First you have to pick which constructor you want for the purpose of injection and annotate it with the `@Inject` annotation.

```java
import com.google.inject.Inject;

class Circle extends Shape
{

    public float radius;

    @Inject
    public Circle(Renderer renderer)
    {
        super(renderer);
    }

    //...
}
```

Next procedure is to make a module which configures what actually happens when somebody has a dependency on a renderer.

```java
import com.google.inject.AbstractModule;

class ShapeModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(Renderer.class).to(RasterRenderer.class);
    }
}
```

We're basically saying that whenever somebody actually requests a `Renderer` to be injected, we're going to make an instance of the `RasterRenderer` class. It's time to test it out.

```java
import com.google.inject.Guice;
import com.google.inject.Injector;

class Demo
{
    public static void main(String[] args)
    {

        //Guice.createInjector(new ShapeModule()).getInstance(Circle.class).draw();
        Injector injector = Guice.createInjector(new ShapeModule());
        Circle circle = injector.getInstance(Circle.class);
        circle.radius = 5.0f;
        circle.draw();
        circle.resize(2);
        circle.draw();
    }
}
```

To run the program use the following commands:

```bash
javac -cp .\libs\*  .\BridgeDemo.java
java -cp .\libs\* .\BridgeDemo.java  
```

As we can see dependency injection here complements the bridge design pattern.
