# Mediator

## Chat room

The classic illustration of a mediator is the idea of a chat room and the reason for that is that the chat room is precisely what the mediator is. It is a way of letting users interact with one another without necessarily having direct references or pointers or any kinds of direct connection to one another. Instead, every single message goes through the chat room and the chat room acts as a mediator, so that's precisely what we're going to build.

Let's begin my making a class called `Person` which is going to represent a participant in the chat room, so each person has to have a `name`. They will also have a private list of strings representing the chat logs of messages they've received. 

Now in addition you also need a reference to the mediator itself, so let's also define a class called `ChatRoom` because every single participant, every visitor, is going to have a reference to the chat room.

```java
class ChatRoom 
{
    
}

class Person 
{
    private String name;
    private ChatRoom room;
    private List<String> chatLog = new ArrayList<>();
}
```

First let's add a constructor for `Person` where we only set the `name`. And reference to the chat room will be set a bit later because note that a person can be booted out of a chat room, they can be kicked out, or they can leave the chat room but, they can still keep their name because of that the name is more important to initialize.

```java
class Person 
{
    // ...

    public Person(String name) {
        this.name = name;
    }
}
```

Now we're going to have API's on both, the side of the person as well the side of the chat room for actually saying things into the room, just globally broadcasting a message or saying a private message to another participant in the room. In addition to that, a person is also going to have a method of receiving a message from the chat room itself.

```java
class Person 
{
    // ...

    public void receive(String sender, String message) 
    {
        String s = sender + ": '" + message + "'";
        System.out.println("[" + name + "'s chat session] " + s);
        chatLog.add(s);
    }

    public void say(String message) 
    {
        room.broadcast(name, message);
    }

    public void privateMessage(String who, String message) 
    {
        room.message(name, who, message);
    }
}
```

Now we can go implement the necessary methods on the chat room class.

```java
class ChatRoom 
{
    private List<Person> participants = new ArrayList<>();

    public void join(Person p) 
    {
        String joinMsg = p.name + " has joined the room.";
        broadcast("room", joinMsg);

        p.room = this;
        participants.add(p);
    }

    public void broadcast(String source, String message)
    {
        participants.stream()
            .filter(p -> !p.name.equals(source)) // validating that the person is not broadcasting to themself
            .forEach(p -> p.receive(source, message));
    }

    public void message(String from, String to, String message) 
    {
        participants.stream()
            .filter(p -> p.name.equals(to))
            .findFirst()
            .ifPresent(p -> p.receive(from, message));
    }
}
```

So we now have all parts of the mediator figured out and every single participant in the chat room basically refers to that mediator we now have a complete demo.

```java
class Demo
{
    public static void main(String[] args) 
    {
        ChatRoom room = new ChatRoom();
        Person alice = new Person("Alice");
        Person bob = new Person("Bob");
        Person charlie = new Person("Charlie");

        room.join(alice);
        room.join(bob);

        alice.say("Hello");
        bob.say("Hi there!");

        room.join(charlie);
        charlie.say("Hi everyone!");

        alice.privateMessage("Charlie", "Hi Charlie!");
    }
}
```

Output:

```txt
[Alice's chat session] room: 'Bob has joined the room.'
[Bob's chat session] Alice: 'Hello'
[Alice's chat session] Bob: 'Hi there!'
[Alice's chat session] room: 'Charlie has joined the room.'
[Bob's chat session] room: 'Charlie has joined the room.'
[Alice's chat session] Charlie: 'Hi everyone!'
[Bob's chat session] Charlie: 'Hi everyone!'
[Charlie's chat session] Alice: 'Hi Charlie!'
```

First we have Bob joining the room, when he joins the room no messages are out because there's nobody to actually receive the message. Then we have Alice joining the room and Bob receives that message. So Alice says hello and Bob receives that message and responds. When Charlie joins the room both Bob and Alice receive the broadcast message from the room. Charlie than says hi everyone which both Bob and Alice receive. Then Alice sends a private message to Charlie and Charlie receives that message.

This is how you implement the mediator design pattern. The key idea here is that everyone has a reference to the mediator but notice that at no time does a `Person` in any way reference to another `Person`, they way they reference each other is by their names. The system is quite robust in the sense that if you send a message to a person who left the room no one gets notified.

## Rx event broker

