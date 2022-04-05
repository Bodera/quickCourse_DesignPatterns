# Fluent Interface

Let's move a step back and recap the use of the `StringBuilder`.

```java
StringBuilder sb = new StringBuilder();
```

As you call the `append()` method on the StringBuilder, the return type of that append operation is itself a StringBuilder.

```java
sb.append("foo").append("bar");
```

What do we get as return is the same StringBuilder that we're working with, that enables us to perform chained calls to `append()`.

This is what we call a fluent interface, and the whole point of a fluent interface is to allow you to write very long chains which are particulary useful for building things up. Let's remember how our first builder turned out on the previous lesson.

```java
builder.addChild("li", "hello");
builder.addChild("li", "world");
```

Anytime that we want to add a new child node we have to refer to our `builder` object. Why can't we just say refer to `builder` once and then `addChild()` and then `addChild()` again? Maybe allow some other operations, that wouldn't hurt either.

To enabled that behaviour, we have to change the signature of our `addChild()` method in our API.

```java
// So that
public void addChild(String childName, String childText){...}

// Turns into
public HtmlElement addChild(String childName, String childText){... return this;}
```

By applying these changes every single invocation of `addChild()` returns a reference to the original builder so that we can continue using that reference to call more and more of the different parts of the builder, and suddenly if you had more than a single method inside a builder you could keep calling any of the methods in any order that you wish, this will become more clear on this lesson.

## Person Builder

Let's suppose we've got a class `Person`, this class holds the following: a __name__ and might also have some employment information so it will also have their __position__  at the company.

First we need to create one builder which builds up the kind of personal information, so in this example it will only saves the name of the person, and we're going to have another builder which inherits from that personal info builder so that it will also be capable to fills the employment information. Let's go to the action.

```java
class PersonBuilder
{
    protected Person person = new Person();

    public PersonBuilder withName(String name) 
    {
        person.name = name;
        return this;
    }

    public Person build()
    {
        return person;
    }
}
```

The `PersonBuilder` class needs to have a reference to the object that we're actually building, that's our `protected Person` object. We created a method to set the name of the person and another method that actually returns the `Person` called `build()`. 

Let's see what we've got so far.

```java
class FluentBuilder {
    public static void main(String[] args) {
        PersonBuilder pb = new PersonBuilder();
        Person grigori = pb.withName("Grigori").build();
        System.out.println(grigori);
    }
}
```

As output we get: `Person{name = 'Grigori', position = 'null'}`. So far, so great.

Now imagine that you want to have a builder which also builds up the employment information like position, annual income, stuff like that. But you also want that this new builder preserves the functionality of the original builder. How you do it?

Well obviously you do it with inheritance.

```java
class EmployeeBuilder 
    extends PersonBuilder
{
    public EmployeeBuilder worksAt(String position)
    {
        person.position = position;
        return this;
    }
}
```

Remember that we already have a `protected Person` reference in the `PersonBuilder` and that's why we can access it.

If you thought that was all I'm sorry to inform you that this is not enough. But before talking about why let's check the outout that we get if run the following:

```java
class Main {
    public static void main(String[] args) {
        EmployeeBuilder eb = new EmployeeBuilder();
        Person grigori = eb
            .withName("Grigori")
            .worksAt("ML Engineet at Yandex")
            .build();
        System.out.println(grigori);
    }
}
```

As output we get:

```text
error: cannot find symbol
        Person grigori = eb.withName("Grigori").worksAt("ML Engineet at Yandex").build();
                                               ^
  symbol:   method worksAt(String)
  location: class PersonBuilder
1 error
```

What makes our builder behave like this? Well just check the implementation of our `withName()` method, it returns a `PersonBuilder` not a `EmployeeBuilder`. So how can you call `worksAt()` if all you have is a reference to a `PersonBuilder` and not to an `EmployeeBuilder`?

This is precisely the location where Java's recursive generics come into play because of what you can essentially do is design the `PersonBuilder` in such a way that it's "friendly" to providing fluent APIs using requests of generics.

