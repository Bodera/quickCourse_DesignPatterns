# Chain of responsibility

> Sequence of handlers processing an event one after another.

To grasp what this pattern is all about let's imagine a situation where you're working Volkswagen and you decide that whenever somebody does emission tests on your cars your computer is not going to lie, so they're going to reduce the toxic output so that everybody thinks your cars are the most environmentally friendly.

This is clearly unethical behavior, and the question is who actually takes the blame for this? And it typically goes up the chain:

- You can blame an employee

We can say that some employee messed up, tried to cheat, and we're going to fire him and just forget it ever happened.

However, this guy typically has a manager, so must ask - "Did the manager know about this?".

- You can blame a manager

If the manager knew about this than it may happen to be the department policy to just lie and cheat. But maybe the CEO knew about this.

- You can blame a CEO

If the CEO knows about this than in this case the entire company is corrupt and the company should be fined, the CEO could face responsibility for these kinds of actions, and so on and so forth.

This is typically the illustration of the chain of responsibility. First we have the employees, then we have the managers, then we have the CEOs, and they are all responsible, and depending on the kind of unethical behavior that has occurred the actual consequences may impact one or more of these individuals in the chain.

Let's have a look at another example of a chain of responsibility. Suppose you create some graphical element on a form. So for example you click a button, the question is who actually handles this event? Typically, there is a whole chain of responsibility in terms of the components we can actually handle the clicking event:

- The button itself handles it, then stops further processing, so it doesn't go up the chain.
- The underlying group box which the button belongs can receive the event and handle it in its own special way.
- The underlying window, so after all the button has handled the event and the group box would also have handled it, then you can also have the window itself handle the event.

Another example is a collectible card game, a computer game, so you typically have:

- Creature has attack and defense values 
- Those can be boosted by other cards

So a chain of responsibility is essentially just a chain of components who all get a chance to process either a command or a query, and they can optionally have some sort of default processing implementation, and they can also terminate the processing chain and thereby preventing the rest of objects in the chain, avoiding them from processing the command or the query, and this largely depends on the scenario that you're dealing with.

## Summary

Generally the chain of responsibility can be implemented quite simply as a chain of references where you simply change the method call, or it can be implemented as a centralized construct like the event broker that we've built.

The idea in both cases is that you list objects into the chain of responsibility and in some cases you might want to control the order inside the chain as well. And you can also remove the objects from the chain, where in our example we've used the `AutoCloseable` interface to unsubscribe from the chain after the `close` method call.