Alright, now we're going to take a look at a more sophisticated kind of mediator which is an **event broker** except that we already looked at an event broker when we talked about the chain of responsibility. So this time around we're going to do an event broker which leverages [reactive extensions](https://github.com/ReactiveX/RxJava). We're not going to jump deep into reactive extensions but, we're going to implement a couple of things from it.

Let's imagine a situation where you have a football game, and you have a player running on the field scoring goals, and you have that football coach who won and wants to some way congratulates the players as they score additional goals. How can we implement this using reactive extensions of an event broker?

First we need to define the event broker, we can do that by creating a class like so:

```java
import io.reactivex.rxjava3.core.Observable;

class EventBroker extends Observable<Integer>
{
    
}
```

Here `Observable` is of course a reactive extension class that allows us to create an observable component that we can subsequently subscribe to, we do that by simply implementing the `subscribeActual()` method.

```java
import io.reactivex.rxjava3.core.Observable;

class EventBroker extends Observable<Integer>
{

    @Override
    protected void subscribeActual(Observer<? super Integer> observer)
    {
        // ...
    }
}
```

We take the observer that we receive as an argument and add it to a list of observers which keeps track of every single subscriber to the events generated by the event broker.

```java
import io.reactivex.rxjava3.core.Observable;

class EventBroker extends Observable<Integer>
{

    private List<Observer<? super Integer>> observers = new ArrayList<>();

    @Override
    protected void subscribeActual(Observer<? super Integer> observer)
    {
        observers.add(observer);
    }
}
```

So as somebody tries to subscribe what we're going to do is: `observers.add(observer);`.

Now that we have all of these observers who are watching the event broker the question is how can we actually supply the information? How can we publish an event happening inside the broker?

In our case the only information that we're going to propagate about the event is an integer, just a single integer indicating how many goals a player has scored.

Let's start creating a utility method called `publish(int n)` and `n` is actually what we're going to publish, we're going through every single observer and publish the event to them.

```java
class EventBroker extends Observable<Integer>
{
    // ...

    public void publish(int n) 
    {
        for (Observer<? super Integer> observer : observers) {
            observer.onNext(n);
        }
    }
}
```

You may like to read about the [Observable Contract](https://reactivex.io/documentation/contract.html).

Ok, now we can build both the football player and the football coach.

```java
class FootballPlayer 
{
    private int goalsScored = 0;
    private EventBroker broker;
    public String name;

    public FootballPlayer(EventBroker broker, String name) 
    {
        this.broker = broker;
        this.name = name;
    }

    public void score() 
    {
        broker.publish(++goalsScored);
    }
}
```

```java
class FootballCoach 
{
    public FootballCoach(EventBroker broker) 
    {
        broker.subscribe(goals -> {
            System.out.println("The coach congratulates " + name + " for scoring " + goals + " goals!");
        });
    }
}
```

This is how the coach can actually handle the whole thing. Let's run a demo.

```java
class Demo
{
    public static void main(String[] args) 
    {
        EventBroker broker = new EventBroker();
        FootballPlayer player = new FootballPlayer(broker, "Antony");
        FootballCoach coach = new FootballCoach(broker);

        player.score();
        player.score();
    }
}
```

We expect that any time that a player scores we actually put the incremented goal scored on the pipeline and get the broker to actually publish the whole thing, every single subscriber gets notified for every observable event. Even though you can see that we're not using the football coach directly, the constructor of `FootballCoach` does subscribe to the broker and gets notified whenever the player scores.

Output:

```txt
The coach congratulates Antony for scoring 1 goals!
The coach congratulates Antony for scoring 2 goals!
```

This is a small illustration on how you can use reactive extensions instead of rolling your own observer classes in order to create a mediator which allows you to supply values.

Reactive extensions is an extremely powerful API for actually filtering events and getting just the sequences that you're actually interested in. It also can combine sets of sequences and so on and so forth. Reactive extensions really deserve its own course to talk about all different features. The central premise is simple you have a central component and everyone communicates through that components.

### Some additional notes

Make things easier, it's 01:04am and to be frank I'm not interested in more headaches. So just:

- Create a `.vscode` folder for you project (use the Run and Debug panel available in Visual Studio Code available when install the Java extension)
- Download the jars necessary for reactx
- Refer the `libs` folder in the `settings.json` file
- Run the application and see the output

That's it! (Some modifications to original code were made to see the player name)