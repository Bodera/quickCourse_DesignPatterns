# State

## Classic Implementation

The classic Gang of Four implementation of the state design pattern is particularly weird, and before we begin you should be aware that this is not something we really do in the industry as such. It's not the kind of code that we write but is code that does work.

We're going to simulate this simple act of pressing a light switch and turning the light on or off. Now obviously we need to handle a few particular cases like for example, if the light is already on and you press the ON button then we should get some sort of message.

Let's begin by defining a class called `State`, the purpose of this class is to give us some sort of API for turning on and off a light given a particular light switch.

```java
class State 
{
    void on(LightSwitch ls)
    {
        
    }

    void off(LightSwitch ls)
    {
        
    }
}

class LightSwitch 
{
    
}
```

Here is the idea, in the classic Gang of Four approach to the state design pattern every single state is a class. So when you have the light on you have an `OnState` class, and similarly for the `OffState`. Let's actually build those.

```java
class OnState extends State
{
    public OnState()
    {
        System.out.println("Light turned on");
    }
}

class OffState extends State
{
    public OffState()
    {
        System.out.println("Light turned off");
    }
}
```

Now you're probably wondering - "Hold on, we can never be in `State`, why isn't `State` an abstract class?". You will see the reason right on, but essentially the idea here, is to override some behaviors of the base class. When we are inside `OnState` the only behavior that we need to override is the `off()` method, similarly when we are inside `OffState` the only behavior that we need to override is the `on()` method/ Like so:

```java
class OnState extends State
{
    // ...

    @Override
    public void off(LightSwitch ls)
    {
        System.out.println("Switching light off...");
        ls.setState(new OffState()); // we not build this yet
    }
}
```

We have to take the `LighSwitch` we got as an argument and call the `setState()` on that, let's implement that now.

```java
class LightSwitch
{
    private State state;

    public LightSwitch()
    {
        this.state = new OffState();
    }

    public void setState(State state)
    {
        this.state = state;
    }

    void on() { }
}
```

Quick break here, what you suppose we have to do to fulfil the `on()` method? If your guess was something like this:

```java
class LightSwitch
{
    // ...

    void on()
    {
        state = new OnState();
    }
}
```

Then I'm sorry to say that you're wrong, we actually let the state itself, the current state, handle the transition to the new state. Essentially what happens here is that we take the current state, and we call the desired method on it with the current implementation.

```java
class LightSwitch
{
    // ...

    void on()
    {
        state.on(this);
    }

    void off()
    {
        state.off(this);
    }
}
```

Coming back to the idea of `State` we can now see what's happening here, and figure out why is this so complicated, that's because not only we want to handle the situation where somebody is in the `OnState` and they try to switch off the light but, in addition we want to handle a situation where somebody is in the `OffState` and they try to switch off the light. For covering such cases we need to adjust our `State` class a bit.

```java
class State 
{
    void on(LightSwitch ls)
    {
        System.out.println("Light is already on");
    }

    void off(LightSwitch ls)
    {
        System.out.println("Light is already off");
    }
}
```

Hopefully this was clarifying to you in terms of what's going on. It's a somehow bizarre situation because we have the state changing reference in the owner of the state to a different state. It's idiomatic in terms of the Gang of Four but, it's not idiomatic in terms of how we write code today.

But hey, this code actually works fine, let's check out a demo.

```java
class Demo
{
    public static void main(String[] args)
    {
        LightSwitch ls = new LightSwitch();

        ls.on();
        ls.off();
        ls.off();
    }
}
```

As an output we get:

```
Light turned off
Switching light on...
Light turned on
Switching light off...
Light turned off
Light is already off
```

## Handmade State Machine

Now we're going to learn how to hand roll the state machine for making a phone call. Let's first start by defining the states a phone can actually be in. 

Let's define an enum for that instead of one class per state, by doing so we avoid any unnecessary overhead especially when our states doesn't have any special behaviors.

