# Iterator

## In-Order Traversal

So now we're going to take a look at the implementation of the iterator, and just as a reminder, the iterator is nothing more than a helper object which tells you how to traverse a particular data structure. The data structure that we're going to use for this example is a binary tree, a very popular data structure because, among other things, a binary tree can be traversed in all sorts of different ways. There is pre-order, in-order, and post-order traversal, and we're going to use the in-order traversal here.

We first start by defining a binary tree in terms of its nodes, for that we have to define a class named `Node<T>` which is going to represent the node which stores a particular value of type `T`, and we're also going to have a couple of references from this node to other nodes around it, left, right and parent.

```java
class Node<T>
{
    T value;
    Node<T> left, right, parent;

    public Node(T value)
    {
        this.value = value;
    }

    public Node(T value, Node<T> left, Node<T> right)
    {
        this.value = value;
        this.left = left;
        this.right = right;

        left.parent = right.parent = this;
    }
}
```

Now what we're going to do is try to build an iterator which allows us for in-order traversal of this binary tree. For that we can start by defining a class called `InOrderIterator<T>` which is going to implement `Iterator<T>`. By signing the contract with `Iterator` a few methods need to be overridden in order to define the iterator API, and that is exactly what we're going to do here.

```java
class InOrderIterator<T> implements Iterator<T>
{
    @Override
    public boolean hasNext()
    {
        return true;
    }

    @Override
    public T next()
    {
        return null;
    }
}
```

For this example we're just overriding `hasNext()` and `next()` methods, there are additional methods that you can implement which are optional in case you want to have additional behaviors in your iterator. We're just going to have an iterator which just goes from the first elements and traverses what we want to do till the last element. So, in order to traverse a binary tree we need a couple of references here, like the current element we're currently on in the iterator, and also a reference to the root of the tree, and we will have a boolean flag `yieldedStart` which is going to tell us whether we've yielded the starting element. The reason why we're defining this flag is due the iteration of the tree if we're doing in-order iteration doesn't start from the root, it starts from the leftmost element, so in the constructor we need to find that leftmost element and set the current reference.

```java
class InOrderIterator<T> implements Iterator<T>
{
    private Node<T> current, root;
    private boolean yieldedStart;

    public InOrderIterator(Node<T> root)
    {
        this.root = current = root;

        while (current.left != null)
            current = current.left;
    }

    private boolean hasRightmostParent(Node<T> node)
    {
        if (node.parent == null) return false;
        else {
            return (node.parent.left == node)
                    || hasRightmostParent(node.parent);
        }
    }
    // ...
}
```

Our utility function `hasRightmostParent()` is important because essentially our navigation around the tree, our in-order traversal, isn't going to be recursive. So it checks if the parent of the current node is null because in that case it would mean that it doesn't have a rightmost parent, another verification is to check if current is the leftmost node or the rightmost node. 

Now the complicated stuff is going to happen in the `hasNext()` and `next()` methods. How do we determine whether we have the next element to move to? Well there are three possibilities here, either we have a left child, or a right child, or a rightmost parent.

```java
class InOrderIterator<T> implements Iterator<T>
{
    // ...

    @Override
    public boolean hasNext()
    {
        return current.left != null
                || current.right != null
                || hasRightmostParent(current);
    }
}
```

That's it for the `hasNext()` method. Now don't be scared of the following code chunk for the `next()` method, it has it complicated fare that's not commonly spread in textbooks or tutorials regarding in-order traversal, the reason is once again, we don't have recursive capabilities here, unfortunately `next()` has no way of suspending execution and then continuing from the same context, but is worth to mention that in languages like C# you can do this, unfortunately in Java there is no idea of suspending the context and asynchronously yielding values and, as result, we have this rather complicated mess that you can see here.

```java
class InOrderIterator<T> implements Iterator<T>
{
    // ...

    @Override
    public T next()
    {
        if (!yieldedStart)
        {
            yieldedStart = true;
            return current.value;
        }

        if (current.right != null)
        {
            current = current.right;

            while (current.left != null)
                current = current.left;

            return current.value;
        }
        else {
            Node<T> parent = current.parent;

            while (parent != null && parent.right == current)
            {
                current = parent;
                parent = parent.parent;
            }

            current = parent;
            return current.value;
        }
    }
}
```

Having made all this we can start using the iterator directly. Some of you are used to using the `for` loop and expecting that you have some collection which implements an `Iterable<T>` as opposed of `Iterator<T>` but don't worry, we're going to implement `Iterable<T>` in just a moment, but first, this demo purpose is to show you how to use the `Iterator` directly, this is the C++ style of doing things, taking the iterator and working directly with it.

