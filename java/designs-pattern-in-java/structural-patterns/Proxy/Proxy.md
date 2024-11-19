# Proxy

## Protection proxy

We'll start going to take a look at another type of proxy called _protection proxy_ which is typically used to control access to a particular resource while, once again, offering the same API.

For instance let's imagine that we have a situation where we model somebody driving a car, where we have an interface `Drivable` containing a single method `drive()`. Now let's try to design a car which implements drivable, plus we might want to also define the driver of the car. So the `Car` has a protected instance of `Driver` which is initialized in the constructor method call, because you cannot use a car without the driver.

```java
interface Drivable
{
    void drive();
}

class Car implements Drivable
{
    protected Driver driver;

    public Car(Driver driver)
    {
        this.driver = driver;
    }

    @Override
    public void drive()
    {
        System.out.println("Car is being driven");
    }
}

class Driver
{
    
}
```

Let's talk about the `Driver` for a second, what is it all about. Let's give the driver something to store, like age for example, the driver obviously cannot drive if their age is below 18.

```java
class Driver
{
    public int age;

    public Driver(int age)
    {
        this.age = age;
    }
}
```

Essentially what we want to have here is something which behaves every way as a `Car`, but it actually verifies that the driver is old enough to drive. For this purpose what we do is make a proxy for a `Car` which also verifies that the driver that we assign is old enough to drive and if the driver isn't old enough we can print the message. Let's go ahead and try that out.

```java
class CarProxy extends Car
{
    public CarProxy(Driver driver)
    {
        super(driver);
    }

    @Override
    public void drive()
    {
        if (driver.age >= 18)
            super.drive();
        else
            System.out.println("Driver is too young to drive");
    }
}
```

This is the setup essentially, so you have your ordinary car which doesn't perform any checks, then suddenly turns out that some verifications are necessary, so you make a proxy `CarProxy` which also extends `Car`. Now what happens is, if you have some sort of API which uses a `Car` you can replace a `Car` with a `CarProxy` and nothing bad will happen.

```java
class Demo
{
    public static void main(String[] args)
    {
        Car car = new Car(new Driver(17));
        car.drive();

        car = new CarProxy(new Driver(17));
        car.drive();
    }
}
```

Output:

```bash
Car is being driven
Driver is too young to drive
```

In this example we're initializing the `CarProxy` manually but what you could do with dependency injection is once again you could reconfigure the dependency injection container to make sure that whenever somebody wants a `Car` they are given a `CarProxy` instead and that way you could sort of replace one with the other throughout the entire application.

## Property proxy

This one is a bit unusual and doesn't fall exactly into the proxy categories. Let me set up the scenario for you, suppose I do something like:

```java
class Demo
{
    public static void main(String[] args)
    {
        int x = 0;
        x = 5;
    }
}
```

Now even though `x` is assigned the value of `5`, at no point we're actually recording the fact that we're assigning the value of `5` to `x`. So if you want to perform logging on this particular operation you will have to perform that logging in front of every single assignment.

```java
int x = 0;
//Log.logAssignment(x, 5);
x = 5;
```

The question is how do we typically deal with situations where we want to log assignments or log accesses to a particular variable or indeed a member variable, so a field. And the answer is that we use `getters` and `setters` methods, and we effectively turn our fields into properties. 

Typically, the way this goes is something like the following, suppose you have a creature in a computer game and the creature has an agility value. So you have a class called `Creature`, and then you have a private `agility`, and public `getAgility()` and `setAgility()` methods. Inside the `getter` and `setter` methods you can actually track the changes to this particular field.

```java
class Creature
{
    private int agility;

    public int getAgility()
    {
        return agility;
    }

    public void setAgility(int agility)
    {
        this.agility = agility;
    }
}
```

However, the field itself is still available to access through the _equals operator_ `=`. So in a situation where we initialize `agility` inside the constructor method the assignment will not be recorded anywhere.

```java
class Creature
{
    private int agility;

    public Creature(int agility)
    {
        this.agility = agility;
    }

    // ...
}
```

So one of the things that people build, and it doesn't just happen in Java it also happens in C# and C++, is something called a _property proxy_ an idea of replacing a field with something more interesting, something which forces you to perform the kind of checks that we want to do here like logging for example, suppose you want to log every assignment to that field, so you would replace that field with something else.

