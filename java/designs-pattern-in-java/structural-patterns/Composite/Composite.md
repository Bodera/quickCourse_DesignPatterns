# Composite

In some software like Adobe Illustrator or Microsoft PowerPoint in addition to having several shapes separate, you can also take several graphic shapes and group them together then drag them as a group. This is a classic implementation of the composite design pattern, let's try doing something similar.

Let's first define an abstract concept called a `GraphicObject`. So whether we're dealing with a `Square`, or a `Circle`, or a combination of shapes, is going to have a `name`. And also it will have something particular unusual, by default we're going to set it to `Group` which hints what its purpose, a `GraphicObject` is a collection of other objects unless somebody inherits from it and specify something different.

```java
class GraphicObject {

    protected String name = "Group";

    public GraphicObject() {
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
}
```

Let's further define another trait called `color`, and add the children objects, here is where things get interesting. You can consider a `GraphicObject` as a scalar object because there is the idea of somebody inheriting from it. A `Circle` or a `Square` are both a single object, but a `GraphicObject` can also represent a grouping in which case will have a bunch of children.

```java
class GraphicObject {
    //...

    public String color;
    public List<GraphicObject> children = new ArrayList<>();
}
```

So `GraphicObject` is either a group or a single object, it doesn't really matter. That's the core of the composite design pattern - *"Being able to treat individual objects and groups of objects in a uniform fashion"*. 

Now what we're going to do is create a method responsible for printing the `GraphicObject`. We're going to use our own internal implementation of printing because the problem with a `GraphicObject` until here is related to a __recursive object__ (a `GraphicObject` that contains other `GraphicObject` which can also contain other `GraphicObject` and so on). Our goal is to show the hierarchy of these objects in our print method.

```java
class GraphicObject {
    //...

    private void print(StringBuilder sb, int depth)
    {
        sb.append(String.join("", Collections.nCopies(depth, "*")))
            .append(depth > 0 ? " " : "")
            .append((Objects.isNull(color) || color.isEmpty()) ? "" : color + " ")
            .append(getName())
            .append(System.lineSeparator());

        for (GraphicObject child : children)
            child.print(sb, depth + 1);
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        print(sb, 0);
        return sb.toString();
    }
}
```

What we can do now is create individual objects like `Circles` and `Squares`.

```java
class Circle extends GraphicObject 
{
    public Circle(String color)
    {
        name = "Circle";
        this.color = color;
    }    
}

class Square extends GraphicObject 
{
    public Square(String color)
    {
        name = "Square";
        this.color = color;
    }    
}
```

The interest thing about this set up is that now we have the ability to instantiate not just `Squares` and `Circles`, we can also instantiate the root `GraphicObject`, notice that the class itself is not __abstract__, it's a concrete class, and also has a public constructor. The reason why you wouldn't instantiate it is that for a `GraphicObject` act as a group of different objects, and it can also contain other groups.

Let's check out the demo.

```java
class Demo
{
    public static void main(String[] args)
    {
        GraphicObject drawing = new GraphicObject();
        drawing.setName("My Drawing");
        drawing.children.add(new Circle("Red"));
        drawing.children.add(new Square("Yellow"));

        GraphicObject group = new GraphicObject();
        group.setName("Group 1");
        group.children.add(new Square("Black"));
        group.children.add(new Square("Gray"));
        drawing.children.add(group);

        System.out.println(drawing);
    }
}
```

The reason why we can write in line the entire drawing is because if you look at the implementation of any `GraphicObject` it always does the recursive procedure through its children on `print()` method. The `GraphicObject` is essentially either a singular object as you can see in `Circle` or `Square` which inherits from it, or it can be a container of objects optionally.

That's the key of the composite design pattern, it's an ability to have objects which can be treated in both singular manner as scalar objects, or as collections of objects. Time to check out our demo.

```bash
My Drawing
* Red Circle
* Yellow Square
* Group 1
** Black Square
** Gray Square
```

First we have the primarily added shapes and just after our group notice that another star was included indicating the layering of the elements. We can read that `My Drawing` contains a `Group 1` object which contains two other shapes.

That's a pretty simple variation of the composite design pattern, we basically made an object called `GraphicObject` which can either be a singular one or can contain a bunch of children, and regardless of whether it contains children or not the `print()` method behaves the same.

## Part 2

One of the hottest topics nowadays is machine learning and part of machine learning is the use of neural networks, so that's exactly what we're going to do in this section, learn how we can treat both individual neurons and collections of neurons in a uniform manner.

Suppose we have a class called `Neuron`. A neuron has connections to other neurons, so we can have a public list of neurons for both the incoming and outgoing connections.

```java
class Neuron
{
    private List<Neuron> in, out;
}
```

Nothing special so far. Now suppose that in addition to individual neurons what we want to be able to do is be able to make entire layers of neurons. For that we can define a class named `NeuronLayer` that may inherit a list of neurons.

```java
class NeuronLayer extends List<Neuron>
{
    
}
```