Let's modify `PersonBuilder` a bit.

```java
class PersonBuilder<SELF extends PersonBuilder<SELF>>
```

So now it takes a generic argument called `SELF` which extends a `PersonBuilder` of `SELF`. Now this might seem really trick and I recommend that if you are not familiar with this kind of approach go ahead and read up on recursive generics in Java because what we're essentially doing is saying that `PersonBuilder` is going to take a __type__ argument but that type argument __has to be__ some inheritor of `PersonBuilder`. And who inherits from `PersonBuilder`? Yep. The `EmployeeBuilder`, so let's just adapt it like this:

```java
class EmployeeBuilder 
    extends PersonBuilder<EmployeeBuilder>
```

So what we're saying now is instead of just inheriting a `PersonBuilder` we also provide `EmployeeBuilder` as a type argument.

The reason of doing all that, well we did this because we wanted to preserve the fluent builder interface, and to accomplish that we have to modify the return type of `PersonBuilder` in our `withName()` method.

```java
public SELF withName(String name) 
{
    ...
    return this;
}
```

Of course this causes another cascade of problems because essentially what we want to do is to return this `SELF` argument from whatever other locations in code that we actually want to kind of implement. You can't return `this` anymore in the `withName()` method, you must cast it to `SELF`.

```java
public SELF withName(String name) 
{
    ...
    return (SELF) this;
}
```

Opinions on this form of type casting are controversial because that is an uncheked cast. So maybe yout want to sort of verify that this is in fact the case. However that's simply a deficiency of IDE's because you can see there is only one way this can go. It can only execute in the correct fashion. There is no way to provide a `SELF` which does not extend from `PersonBuilder` and we're also already inside `PersonBuilder` so casting this to `SELF` is absolutely valid and you shouldn't worry about this kind of thing. If you want to see proof that this is always correct then simply try sticking something else here and then you're going to see the IDE complain and your code will not compile.

Now what we need to do is: we need to be able to fundamentally return whatever type of reference we're actually working with and that reference is `SELF`. But be aware that `SELF` isn't just relevant to `PersonBuilder` but is also relevant to `EmployeeBuilder`. So here is what we gonna do.

```java
class PersonBuilder<SELF extends PersonBuilder<SELF>>
{
    protected Person person = new Person();

    public SELF withName(String name) 
    {
        person.name = name;
        return self();
    }

    public Person build()
    {
        return person;
    }

    protected SELF self()
    {
        return (SELF) this;
    }
}
```

We make a new method called `self()` which simply returns ourselves cast to `SELF` and our methods will pass to return this new method. The reason for doing this is because we want to be able to override the behavior of `self()` in derived classes. So now we can adapt `EmployeeBuilder` this way:

```java
class EmployeeBuilder 
    extends PersonBuilder<EmployeeBuilder>
{
    public EmployeeBuilder worksAt(String position)
    {
        person.position = position;
        return self();
    }

    @Override
    protected EmployeeBuilder self() 
    {
        return this;
    }
}
```

Like this we're simply specifying that whenever somebody wants a `self()` outside of this particular constructor they can simply use a pointer tor `this`. So we're kind of propagating this idea that the fluent interface actually affectes the entire system.

Lets check the output we get if run this now: `Person{name = 'Grigori', position = 'ML Engineet at Yandex'}`.

Now we're getting the correct output. So the take away from this example is that if you want a fluent interface to propagate across inheritance hierarchies then what you need to do is, you need to have recursive generic definition so instead of just having a `PersonBuilder` you have a `PersonBuilder` which takes a type argument `SELF` which extends `PersonBuilder` and for every kind of inheritor what you do is, you stick that inheritor as a type argument of `PersonBuilder` thereby propagating this idea of always returning a type reference to the most derived type that you're working with.

```bash
$ cd src/
$ javac FluentBuilder.java
$ java FluentBuilder
```
