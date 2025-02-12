# Mediator

The mediator design pattern facilitates the communication between components by letting the components being unaware of each of other's presence or absence in the system. The motivations for learning this design pattern is essentially that components may go in and out of a system at any time.

- Chat room participants
- Players in an MMORPG

It makes no sense for the different participants to have direct references to one another because those references can go dead at any time. So the solution here is to have then all refer to some central component that facilitates communication, and that component is called the `mediator`.

So the mediator design pattern necessitates a component that simply facilitates communication between the different components without them necessarily being aware of each other, or having direct or preferential access to one another.

