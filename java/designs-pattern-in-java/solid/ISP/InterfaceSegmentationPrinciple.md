# Interface Segmentation Principle
The ISP is basically a recommendation on how to split interfaces into smaller interfaces.

Let's suppose that we decide to make an interface which would allow clients to perform some operations on the document or at least implement a common interface for operating on document.

```java
class Document
{

}

interface Machine
{
    void print(Document d);
    void fax(Document d);
    void scan(Document d);
}
```

With this interface defined your clients can actually take it and start implementing it. Ok, so one of your clients tries to build a multifunction printer so they make a class called *MultiFunctionPrinter*.

```java
class MultiFunctionPrinter implements Machine
{

    @Override
    public void print(Document d) {
        //
    }

    @Override
    public void fax(Document d) {
        //
    }

    @Override
    public void scan(Document d) {
        //
    }
}
```

Perfectly, the client is able to implement all relevant functionality by applying meaningful code which actually performs the relevant operation. Now a different client comes up wanting a simply old-fashioned printer.

```java
interface Machine
{
    void print(Document d);
    void fax(Document d);
    void scan(Document d);
}

class OldFashionedPrinter implements Machine
{

    @Override
    public void print(Document d) {
        //
    }

    @Override
    public void fax(Document d) {
        //
    }

    @Override
    public void scan(Document d) {
        //
    }
}
```
 
For sure some old-fashioned will be able to print documents, but unfortunately we encounter a bit of an issue when implementing the fax and scan methods because it is entirely uncertain what you should be doing here. But there are some options.

You can leave these methods empty because an old-fashioned printer does not know how to fax or scan but by doing that you're still giving to the user of the old-fashioned printer an indication that because you implemented this interface you must support faxing and scanning as well and they're going to be trying to call these methods and maybe they will be really puzzled about why nothing is happening when these methods are called.

Another alternative is to throw some sort of exception. So you throw a new Exception that can be some specialized exception like _NotImplementedException_ or something to that effect. But as soon as you start doing this you end up with the problem of exception specifications because all of sudden you have to add the specification to the method signature and also in addition it would have to propagate up into the interface itself. So coming back to **Machine** interface you might want to add it here as well.

```java
interface Machine
{
    void print(Document d);
    void fax(Document d) throws Exception ;
    void scan(Document d);
}

class OldFashionedPrinter implements Machine
{

    @Override
    public void print(Document d) {
        //
    }

    @Override
    public void fax(Document d) throws Exception {
        throw new Exception();
    }

    @Override
    public void scan(Document d) {
        //
    }
}
```

Now this can be an additional issue because you might not actually control the **Machine** interface to begin with, a machine interface could be something that you're given and so you cannot propagate the expecification quite simply because you don't own the source code to the machine interface. So this where we come to the interface segregation principle basically a very simple idea that you shouldn't put into your interface more than what the client is expected to implement. And at the moment we are breaking the interface segregation principle because we're saying that even though somebody is interested in just making an ordinary printer they are still forced to implement the _fax()_ and _scan()_ methods even though it's entirely unclear what those methods should do.

So the meaning of segregation is basically putting apart or putting into separate categories or separate locations. And that's exactly what we're going to do, we're going to split the **Machine** interface into separate interfaces for printing, scanning and faxing.

```java
interface Printer
{
    void print(Document d);
}

interface Scanner
{
    void scan(Document d);
}

class JustAPrinter implements Printer{
    
    @Override
    public void print(Document d) {

    }
}
```

There is another term that you should be aware of, and that acronym is __YAGNI__ which stands for *You Ain't Going to Need It*. So to accomplish a task you just need to solve the problem which was asked for, in our case this mean to implement a machine that prints documents. Adding too much resources is meaningless and make manutenability harder, some thoughs regarding [an article of Martin Fowler](https://martinfowler.com/bliki/Yagni.html):

> While we may now think we need this supposed feature, there are some costs to be aware of before decide to spend resources on it.

- The cost of build: all effort spent on analyzing, programming, testing, documenting and implementing for a useless feature.

- The cost of delay: when building this feature, we haven't implemented something that is sure to bring clear business value. Does this really outweigh the savings of implementing now rather than later?

> Often people don't think through the comparative cost of building now to building later. One approach I use when mentoring developers in this situation is to ask them to imagine the refactoring they would have to do later to introduce the capability when it's needed. Often that thought experiment is enough to convince them that it won't be significantly more expensive to add it later. Another result from such an imagining is to add something that's easy to do now, adds minimal complexity, yet significantly reduces the later cost. Using lookup tables for error messages rather than inline literals are an example that are simple yet make later translations easier to support.

So let's reforce it one more time.

__Non-Complient__:
```java
class Printer implements Printer, Scanner {
    
    @Override
    public void print(Document d) {

    }
}
```

__Complient__:
```java
class Photocopier implements Printer, Scanner {
    
    @Override
    public void print(Document d) {

    }

    @Override
    public void print(Document d) {

    }
}
```

A taste of one of the patterns that we will see later on the course is the decorator pattern. Given a `MultiFunctionDevice`, we can implement it like this:

```java
interface MultiFunctionDevice extendes Printer, Scanner {}

class MultiFunctionMachine implements MultiFunctionDevice {
    
    @Override
    public void print(Document d) {

    }

    @Override
    public void print(Document d) {

    }
}
```

Now let's see how it looks like when using the decorator pattern which will enable us to reuse functionality of a `Printer` and a `Scanner` if you already have those things in order to build up your `MultiFunctionMachine`:

```java
class MultiFunctionMachine {
    private Printer printer;
    private Scanner scanner;

    MultiFunctionMachine(Printer printer, Scanner scanner) {
        this.printer = printer;
        this.scanner = scanner;
    }

    @Override
    public void print(Document d) {
        printer.print(d);
    }

    @Override
    public void print(Document d) {
        scanner.scan(d);
    }
}
```

The take away of Interface Segregation Principle it that instead of sticking everything into a single interface, what we should do is to put the absolute minimum amount of code into an interface so that at no point does a client, and here the client is the consumer a.k.a the developer that has to actually implement the interface, have to implement certain methods which they don't need at all.
