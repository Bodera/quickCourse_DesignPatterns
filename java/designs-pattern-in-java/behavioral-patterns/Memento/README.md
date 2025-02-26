# Memento

The goal of the memento design pattern is to keep some sort of _token_ which then allows you to return an object to a particular state.

The motivations to learn this pattern is for example you have some object or a set of components that goes through changes like a bank account which gets deposits and withdrawals. As a consequence, there are different ways of navigating those changes. One way is to record every change using a _Command_ and teach the command to undo and redo the changes. Another approach is to simply save the snapshots of the system at particular points in time and them allow the user to roll back the system to a particular snapshot.

The _Memento_ it's a kind of _token_ or _handle_ representing the system state. Let us roll back to the state when the _token_ was generated. May or may not directly expose state information but, it's typically an immutable construct, so it may provide a certain amount of information about the state of the system without allowing you to actually change the system unless you take this token, and you stick it back in the system explicitly asking it to roll itself back to that particular state.

## Summary

Mementos are generally used to roll back states arbitrarily, it is quite simply a snapshot of the object's state at a particular point in time when the memento was generated. They are simply a token/handle class with (typically) no functions/behavior of its own, on other hand it could be used to implement undo/redo functionality.

Also, the memento is not required to expose directly the states to which it reverts the system. As a matter of fact in some situations you might want to hide those states and make sure that those states are read-only.
