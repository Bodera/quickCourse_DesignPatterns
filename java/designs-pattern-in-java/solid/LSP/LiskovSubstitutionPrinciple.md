# Liskov Substitution Principle
We're now going to take a look at the Liskov Substitution Principle which was invented by [Barbara Liskov](https://amturing.acm.org/award_winners/liskov_1108679.cfm) a brilliant woman.

The idea of the LSP is that you should be able to substitute a subclass for a base class, so if you have some API which takes a base class you should be able to stick a subclass in there without the things breaking in any way. And we're going to see how we can actually violate that principle.

Let's suppose that we decide to make a class called *Rectangle* thats is going to have protected members __width__ and __height__.

```java
class Rectangle
{
    protected int width, height;

    public Rectangle() {
    }

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArea() 
    { 
        return width * height;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
            "width=" + width +
            ", height=" + height +
            '}'; 
    }
}
```

So far so good, we implement a *Rectangle* class with constructors, a *toString()* method, some getters and setters and a function which calculates the area of rectangle. Now let's suppose that you decide to use inheritance in order to define a special type of a rectangle, let's say a square, remember a square is just a rectangle where the width and height are the same.

```java
class Square extends Rectangle
{
    public Square() {
    }

    public Square(int size)
    {
        width = height = size;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    }
}
```

We built the *Square* class in way to prevent that the value of width differs from the value of the height. And even though you might think that this is ok we've just violated the LSP. Let's see how exactly this principle has been violated.

Let's start by creating our *Demo* class so we can actually see everything fall apart.

```java
class Demo
{
    static void useIt(Rectangle r)
    {
        int width = r.getWidth();
        r.setHeight(10);
        //expected area for rectangle = width times 10.
        System.out.println(
            "Expected area of " + (width * 10) +
            ", got " + r.getArea()
        );
    }

    public static void main(String[] args) {
        Rectangle rc = new Rectangle(2, 3);
        useIt(rc);
    }
}
```

We get the following output:

```txt
Expected area of 20, got 20
```

LSP states that you should be able to substitute a derived class for a base class. The function *useIt()* takes a rectangle and the square certainly is a rectangle by virtue of inheritence, so it should be possible to use the *useIt()* with a square instead of a rectangle. Let's see what happens if we try to do it.

```java
class Demo
{
    static void useIt(Rectangle r)
    {
        int width = r.getWidth();
        r.setHeight(10);
        //expected area for rectangle = width times 10.
        System.out.println(
            "Expected area of " + (width * 10) +
            ", got " + r.getArea()
        );
    }

    public static void main(String[] args) {
        Rectangle rc = new Rectangle(2, 3);
        useIt(rc);

        Rectangle sq = new Square();
        sq.setWidth(5);
        useIt(sq);
    }
}
```

We get the following output:

```txt
Expected area of 50, got 100
```

Ups, things get out of hand here. Pretty sure you've already guessed the reason behind this behavior, our setter method inside *useIt()* is actually a very bad kind of setter method. It's very non-intuitive setter because as long as it does makes sense for a *Rectangle* in the *Square* implementation both setters for height and width are actually doing something particular annoying which is setting values for two attributes behind the scenes without really informing anyone it. So even though the *useIt()* works perfectly fine for a rectangle is violates the LSP when used with a square.

And this is what it's all about, making sure that if you have a method such as *useIt()* which takes a *Rectangle* it works correctly even if you give it some derived class of rectangles, in other words some inheritor of *Rectangle*.

In case if you're wondering about how we can actually improve the situation, how we can make sure that we have this concept of a square and still receiving the right output and so on.

First let me tell you that you don't need a square class necessarily, you could make some sort of detection of whether a rectangle is a square or not.

Inside of *Rectangle* class we add:

```java
public boolean isSquare()
{
    return width == height;
}
```

Now if you want explicit construction of rectangles and squares the you would have to use a design pattern called the __Factory Pattern__. We're going to take a look at it later on but I will show you how it looks.

We could create a new class *ShapeFactory* or just *RectangleFactory*.

```java
class RectangleFactory
{
    public static Rectangle newRectangle(int width, int height)
    {
        return new Rectangle(width, height);
    }

    public static Rectangle newSquare(int side)
    {
        return new Rectangle(side, side);
    }
}
```

This is a clue about how you would implement this using the factory design pattern, but is really up to you how to handle this. The point is to show to you that the Liskov Substitution Principle, if you violated it will result in incorrect code through inheritance. So you inherit the class and suddenly this class behaves in such a different way that you've broken the way in which the methods was working and this is something that you want to actually avoid.