Unfortunately one of the things you cannot do in Java is you cannot overload the assignment operator `=`, and similarly you cannot do automatic conversion, so you don't have implicit operator conversion. But apart from that we can try to build what is effectively a property proxy even though it doesn't replicate the interface, because remember the interface of an ordinary field is to use the equals operator, and unfortunately we're not going to be able to do this, but we're going to be quite close.

First we make a class called `Property` which can be of any type `T` so `T` is the type of the internal storage. Inside we're going to have some private `value` that we're going to store.

```java
class Property<T>
{
    private T value;
}
```

Unfortunately we have to provide `value` from the outside the syntax below is not allowed in Java:

```java
class Property<T> 
{
    //Invalid syntax
    private T value = new T();
} 
```

That's the rules when using type erasure, what we do instead is we make a constructor which actually requires the consumer to provide the initial value and then of course what you do is implement the `getValue()` and `setValue()` methods for `T`.

```java
class Property<T>
{
    private T value;

    public Property(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }
}
```

Once you have the `Property` class you don't have any way of assigning it using the equals operator, you must use the constructor and then `getter` and `setter` methods.

```java
//Invalid syntax
Property<Integer> property = 5;
```

So before we finish the `Property` class let's actually add a few more things. A great feature is to override the `equals()` and `hashCode()` methods in it.

```java
class Property<T>
{
    // ...

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property<?> property = (Property<?>) o;
        return Objects.equals(value, property.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(value);
    }
}
```

Now what you can do, is start implementing the logs inside the methods. Let's start with the `setter` for example:

```java
class Property<T>
{
    private T value;
    private static final Logger LOGGER_VALUE = Logger.getLogger(Property.class.getName() + "Proxy");

    // ...

    public void setValue(T value)
    {
        this.value = value;
        LOGGER_VALUE.info("Value set to " + value);
    }
}
```

You can use the methods mentioned to turn on or off logging, you can make it configurable somewhere else, you can even make it globally configurable using in some sort of dependency injection scenario, this is interesting because using dependency injection you can get the property, you can assign some default values to be injected into the class. We're not going to explore this here, but you can see the idea. Doing dependency injection is still possible just a bit more tricky. 

Going back to our `Creature` class we would have a private `Property` of `Integer`, just for illustration purpose, and we assign an initial value to it, as a matter of fact you must provide the initial value since we won't initialize the field on constructor call, next we update the `setter` and `getter` methods.

```java
class Creature
{
    private Property<Integer> agility = new Property<>(5);

    public int getAgility()
    {
        return agility.getValue();
    }

    public void setAgility(int agility)
    {
        this.agility.setValue(agility);
    }
}
```

Time to test if it works.

```java
class Demo
{
    public static void main(String[] args)
    {
        Creature creature = new Creature();
        creature.setAgility(10);
        System.out.println(creature.getAgility());
    }
}
```

So the idea of a property proxy is to simply replace every single field that you store with a specialized kind of construct which enables you more control because a field with its equals operator for assignment and its direct access to the underlying memory doesn't allow you much control in terms of things like logging, or controlling access, or maybe even substituting different values, but property proxy does that's one reason to use it.

## Dynamic proxy for logging

Previously we looked at how to construct proxies by specifically making new classes, now we're going to explore a much more powerful but somewhat computationally costly approach and that is the approach of _dynamic proxies_. Dynamic proxies are used in many places, and many frameworks, and you should be aware of what dynamic proxies are and how to use them.

The simple answer to what is a dynamic proxy is

> It's a proxy which is constructed at runtime as opposed to compile time.

So at runtime you take an existing object, and you make a wrapper around it for example, in order to intercept every single call to every single one of its methods.

The best way to understand what a dynamic proxy is, is to actually build one and see how it works. So let's get our hands dirty.

First let's define an interface called `Human`, and we decided that a `Human` should be capable to do two things: `talk()` and `walk()`.

```java
interface Human
{
    void talk();
    void walk();
}

class Person implements Human
{
    @Override
    public void walk()
    {
        System.out.println("I am walking");
    }

    @Override
    public void talk()
    {
        System.out.println("I am talking");
    }
}
```

We now have a class called `Person` as well as an interface called `Human`, and we're going to try to build a dynamic proxy which takes an existing object of type `Person` and counts the number of methods inside that `Person` that have actually been called.

Luckily for us Java provides an interface for doing exactly that. Let's first start by defining some sort of `LoggingHandler` which going to implement the java native `InvocationHandler` interface. Because it uses reflection we're now able to intercept different methods, this is done by using the `invoke()` method, but before we dive deep in how this works let's initialize the `LoggingHandler` and define what kind of information we're going to track inside it.