The problem with this approach will become more visible soon. Let's say you want to somehow perform connections between these neurons, so you can go ahead and implement something like this: we provide the neuron that we want to connect to, and include it in the list of outgoing connections. But also the incoming connections of that other neuron needs to be updated to include ourselves.

```java
class Neuron
{
    private List<Neuron> in, out;

    public void connectTo(Neuron other)
    {
        out.add(other);
        other.in.add(this);
    }
}
```

Now remember that `NeuronLayer` inherits from `List<Neuron>`? Let's check out the demo.

```java
class Demo
{
    public void main(String[] args)
    {
        Neuron n2 = new Neuron();
        Neuron n1 = new Neuron();

        NeuronLayer layer2 = new NeuronLayer();
        NeuronLayer layer1 = new NeuronLayer();
    }
}
```

We have established four different situations for interconnecting the neurons. It's very easy to go ahead and just declare `n1.connectTo(n2)`, but is not so simple if we want to connect the entire layer. Our goal is to perform the following kinds of connections in our neural network:

```java
n1.connectTo(n2);
n1.connectTo(layer1);
layer1.connectTo(n1);
layer1.connectTo(layer2);
```

Right now our code only supports the first operation. Keep in mind that there is no sense to implement four different `connectionTo()` methods, instead we can try to build an interface which is as general as possible for the purposes of connecting elements between each other.

First let's get rid of our first version of the `connectTo()` method, because it will require us to make have other overloaded methods, two in the `Neuron` class where the first one takes a `Neuron` and the second one takes a `NeuronLayer`, and other two in the `NeuronLayer` class where the first one takes a `Neuron` and the second one takes a `NeuronLayer`. In a scenario where our neural network could have other elements in the hierarchy the total number of overload methods would increase exponentially, the maintenance of the code becomes harder.

```java
/*public void connectTo(Neuron other)
{
    out.add(other);
    other.in.add(this);
}*/
```

So we're going to take a different approach, we're going to treat both, individual neurons and neuron layers, in a uniform fashion and that's the core of the composite design pattern. Think for a moment, a `NeuronLayer` is obviously just a collection of `Neuron` because we explicitly told it that when extended from `List<Neuron>`, now a `Neuron` can also be treated as a collection of neurons, but it's a singleton collection (a collection which has a single element, the neuron itself).

Knowing that we're not going to define `Neuron extends ArrayList<>`, instead we're going to define an interface which extends from `Iterable<Neuron>`.

```java
interface SomeNeurons extends Iterable<Neuron>
{

}
```

Now both `Neuron` and `NeuronLayer` are going to implement this interface, turns out that since we're implementing `Iterable<T>` we need to override `iterable()`, `spliterator()`, and `forEach()` methods for `Neuron` class only because `NeuronLayer` already extends from `ArrayList<>` which in hence implements `Iterable<T>`. The only remaining gap is the `Neuron` class, how can we have a single neuron mask itself as a collection of neurons? Actually that's pretty easy when using the `Collections.singleton()` method:

```java
class Neuron implements SomeNeurons
{
    public ArrayList<Neuron> in, out;

    @Override
    public Iterator<Neuron> iterator()
    {
        return Collections.singleton(this).iterator();
    }

    @Override
    public Spliterator<Neuron> spliterator()
    {
        return Collections.singleton(this).spliterator();
    }

    @Override
    public void forEach(Consumer<? super Neuron> action)
    {
        action.accept(this);
    }
}
```

Now that we've implemented everything that's required by the `Iterable` interface we haven't solved the key problem which is to connect `Neuron` to `NeuronLayer` using just a single implementation of `connectTo()` method. To do that, let's add a default implementation of to `SomeNeuron` interface, if you prefer you can substitute `SomeNeuron` with an abstract class if you wish.

```java
interface SomeNeurons extends Iterable<Neuron>
{
    default void connectTo(SomeNeurons other)
    {
        if (this == other) return;

        for (Neuron from : this)
        {
            for (Neuron to : other)
            {
                from.out.add(to);
                to.in.add(from);
            }
        }
    }
}
```

Okay, we make it general so that the `connectTo()` receives other kind of neuron. First we check if the neuron isn't attempting to connect to itself, and if that's not the case for each neuron that we're keeping we iterate the neurons from `other` to interconnect them together.

As a result the following code becomes valid because for all those objects, since they all implement `SomeNeurons` interface, they all have a unique `connectTo()` implementation.

```java
n1.connectTo(n2);
n1.connectTo(layer1);
layer1.connectTo(n1);
layer1.connectTo(layer2);
```

The takeaway from this lesson is that you can take a scalar/singular element like a `Neuron` and you can still implement `Interable<T>` even on a single element. And in order to have this particular element masquerade as a collection of other elements, you can use the `Collections.singleton()` method, and finally override the `iterator()`, `spliterator()`, and `forEach()` methods.

So this is how you can implement a composite design pattern in a very general fashion.

