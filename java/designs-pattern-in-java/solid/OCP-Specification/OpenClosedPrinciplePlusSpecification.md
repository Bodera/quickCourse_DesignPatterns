# Open-Closed Principle with Specification Pattern
In this lesson we're going to look at the open-closed principle through the prism of implementing the __Specification__ design pattern. Be aware that this is not a original Gang of Four Design Pattern, instead it's an enterprise engineering pattern.

Okays, suppose that you decide to make a website where you're selling different products, something like Amazon for example, and you want to allow your users to filter products by a specific criteria.

First of all let's implement the criteria, suppose the products have different colors and sizes.

After that we're able to create the class *Product*, and a product is simply a combination of the products name, it's color and it's size. That's pretty much it. That's all we really need to do.

Code time:

```java
enum Color
{
    RED, GREEN, BLUE
}

enum Size
{
    SMALL, MEDIUM, LARGE, HUGE
}

class Product
{
    public String name;
    public Color color;
    public Size size;

    public Product(String name, Color color, Size size){
        this.name = name;
        this.color = color;
        this.size = size;
    }
}
```

The next feature we gonna implement will be a filter, whick will be a mechanism to allow us to take a __list of products__ and filter then by some __criteria__.

Let's say we first want to filter the products by color, so we have:

```java
class ProductFilter
{
    public Stream<Product> filterByColor(List<Product> products,
                                         Color color)
    {
        return products.stream().filter(p -> p.color == color);
    }
}
```

This seems to satisfy our needs, but then all of a sudden we need to filter products by size as well. So the first thing you do is copy-and-paste code, you've had to jump into a chunk of code that you've already written and tested and you have to modify it. In situations like that you must remember the whole point of the Open-Closed Principle:

> Be open for extension but closed for modification.

And as you can see we're perfoming modifications here. Suddenly comes up the necessity of filter the products by both color and size at the same time, and patterns shows up.

```java
class ProductFilter
{
    public Stream<Product> filterByColor(List<Product> products,
                                         Color color)
    {
        return products.stream().filter(p -> p.color == color);
    }

    public Stream<Product> filterBySize(List<Product> products,
                                         Size size)
    {
        return products.stream().filter(p -> p.size == size);
    }

    public Stream<Product> filterBySizeAndColor(List<Product> products,
                                         Size size,
                                         Color color)
    {
        return products.stream().filter(
            p -> p.size == size
            && p.color == color
        );
    }
}
```

Can you guess the smell of this code by far? As long as the *Product* abstraction keep few attributes and we have few criterias to filtes it's ok. And if you add more attributes like price and supplier, you'll end up with 25 *different methods* that basically do the same thing, that's it the factorial of total of attributes plus one.

Another bigger problem which the OPC tries to solve is to say that you shouldn't be jumping into code that's already written. That is, you want code which is open for extension but closed for modification. In our example we want some sort of filtering mechanism whic is closed for modification after is being tested and after it's been shipped to clients, maybe beacause your clients might already be using a binary snapshot of this code, so you don't want to keep modifying you want to have something on the side maybe an extra library or something.

There is actually an way to solve this problem, but before let's observe how all we actually use all of this theory.

Let's say we have a class like this:

```java
class Demo
{
    public static void main(String[] args) {
        Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
        Product house = new Product("Blue", Color.BLUE, Size.LARGE);

        List<Product> products = List.of(apple, tree, house);

        ProductFilter pf = new ProductFilter();
        System.out.println("Green products (old):");
        pf.filterByColor(products, Color.GREEN)
                    .forEach(p -> System.out.println(
                            " - " + p.name + " is green"));
    }
}
```

Compile it and run with:

```bash
$ javac Product.java
$ java Demo
```

The output should looks like:
```txt
Green products (old):
 - Apple is green
 - Tree is green
```

The result is correct but we're violating the OCP rule by requiring that whenever we make new filters inside *ProductFilter* class we have to go and jump into a class that we already made. So how can we mitigate this problem? How can we build a better architecture? We can built it using the specification pattern, let's see.

