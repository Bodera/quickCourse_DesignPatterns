# Mediator

The mediator design pattern facilitates the communication between components by letting the components being unaware of each of other's presence or absence in the system. The motivations for learning this design pattern is essentially that components may go in and out of a system at any time.

- Chat room participants
- Players in an MMORPG

It makes no sense for the different participants to have direct references to one another because those references can go dead at any time. So the solution here is to have then all refer to some central component that facilitates communication, and that component is called the `mediator`.

So the mediator design pattern necessitates a component that simply facilitates communication between the different components without them necessarily being aware of each other, or having direct or preferential access to one another.

## Summary

Essentially to get the mediator working for you first you need to create a mediator, and then you have each object in the system refer to it. Typically, you would refer to it in a field and perhaps use something like constructor injection to make sure the mediator is actually available in every single component. And yes, typically a mediator is a singleton as well because you don't really need more than one.

The mediator then engages in bidirectional communication with all of its connected components or on the other hand every single component can actually have functions calling on the mediator. So the mediator has functions the components can call.

Also is quite common to use event processing mechanisms such as reactive extensions and these make the communication between the different components a lot easier to implement. 