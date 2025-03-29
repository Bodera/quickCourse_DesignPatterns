# Observer

## Observer and Observable

We're going to begin our discussion of the observer design pattern by attempting to implement our own _observer_ and _observable_ types. For the observer entity we're going to define an interface, and for the observable entity we're going to define a concrete class.

Let's propose a scenario where you might want to observe, let's say you have a class `Person` which holds the person `age`, and we want to be able to get change notifications whenever somebody sets the `age` of a person. You might have some UI component that needs to be informed about the fact that the age has been set, and we're going to try building some infrastructure in order to support this idea of property change notification.

```java
class Person {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

First we've to define a class called `PropertyChangedEventArgs<T>`, it will hold some information about changes to a particular property of a given type `T`.

```java
class PropertyChangedEventArgs<T> {
    private T source;
    private String propertyName;
    private Object newValue;

    public PropertyChangedEventArgs(T source, 
                                    String propertyName, 
                                    Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.newValue = newValue;
    }                                        
}
```

We need the observer and the observable now. Let's start with the observer, it is the interface that you'd expect to be implemented by anyone interested in observing an object of type `T`.

```java
interface Observer<T> {

    void handle(PropertyChangedEventArgs<T> args);
}
```

Essentially the idea is that if you want to monitor an object of type `T` then you implement the `Observer<T>` interface. You define your own `handle()` method and then hope that the `handle()` method will be executed whenever somebody actually performs some changes.

Now we need the observable part. It is a concrete class of type `T`, and this class is going to have a list of all the subscribers that are watching a particular class, this is a list of every single `Observer<T>`, so then we have some sort of API for actually subscribing to this observable to monitor changes.

```java
class Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    protected void propertyChanged(T source, 
                                    String propertyName, 
                                    Object newValue) {
        for (Observer<T> observer : observers) {
            observer.handle(new PropertyChangedEventArgs<T>(
                source, propertyName, newValue));
        }
    }
}
```

Now we update the class `Person` to extends from `Observable<Person>`. The `getAge()` remains the same but the `setAge()` method needs modification to support the idea of notification on changes. 

```java
class Person extends Observable<Person> {

    // ...

    public void setAge(int age) {
        if (age == this.age) return;
        this.age = age;
        super.propertyChanged(this, "age", age);
    }
}
```

Now we're all setup to make a demo.

```java
class Demo implements Observer<Person> {

    public static void main(String[] args) {
        new Demo(); //for the sake of not have to do everything in a static context
    }

    public Demo() {
        Person person = new Person();
        person.subscribe(this); // Demo is the observer here
        
        for (int i = 0; i < 4; i++) {
            person.setAge(i);
        }
    }

    @Override
    public void handle(PropertyChangedEventArgs<Person> args) {
        System.out.println("Person's " + args.propertyName + " changed to " + args.newValue);
    }
}
```

Output:

```
Person's age changed to 1
Person's age changed to 2
Person's age changed to 3
```

This example illustrates how you can set up a simple observer pattern just by essentially keeping a list of subscribers inside the `Observable<T>` and then dispatching on them. However, there is issue in this scenario, as long as is generally ok with having an `Observable<Person>` as part of the `Person` type notice that we're extending `Observable<Person>` which means that person cannot have a proper base class. You can for sure take the `Observable<T>` and change it to an interface with default members and whatsoever, but I would rather not have this interface at all and the same goes for `Observer<T>`, seems really silly that if you want to monitor a type of `T` then an implementation of any kind of interface is going to be required.

In the next lesson we're going to do a much simpler approach, we're going to encapsulate the idea of an event, and we're going to build the entire infrastructure around that without requiring any implementation of _observables_ or _observers_.

## An event class

All right we're going to rewrite the entire example of the previous lesson, and even though you think that's a waste of time let me tell you that this is not. The objective on this lesson is to show you that we can get away from this idea of using _observables_ or _observers_ by dealing with **events**.

The class `Event` is going to encapsulate this idea of something happening, you're going to be able to subscribe to this event in order to get notifications, and you're also going to be able to unsubscribe, by the way the unsubscription process is going to be really neat. The class `Event<TArgs>` has a type argument where `TArgs` represents the actual data that's being fired as the event gets fired.

```java
class Event<TArgs> 
{
    private int count = 0;
    private Map<Integer, Consumer<TArgs>> 
        handlers = new HashMap<>();
}
```

The fact that we're using `Consumer` means that we want the users to be able to provide lambda functions in order to subscribe to this event.

Now we're going to have a method for adding a particular handler, this ain't going to be easy because when adding a handler I want to give some sort of _memento_ back to whoever is subscribing to this event in order to be able to disconnect from the event. Let's name our _memento_ as `Subscription` which is going to be an inner class of `Event<T>`.

```java
class Event<T> 
{
    // ...

    public Subscription addHandler(Consumer<TArgs> handler) { }