Now we introduce two new interfaces, the first one called *Specification* is a generic interface that takes __type parameter__ T and it has a single method which returns a boolean which determines if a particular product satisfies a criteria, but in actual fact we're not constrained just by products we can filter just about anything that we want and we can satisfy a particular criteria on any object that we want and because of that the object type taken on method *isSatisfied* are going to be *T*. The second one called *Filter* also a generic which filters for *T* where *T* can be virtually anything not necessarily a product, the *Demo* will obviously work with products but the *Filter* interface can work with anything, the functionality of this interface will stick to the way we implement in *ProductFilter* class, the function *filter()* returns a Stream of *T* from a List of *T* that represent the list of items we want to filter as weel a Specification of *T* which we want to satisfy. So instead of specifying some attibute like color or size we're making everything very generic and very flexible to work with.

Code time:

```java
interface Specification<T>
{
    boolean isSatisfied(T item);
}

interface Filter<T>
{
    Stream<T> filter(List<T> items, Specification<T> spec);
}
```

So now let's suppose we want to build a color specification which matches a particular color. Here's how you would do this:

```java
class ColorSpecification implements Specification<Product>
{
    private Color color;

    public ColorSpecification(Color color) {
        this.color = color;
    }

    @Override
    public boolean isSatisfied(Product item) {
        return item.color == color;
    }
}

class SizeSpecification implements Specification<Product>
{
    private Size size;

    public SizeSpecification(Size size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfied(Product p) {
        return p.size == size;
    }
}
```

Having made both of these specifications we can now build an example which does pretty much the same thing of filter the green products and print the green products by using this new filter. We haven't made the filter itself yet, so we have our old *ProductFilter* class that's not good enough. We now need a new filter which takes care of using the filters as well as the specifications, looking like this:

```java
class BetterProductFilter implements Filter<Product>
{
    @Override
    public Stream<Product> filter(List<Product> items, Specification<Product> spec) {
        return items.stream().filter(p -> spec.isSatisfied(p));
    }
}
```

If you compare the outputs you will notice that they are exactly the same.

```java
class Demo
{
    public static void main(String[] args) {
        Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
        Product house = new Product("Blue", Color.BLUE, Size.LARGE);

        List<Product> products = List.of(apple, tree, house);

        ProductFilter pf = new ProductFilter();
        System.out.println("Green products (old):");
        pf.filterByColor(products, Color.GREEN)
                    .forEach(p -> System.out.println(
                            " - " + p.name + " is green"));

        BetterProductFilter bpf = new BetterProductFilter();
        System.out.println("Green products (new):");
        bpf.filter(products, new ColorSpecification(Color.GREEN))
                .forEach(p -> System.out.println(
                        " - " + p.name + " is green"));
    }
}
```

The upside of this implementation is that if you want additional filters taht means additional *Specifications* you don't have to jump into existing classes and modify them, you really don't have to change their binary interface you just use inheritance and implementation of interfaces. That's all that you do and it becomes a little more flexible.

You may be wondering how do we check that we satisfy both color and size? It's fairly easy to do because what that means is just a combination of specifications. You just have to make a *Combinator* that combines specifications together.

```java
class AddSpecification<T> implements Specification<T>
{
    private Specification<T> first, second;

    public AddSpecification(Specification<T> first, Specification<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isSatisfied(T item) {
        return first.isSatisfied(item) && second.isSatisfied(item);
    }
}
```

By this example we learned about the Open-Closed Principle that states open for extension, which means it's ok for you to go ahead and inherit things or implement interfaces, but it's closed for modification, that is at no point in time would you want to actually jump back into *BetterProductFilter* class for example and actually modify it somehow because it's finished and correctly working and if you want additional functionality you have to interface it. So you have Specification of *T* for checking whether a particular item satisfy some criteria and you have a filter of *T* for basically taking a list of items and returning only those items which satisfy the appropriate specification.