```java
enum PhoneState
{
    OFF_HOOK,
    ON_HOOK,
    CONNECTING,
    CONNECTED,
    ON_HOLD
}
```

The `OFF_HOOK` state is going to be the starting state when you make a phone call, and the `ON_HOOK` is going to be the terminal state when the execution of the state machines stops, indicating that the call is over.

We transitioned from one state to another and those transitions happens due to triggers. Because of that let's also define an enum for these.

```java
enum PhoneTrigger
{
    DIAL,
    ANSWER,
    HANG_UP,
    ON_HOLD,
    OFF_HOLD,
    LEFT_MSG,
    FINISH
}
```

Now that we've got the states as well as the triggers what we need to do is to define the rules which make up the state machine.

```java
class PhoneDemo
{
    
}
```

In order to define the state machine what we're going to do is starting defining a `Map` from a particular state that we should currently be in, so starting at this particular state I can transition to a number of states depending on the triggers.

```java
class PhoneDemo
{
   private static Map<PhoneState, List<Map.Entry<PhoneTrigger, PhoneState>>>
        machineRules = new HashMap<>();
}
```

Now we're going to initialize our state machine rules in a static block.

```java
class PhoneDemo
{
    // ...

   static
   {
       machineRules.put(PhoneState.OFF_HOOK, List.of(
           Map.entry(PhoneTrigger.DIAL, PhoneState.CONNECTING),
           Map.entry(PhoneTrigger.FINISH, PhoneState.ON_HOOK)
       ));

       machineRules.put(PhoneState.CONNECTING, List.of(
           Map.entry(PhoneTrigger.HANG_UP, PhoneState.OFF_HOOK),
           Map.entry(PhoneTrigger.ANSWER, PhoneState.CONNECTED)
       ));

        machineRules.put(PhoneState.CONNECTED, List.of(
           Map.entry(PhoneTrigger.LEFT_MSG, PhoneState.OFF_HOOK),
           Map.entry(PhoneTrigger.HANG_UP, PhoneState.OFF_HOOK),
           Map.entry(PhoneTrigger.ON_HOLD, PhoneState.ON_HOLD)
       ));

        machineRules.put(PhoneState.ON_HOLD, List.of(
           Map.entry(PhoneTrigger.OFF_HOLD, PhoneState.CONNECTED),
           Map.entry(PhoneTrigger.HANG_UP, PhoneState.OFF_HOOK)
       ));
   }
}
```

The first statement basically says: if I'm currently in the `OFF_HOOK` state then the kind of things I can do is `DIAL` a call, in which case I'll move into the `CONNECTING` state, or I can `FINISH` using the phone, in which case I'll move into the `ON_HOOK` state. The same logic goes for the others statements as well.

Another thing we have to do is define a `currentState` reference in order to get the current state of the phone, and also a `exitState` reference so that we can know when we should stop executing the state machine and indeed the entire application.

```java
class PhoneDemo
{
    // ...

    private static PhoneState currentState = PhoneState.OFF_HOOK;
    private static PhoneState exitState = PhoneState.ON_HOOK;
}
```

Now let's create a `main()` method in order to test our state machine. First thing we're going to code inside it is grabbing the reader's input, and then list all the available triggers, and ask the user to input a particular option on the console, so we can finally execute that appropriate part of the state machine.

```java
class PhoneDemo
{
    // ...

    public static void main(String[] args)
    {
        BufferedReader consoleReader = new BufferedReader(
            new InputStreamReader(System.in)
        );

        while (true)
        {
            System.out.println("The phone is currently in " + currentState + " state.");
            System.out.println("What do you want to do?");

            for (int i = 0; i < machineRules.get(currentState).size(); i++)
            {
                PhoneTrigger trigger = machineRules.get(currentState).get(i).getKey();
                System.out.println("" + i + ": " + trigger);
            }

            boolean parseOk;
            int choice = 0;
            do
            {
                try {
                    System.out.println("Inform your choice: ");
                    choice = Integer.parseInt(consoleReader.readLine());
                    parseOk = choice >= 0 && choice < machineRules.get(currentState).size();
                } catch (IOException e) {
                    parseOk = false;
                } catch (NumberFormatException e) {
                    parseOk = false;
                }
            } while (!parseOk);

            currentState = machineRules.get(currentState).get(choice).getValue();

            if (currentState == exitState) break;
        }

        System.out.println("The phone is now in " + currentState + " state.");
    }
}
```