We're going to have a simple tree which is going to have the element 1 on the top level, and the elements 2 and 3 at the bottom level, like so:

```text
  1
 / \
2   3
```

So if you want to do in-order traversal the expected result would be: `213`. So you go from the leftmost element, then the root, then the rightmost element. That's our intent to achieve here.

```java
class Demo
{
    public static void main(String[] args)
    {
        // tree declaration
        Node<Integer> root = new Node<>(1,
                new Node<>(2),
                new Node<>(3));
        
        InOrderIterator iterator = new InOrderIterator<>(root);

        while (iterator.hasNext())
            System.out.print("" + iterator.next() + ", ");

        System.out.println();
    }
}
```

That's the output we get:

```txt
2, 1, 3, 
```

One question we haven't addressed is what about sticking this constructor in some sort of `for` loop? How would we actually iterate all these nodes inside a `for` loop? Let's start by saying that it's a bit unfair to request that you have a single `for` loop considering there are at least three different ways of traversing a tree, but you can do it.

Supposing you only want in-order to traverse, let's make a class `BinaryTree<T>` that's going to implement `Iterable<T>`. The `Iterable<T>` is precisely the interface which is going to allow us to effectively support the idea of sticking the high constructor in a `for` loop, and we can do it in the following way:

```java
class BinaryTree<T> implements Iterable<T>
{
    @Override
    public Iterator<T> iterator()
    {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action)
    {
        
    }

    @Override
    public Spliterator<T> spliterator()
    {
        return null;
    }
}
```

As we know, when dealing with a binary tree the first thing to do is get the reference to the roots, they are our starting point.

```java
class BinaryTree<T> implements Iterable<T>
{
    private Node<T> root;

    public BinaryTree(Node<T> root)
    {
        this.root = root;
    }
    // ...
}
```

Then we need to implement `iterator()` and `forEach()`, we don't need to worry about `spliterator()` for this demo.

Remember that we've already made an iterator called `InOrderIterator`, so we can just return that.

```java
class BinaryTree<T> implements Iterable<T>
{
    // ...
    @Override
    public Iterator<T> iterator()
    {
        return new InOrderIterator<>(root);
    }
}
```

Regarding the `forEach()` method, we can reuse the `iterator()` method, like so:

```java
class BinaryTree<T> implements Iterable<T>
{
    // ...
    @Override
    public void forEach(Consumer<? super T> action)
    {
        for (T t : this)
            action.accept(t);
    }
}
```

What's really happening here is, the `for (T t : this)` is using the `iterator()` method to get a reference to the iterator then we simply have the consumer accept each of the items in turn.

Having made all this we can now do is create a binary tree, which in this case is a kind of wrapper which provides additional functionality, from our already defined `root`, and then we can use a `for` loop because we've implemented `Iterable<T>`.

```java
class Demo
{
    public static void main(String[] args)
    {
        // tree declaration
        Node<Integer> root = new Node<>(1,
                new Node<>(2),
                new Node<>(3));
        
        // binary tree
        BinaryTree<Integer> tree = new BinaryTree<>(root);
        
        // traversal
        for (Integer i : tree)
            System.out.print("" + i + ", ");
        
        System.out.println();
    }
}
```

As an output we get:

```txt
2, 1, 3, 
```

As you can see we're getting exactly the same output as before, so everything is working correctly.

This demo shows you how to implement your own iterator and how you can expose your own iterator if you're making a particular collection.

## Array-Backed Properties

We are now going to take a look at an approach called array-backed properties. Just a reminder, a property is nothing more than a field plus a `getter` and the `setter`.

Imagine we have some sort of computer game where you have different creatures who are kind of roaming the grounds, and each creature has certain stats. Let's define a class called `SimpleCreature`.

```java
class SimpleCreature
{
    private String name;
    private int health, level, xp;
    // ...

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    // ...
}
```

Sometimes we need to know some sort of general statistics about the creature, for example the _sum_ of its stats, or the _max_ value of its stats, and this is not particularly easy if you have this kind of setup. Let's now explore how you would implement a __maximum__ function.

```java
class SimpleCreature
{
    // ...
    public int maxStats()
    {
        
    }
}
```

Here we need to take the maximum of all the fields that we have, like so:

```java
class SimpleCreature
{
    // ...
    public int maxStats()
    {
        return Math.max(health, Math.max(level, xp));
    }
}
```

Now, if we were to have a _sum_ function we would have to take the sum of all the fields, like so:

```java
class SimpleCreature
{
    // ...
    public int sumStats()
    {
        return health + level + xp;
    }
}
```

And if some day you need to calculate the _average_ of the stats, then you would have to calculate the average of all the fields, like so:

```java
class SimpleCreature
{
    // ...
    public double avgStats()
    {
        return sumStats() / 3.0;
    }
}
```

This how you would implement things if you were just doing it manually. Now the problem with this approach is that we're not really communicating that the stats are all part of a single bundle, as a result if you were to include more stats, you would have to basically rewrite the `maxStats()`, `sumStats()`, and `avgStats()` methods, with little extra code. And change the `avgStats()` which currently has a hard-coded `3.0` value representing the number of stats.

So that's not a particularly robust approach, a different approach consists on instead of storing the fields individually we store them in an array, that's the idea of array-backed properties.

Let's define another class `Creature` which is going to implement the `Iterable<Integer>` interface. By doing so we're saying that this constructor is also able to provide an iterator which can iterate on the different statistics of the creature. Before we customize the overridden methods, we're going to define an array of the different statistics.

```java
class Creature implements Iterable<Integer>
{
    private int[] stats = new int[3];

    @Override
    public Iterator<T> iterator()
    {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action)
    {
        
    }

    @Override
    public Spliterator<T> spliterator()
    {
        return null;
    }
}
```

What happens now is that we can actually define the `getters` and `setters` for the different properties. Check out how this can be done.

```java
class Creature implements Iterable<Integer>
{
    private int[] stats = new int[3];
    
    // getter
    public int getHealth() { return stats[0]; }
    public int getLevel() { return stats[1]; }
    public int getXp() { return stats[2]; }
    
    // setter
    public void setHealth(int health) { stats[0] = health; }
    public void setLevel(int level) { stats[1] = level; }
    public void setXp(int xp) { stats[2] = xp; }
    
    // ...
}
```

The statistics generalization functions like _sum_, _max_, _avg_, become a lot easier and robust.

```java
class Creature implements Iterable<Integer>
{
    // ...

    public int maxStats()
    {
        return Arrays.stream(stats).max().getAsInt();
    }

    public int sumStats()
    {
        return Arrays.stream(stats).sum();
    }

    public double avgStats()
    {
        return Arrays.stream(stats).average().getAsDouble();
    }
}
```

See how all the aggregate operations on the array can be defined in terms of streams for example, saving us a lot of effort. One improvement we can add is by replacing those "magic numbers" with constants, like so:

```java
enum CreatureStats
{
    HEALTH,
    LEVEL,
    XP;
}

class Creature implements Iterable<Integer>
{
    private int[] stats = new int[3];
    
    // getter
    public int getHealth() { return stats[CreatureStats.HEALTH.ordinal()]; }
    public int getLevel() { return stats[CreatureStats.LEVEL.ordinal()]; }
    public int getXp() { return stats[CreatureStats.XP.ordinal()]; }
    
    // setter
    public void setHealth(int health) { stats[CreatureStats.HEALTH.ordinal()] = health; }
    public void setLevel(int level) { stats[CreatureStats.LEVEL.ordinal()] = level; }
    public void setXp(int xp) { stats[CreatureStats.XP.ordinal()] = xp; }
    
    // ...
}
```

The main benefit here is that by changing the number of stats, we don't have to worry about changing the statistical functions.

Let's see now how we can implement `Iterable<T>` to take the creature and go through every single one of its abilities with a `for` loop.

We start by updating our overridden methods from `Iterable<T>`:

```java
class Creature implements Iterable<Integer>
{
    // ...

    @Override
    public Iterator<Integer> iterator()
    {
        return Arrays.stream(stats).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action)
    {
        for (Integer stat : stats)
            action.accept(stat);
    }

    @Override
    public Spliterator<Integer> spliterator()
    {
        return Arrays.stream(stats).spliterator();
    }
}
```

Let's create a demo class for testing our creature:

```java
class CreatureDemo
{
    public static void main(String[] args)
    {
        Creature creature = new Creature();
        creature.setHealth(100);
        creature.setLevel(10);
        creature.setXp(100);
        
        System.out.println("Max stats: " + creature.maxStats());
        System.out.println("Total stats: " + creature.sumStats());
        System.out.println("Avg stat: " + creature.avgStats());
    }
}
```

Output:

```txt
Max stats: 100
Total stats: 210
Avg stat: 70.0
```

The takeaway from this example is that ultimately what you can do is store all the fields inside a single array, if they're not addressable directly, if they're being kept private, then why not put every single field inside an array or indeed every single related field, because you can imagine a situation where a creature also has a number of names - and you can also put that inside an array that allows you to perform some aggregations like for example concatenating all the names together before returning them, in that case you would obviously have to implement the `Iterable<String>` interface.

By doing so you become able to treat all fields or a group of the fields as a single element that you can take a **stream** of, and then perform **stream** operations which is very convenient.