We're going to track basically two things, the first one is a reference to the object which we're actually providing a proxy for, remember this is all happening at runtime, so at runtime we must pass to it an existing object and tell it basically to take over its functionality while providing additional things. We're gonna refer to that object as `target`.

And the second thing that you want is a `Map` which is going to record the number of method calls to the various methods that are called on the underlying object.

```java
class LoggingHandler implements InvocationHandler
{

    private final Object target;
    private Map<String, Integer> methodCalls = new HashMap<>();

    public LoggingHandler(Object target)
    {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        return null;
    }
}
```

Now we're finally ready to implement the `invoke()` method. The premise of it is that you get to invoke a particular method for a particular set of arguments. Nothing so scary at first sight, but before we actually perform the invocation we want to do some additional work, for example noting down the number of times each method has been called.

So let's start by retrieving the method name using reflection, so then we can increment the number of calls that have been made to that particular method inside our map. Towards the end we actually do use the reflection API to perform the invocation of the method.

```java
//...
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
{
    String methodName = method.getName();

    methodCalls.merge(methodName, 1, Integer::sum);

    return method.invoke(target, args);
}
```

There is still room for another trick here, suppose that somebody call `toString()` on the decorated object, in a scenario which that's exactly the point where you want to output the number of calls that have been made to each particular method.

```java
//...
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
{
    String methodName = method.getName();

    if (methodName.equals("toString"))
    {
        return methodCalls.toString();
    }

    methodCalls.merge(methodName, 1, Integer::sum);
    return method.invoke(target, args);
}
```

Okay, soon we're going to take a look at how it works, first let's run our `Demo` class where we can actually start using all of this to build a dynamic proxy for a `Person`. We're going need to define a utility method for constructing a dynamic proxy with logging enabled to any kind of object, it doesn't have to be exclusively a `Person` it can be virtually anything.

It's going to be a static method, parameterized on type `T`, and it's going to return that type `T` by the way, and let's name it `withLogging()`. It takes two things, the `target` which is the object for which the logging is required, and we also have to specify a `Class<T>` regarding the interface that we want to receive on the output, remember that our goal is to get a particular interface and a dynamic proxy for that interface, and so we cannot simply just take the underlying class and get that as the end result, it will just don't work. But in fact we can get an interface, that should hopefully explain why in this example we have a class which implements an interface because that interface is precisely what we're going to give out at the end of the `withLogging()` invocation.

```java
public class Demo
{

    public static <T> T withLogging(T target, Class<T> interfaceType)
    {
        return (T) Proxy.newProxyInstance(
            interfaceType.getClassLoader(), 
            new Class<?>[] {interfaceType},
            new LoggingHandler(target)
        );
    }

    public static void main(String[] args)
    {
        Person person = new Person();
        Human logged = withLogging(person, Human.class);
        logged.walk();
        logged.walk();
        logged.talk();

        System.out.println(logged);
    }
}
```

And as output we get:

```bash
I am walking
I am walking
I am talking
{talk=1, walk=2}
```

We're getting the correct output, at the end it prints the summary of count of times each method has been called. This is a very small illustration of the kind of power that is afforded to us by dynamic proxies. Here all that's happened is essentially just by implementing a single interface for an `InvocationHandler` you have a point where you intercept the invocation of every single method and before that method gets actually invoked you can perform some additional processing, which is pretty much what proxies are actually all about.

## Proxy vs Decorator

So one question that you might have is considering that the **proxy pattern** and the **decorator pattern** are so similar, then what exactly is the difference between the two?

The key difference is that the proxy tries to provide an identical interface whereas the decorator tries to enhance that original interface.

Let's take a communication proxy for example, it would try to stick to the exact interface that you already have so that you wouldn't have to actually rewrite any code. Whereas the decorator also tries to provide part or the whole of the interface that you are decorating, it typically gives you additional features as well, it could be additional fields, additional member functions, what have you.

Now in terms of implementation what happens is that the decorator typically aggregates or has a reference to what exactly it's decorating, and the proxy doesn't have to do that. The proxy can be a brand-new object not related to the underlying object at all, all it has to do is just masquerade as that object that you've been working with.

One interesting thing about the proxy unlike a decorated object is it might not even be materialized, it might not even exist, so you might have a proxy over something which hasn't been constructed. We've certainly looked at how that can be made possible if you have a virtual proxy for example.