Let's try it out!

```bash
The phone is currently in OFF_HOOK state.
What do you want to do?
0: DIAL
1: FINISH
Inform your choice: 
0
The phone is currently in CONNECTING state.
What do you want to do?
0: HANG_UP
1: ANSWER
Inform your choice:
1
The phone is currently in CONNECTED state.
What do you want to do?
0: LEFT_MSG
1: HANG_UP
2: ON_HOLD
Inform your choice:
2
The phone is currently in ON_HOLD state.
What do you want to do?
0: OFF_HOLD
1: HANG_UP
Inform your choice:
0
The phone is currently in CONNECTED state.
What do you want to do?
0: LEFT_MSG
1: HANG_UP
2: ON_HOLD
Inform your choice:
0
The phone is currently in OFF_HOOK state.
What do you want to do?
0: DIAL
1: FINISH
Inform your choice:
0 
The phone is currently in CONNECTING state.
What do you want to do?
0: HANG_UP
1: ANSWER
Inform your choice:
1
The phone is currently in CONNECTED state.
What do you want to do?
0: LEFT_MSG
1: HANG_UP
2: ON_HOLD
Inform your choice:
1
The phone is currently in OFF_HOOK state.
What do you want to do?
0: DIAL
1: FINISH
Inform your choice:
1
The phone is now in ON_HOOK state.
```

This demonstration shows us how you can implement your own state machine without really depending on any kind of external framework. All we've done is defined a `Map` which specifies that for any given state there is a list of possible transitions and their associated states, and then you simply play out this state machine manually by getting the user's input and moving from one state to another.

## Spring State Machine

In previous lesson we looked at how to make your own state machine and how to orchestrate or run the state machine without any external libraries. This time around we're going to be using an external library, even though there are lots of libraries, is the [Spring State Machine](https://spring.io/projects/spring-statemachine) library. Let's basically see how can we rewrite the example we've written in the previous lesson using the Spring State Machine library.

First we're going to define the states as well as the triggers, except that in the context of Spring framework state machine library those triggers are actually called *Events*. The idea is that an *event* is something that happens whereas it triggers something, so when an event happens you transition from one state to another.

```java
enum States // the spring state machine library already provides a type called State
{
    OFF_HOOK,
    ON_HOOK,
    CONNECTING,
    CONNECTED,
    ON_HOLD
}

enum Events
{
    DIAL,
    ANSWER,
    HANG_UP,
    ON_HOLD,
    OFF_HOLD,
    LEFT_MSG,
    FINISH
}
```

What we're going to do next is to actually define our state machine:

```java
public class Demo
{
    public static StateMachine<States, Events> buildMachine() 
        throws Exception
    {
        
    }

    public static void main(String[] args) throws Exception
    {
        
    }
}
```

The idea behind building a state machine is you don't do it by hand, instead what the framework gives you is a `StateMachineBuilder` object which you can use to actually build a state machine, like so:

```java
public class Demo
{
    public static StateMachine<States, Events> buildMachine() 
        throws Exception
    {
        StateMachineBuilder.Builder<States, Events> builder 
            = StateMachineBuilder.builder();

        return builder.build();
    }

    // ...
}
```

Now all that we have to do is to configure this state machine by telling it what are the possible states and the possible events that can happen, and also what the initial state is.

