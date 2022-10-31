# Factory

Alright in the [previous lesson](../FactoryMethod/FactoryMethod.md) we saw how to create factory methods, now let's assume you have a lot of these factory methods, you might want to group them together somehow. Well how can you really do it? How can you put them into a separate build?

The simplest way might be to just put them into a separate class, and that's exactly what creates a factory. You should know that under the Gang of Four specification there is no `Factory ` definition, there is only __factory method__ and __abstract factory__, but a factory is the core pattern that underlies this whole idea.

Let's start by creating the class *PointFactory*, which is dedicated to create *Point* instances.

```java
class Point
{
    private double x, y;

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

class Factory
{
    public static Point newCartesianPoint(double x, double y) {
        return new Point(x, y);
    }

    public static Point newPolarPoint(double rho, double theta) {
        return new Point(rho * Math.cos(theta), rho * Math.sin(theta));
    }
}
```

Oops, I'm afraid we can't go on with this as we call the private *Point* constructor from outside the scope of the *Point* class. In this situation we can choose between the following: either we change the access modifier of the constructor to `public` or we can simply move the *Factory* class so that it becomes an inner class of *Point*.

By adding the public access modifier to the *Point* constructor, we're now opening floodgates for the client to have two completely different ways to construct a *Point* (default or factory constructor). That's not bad, it's up to you whether you're ok with this or not, you can have both a factory and the object itself. If however you want to hide the constructor by making it private, the only way to do that is to move the *Factory* class where it can still access that particular constructor, doing that we need to make some other changes because an inner class cannot have static statements if it is not static itself.

So this is one approach to making a factory, there is only one choice here: the choice between whether you want the factory to be independent as a separate top-level class, in which case you must make sure that the constructors that the factory uses to create the objects are available __or__ if you want to keep the constructor private, make a factory nested as an inner class inside the object you are trying to create. Of course this only works if you actually have access to the source code of the original object because sometimes you will build a factory long after the original object is built, maybe you are building a factory as a kind of utility to make the build process easier or more readable, in which case you don't have the option to have a nested factory, but in our example we do and this is another viable alternative.