    public class Subscription implements AutoCloseable 
    {
        private Event<TArgs> event;
        private int id; //index key of the handler

        public Subscription(Event<TArgs> event, int id) 
        { 
            this.event = event;
            this.id = id;
        }

        @Override
        public void close() {
            event.handlers.remove(id);
         }
    }
}
```

By adopting the implementation of `AutoCloseable` fits the concept that we want the subscription to only last for a certain amount of time. 

Now let's figure out the implementation of the `addHandler()` method.

```java
class Event<T> 
{
    // ...

    public Subscription addHandler(Consumer<TArgs> handler) {
        int id = count++;
        handlers.put(id, handler);
        return new Subscription(this, id);
    }
}
```

You'll notice that the `event` and `id` of `Subscription` are private and immutable, and there is only one piece of API which is the `close()` method, now you can use `try-with-resources` or call the `close()` method to disconnect from the event.

Now we need a way of firing the events to notify all the consumers that something happened, and this is going to be done by the `fire()` method.

```java
class Event<T> 
{
    // ...

    public void fire(TArgs args) 
    {
        for (Consumer<TArgs> handler : handlers.values()) {
            handler.accept(args);
        }
    }
}
```

That's how we get each of the consumers that we have firing the events with the appropriate arguments.

Having set up this `Event` what we can now do is we can build property changes to the event args, like we did before.

```java
class PropertyChangedEventArgs {
    public Object source;
    public String propertyName;

    public PropertyChangedEventArgs(Object source, String propertyName) {
        this.source = source;
        this.propertyName = propertyName;
    }
}
```

This is the notification object that we're going to fire from the `Event`.

To set up our demo let's define a class called `Person` which isn't going to implement any interface, is going to be no mention of `Observable<T>` or `Observer<T>` in any way shape or form, we're going to go with classes as they are.

```java
class Person 
{
    public Event<PropertyChangedEventArgs>
        propertyChanged = new Event<>();

    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age == this.age) return;
        this.age = age;

        propertyChanged.fire(new PropertyChangedEventArgs(age, "age"));
    }
}
```

That's pretty much it, let's just verify that it works.

```java
class Demo 
{
    public static void main(String[] args) {
        Person person = new Person();
        person.propertyChanged.addHandler(tArgs -> {
            System.out.println("Person's " + tArgs.propertyName + " changed to " + tArgs.source);
        });

        for (int i = 0; i < 4; i++) {
            person.setAge(i);
        }
    }
}
```

Output:

```
Person's age changed to 1
Person's age changed to 2
Person's age changed to 3
```

And there is a demonstration of how we can unsubscribe from the event.

```java
class Demo
{
    public static void main(String[] args) {
        Person person = new Person();
        Event<PropertyChangedEventArgs>.Subscription 
        subscription = person.propertyChanged.addHandler(tArgs -> {
            System.out.println("Person's " + tArgs.propertyName + " changed to " + tArgs.source);
        });

        for (int i = 0; i < 2; i++) {
            person.setAge(i);
        }

        subscription.close();

        for (int i = 2; i < 4; i++) {
            person.setAge(i);
        }
    }
}
```

Output:

```
Person's age changed to 1
```

## The problem with dependent properties

Property change notification have lots of methodological issues and even though we've been working on a fundamentally correct example, let's see how it can go bad really quickly.

Let's take the `Person` class, we have the `getters` and `setters` for the `age`. Imagine that we have a read-only property of some kind, let's say a boolean indicating whether a person can actually vote.

```java
class Person 
{
    // ...

    public boolean getCanVote() {
        return age >= 18;
    }
}
```

The problem is that we also want change notifications on this new `getter` just like on everything else. The question is how we can get it because there is no `setter`? The obvious answer would be to stick it into the `getter` and `setter` methods of the `age` property because it depends on it. But once again the problem is that you only fire the notification if the property has in fact changed, so as consequence of this you end up cashing the old `canVote` value and then, after you notify on the `age`, you determine whether to notify on `canVote` depending on whether the value in fact change.

Something like this:

```java
class Person 
{
    // ...

    public boolean getCanVote() {
        boolean canVote = age >= 18;
    }

    public void setAge(int age) {
        if (age == this.age) return;

        boolean oldCanVote = age >= 18;

        this.age = age;
        propertyChanged.fire(new PropertyChangedEventArgs(age, "age"));

        if (oldCanVote != getCanVote()) {
            propertyChanged.fire(new PropertyChangedEventArgs(getCanVote(), "canVote"));
        }
    }
}
```

This is an approach that works if you have one property dependent on another property. Now imagine you have a massive tree of different properties dependent on one another, the approach we've explored here is not going to work, it simply does not scale, and you will end up making some sort of custom trees or custom dependency lists in order to actually navigate all the dependencies and check that all the dependencies are up-to-date.