```java
public class Demo
{
    public static StateMachine<States, Events> buildMachine() 
        throws Exception
    {
        StateMachineBuilder.Builder<States, Events> builder 
            = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
                .initial(States.OFF_HOOK)
                .states(EnumSet.allOf(States.class));

        return builder.build();
    }

    // ...
}
```

Now the fun part which is defining the transition:

```java
public class Demo
{
    public static StateMachine<States, Events> buildMachine() 
        throws Exception
    {
        StateMachineBuilder.Builder<States, Events> builder 
            = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
                .initial(States.OFF_HOOK)
                .states(EnumSet.allOf(States.class));

        builder.configureTransitions()
            .withExternal()
                .source(States.OFF_HOOK).target(States.CONNECTING)
                .event(Events.DIAL)
                .and()
            .withExternal()
                .source(States.OFF_HOOK).target(States.ON_HOOK)
                .event(Events.FINISH)
                .and()
            .withExternal()
                .source(States.CONNECTING).target(States.OFF_HOOK)
                .event(Events.HANG_UP)
                .and()
            .withExternal()
                .source(States.CONNECTING).target(States.CONNECTED)
                .event(Events.ANSWER)
                .and()
            .withExternal()
                .source(States.CONNECTED).target(States.OFF_HOOK)
                .event(Events.LEFT_MSG)
                .and()
            .withExternal()
                .source(States.CONNECTED).target(States.OFF_HOOK)
                .event(Events.HANG_UP)
                .and()
            .withExternal()
                .source(States.CONNECTED).target(States.ON_HOLD)
                .event(Events.ON_HOLD)
                .and()
            .withExternal()
                .source(States.ON_HOLD).target(States.CONNECTED)
                .event(Events.OFF_HOLD)
                .and()
            .withExternal()
                .source(States.ON_HOLD).target(States.OFF_HOOK)
                .event(Events.HANG_UP);

        return builder.build();
    }

    // ...
}
```

Now it's time for us to complete our example by filling the `main` method:

```java
public class Demo
{
    // ...

    public static void main(String[] args) throws Exception
    {
        // create the state machine
        StateMachine<States, Events> machine = buildMachine();

        // start the state machine
        machine.start();

        // define the exit state
        States exitState = States.ON_HOOK;

        // get the reader's input
        BufferedReader consoleReader = new BufferedReader(
            new InputStreamReader(System.in)
        );

        while (true)
        {
            State<States, Events> currentState = machine.getState();
            System.out.println("The phone is currently in " + currentState + " state.");
            System.out.println("What do you want to do?");

            List<Transition<States, Events>> transitions = 
                machine.getTransitions()
                    .stream()
                    .filter(t -> t.getSource().equals(currentState))
                    .collect(Collectors.toList());

            for (int i = 0; i < transitions.size(); i++)
            {
                System.out.println("" + i + ": " + transitions.get(i).getTrigger().getEvent());
            }

            boolean parseOk;
            int choice = 0;
            do
            {
                try 
                {
                    choice = Integer.parseInt(consoleReader.readLine());
                    parseOk = choice >= 0 && choice < transitions.size();
                } catch (IOException e) 
                {
                    parseOk = false;
                }
            } while (!parseOk);

            // make the transition
            machine.sendEvent(transitions.get(choice).getTrigger().getEvent());

            // check if we're in the exit state
            if (machine.getState().equals(exitState)) break;
        }

        // state machine has finished
        System.out.println("The phone is now in " + machine.getState() + " state.");
    }
}
```

And that's it! Check out the output to see how the state machine works.

You will realize that there's a lot more power in that framework in comparison to what we've implemented by hand. Also, we're not reinventing the wheel once again, by running a state machine that has been tested by many people.

Typical business scenarios you're going to have exactly this kind of set up, a bunch of enums, which are then connected, and transition from one to another using events/triggers.

#### Note

Due to updates on the Spring State Machine library, this example has been updated to use *Reactive Streams*. You can check out the updated version [here](./src/SpringStatePattern.java).