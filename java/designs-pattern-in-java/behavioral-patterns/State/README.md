# State

In this section of the course we're going to take a look at the state design pattern. So you might think - "Hold on, every object, or most objects, have state in their fields so why are we even discussing it?" = In the representation of the Gang of Four this design pattern basically suggests that the state of the system kind of controls the way that it operates and, it also ties this idea of states machines, or finite machines.

As a motivation behind using the state design pattern consider an ordinary telephone, what you can do with a telephone typically depends on the current state of the telephone as well as the line. For example, if the telephone is ringing or if you want to make a call, you need to pick up the phone. That's a requirement to be able to actually respond to somebody or to actually make a call somewhere. The phone must be off the hook to talk to somebody or make a call. And if you try calling someone, and the line is busy, then you put the handset down.

Changes in the state of the system can be explicit or, they can occur in response to some event - that's the Observer pattern. And that's up to you to define how your state machine operates and how to specify the state of the system.

So the state design pattern is quite simply a pattern in which the object's behavior is determined by its state (and it doesn't have to be a single field, it can be quite a complex object). An object transitions from one state to another (something needs to trigger a transition).

A formalized construct which manages state and transitions is called a state machine, which is typically a class, can be a class that you yourself build or a class taken from a third-party library.

## Summary

Given sufficient complexity, it's worth formally defining the states and events/triggers transitions from one state to another. If you have a very simple system you can certainly do it just by calling a couple of methods, but if you have a complicated system with lots of states and lots of possible transitions then you can use either your own solution or some third-party solution.

Some things that you can define in relation to the state machine:

- State entry/exit behaviors
- Actions when a particular event causes a transition
- Guard transitions enabling/disabling transitions
- Default actions when no transitions are found for a particular event
