# Factory Method

All right so we're going to begin the discussion of the different factory patterns by looking at the factory method which is probably the simplest kind of implementation of the factory that you can actually have.

Let me show you an example of where exactly the factory method would be useful. Let's suppose you have a class called `Points`, this class just stores a two dimensional point.

Now the thing is, internally we're going to store the point in Cartesian coordinates so I'm going to have private attributes `x` and `y`. And we as well make a constructor which actually initializes our attributes.

```java
class Point
{
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
```

Ok so far so good. Now imagine if you also want to initialize the point from polar coordinates. This is actually a bit of a problem because what you really want to do is you want to somehow communicate the fact that you have another constructor which initializes the point explicitly from polar coordinates.

Typically you would do something like the following:

```java
class Point
{
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // na-na-nah
    public Point(double rho, double theta)
    {
        x = rho * Math.cos(theta);
        y = rho * Math.sin(theta);
    }
}
```

Except for the reason that you simply can't. Unfortunately having such kind of constructors is illegal in Java because we already have a constructor which takes two double parameters. There are programming languages such as Objective-C and Swift which allow you to do this, to overload just on the names of the arguments. That's not the case here, in Java if you want to somehow make it explicit that you're initializing from different coordinates systems you end up having to make a complicated constructor, so you end up introducing some sort of **enum**, like this:

```java
enum CoordinateSystem
{
    CARTESIAN,
    POLAR
}

class Point
{
    private double x, y;

    public Point(double a, double b, CoordinateSystem cs) {
        switch (cs) {
            case CARTESIAN:
                this.x = a;
                this.y = b;
                break;
            case POLAR:
                x = a * Math.cos(b);
                y = a * Math.sin(b);
                break;
        }
    }
}
```

I'm sure you'll agree that the constructor we got so far is really ugly and I'm only showing you a simple example of why you would have to branch it might be a lot more complicated. The point is you're getting some logic in the constructor which is particularly unpleasant, and also the constructor is now less usable because people have to guess the meaning of `a` and `b`, until you make some documentation and tell them explicitly that `a` is X if `cs` is __Cartesian__ or a row if it is __Polar__. This is a really bad idea, really bad thing to do.

We want to somehow avoid all of this unfortunately we just can't, and the reason for that is because the constructor method, the name of the constructor, has always the name of the surrounding class. That's just simply how constructors are. So we cannot give a hint in the constructor what kind of *Point* we are constructing, we certainly cannot overload the constructor with identical type and quantity of arguments and so we are stuck. The factory pattern is precisely the kind of thing that solves this problem.

Now we are going to see how we can use the factory pattern and we're going to use the *factory method* then, as the name suggests it's just going to be a method which is a member of the *Point* class. The first thing to do is to hide the *Point* constructor that we have by adding it the private access modifier that way we have a constructor but nobody can actually use it from outside the class. What we want to do is to make a couple of static methods which are going to initialize different *Points* but they're going to have dedicated names.

We might end up with something similar to this:

```java

// Notice how the name of the factory method actually indicates what kind of Point we're constructing
public static Point newCartesianPoint(double x, double y) {
    return new Point(x, y);
}

public static Point newPolarPoint(double rho, double theta) {
    return new Point(rho * Math.cos(theta), rho * Math.sin(theta));
}

private Point(double a, double b) {
    this.x = a;
    this.y = b;
}
```

This is much nicer because essentially what you have now is a situation which is very explicit what kind of point is being constructed or at least what the coordinates of the point originally are. And the reason why we declared our constructor as private is to force the user to explicitly use the factory method at any point in time so is guaranteed that we can't instantiate a *Point* without using the factory method.
