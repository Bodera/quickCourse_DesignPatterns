# Singleton

In this section of the course we're going to take a look a somewhat controversial design pattern and we're going to discuss whether it's really as bad as people say it's.

After the publication of the book `Design Patterns: Elements of Reusable Object-Oriented Software`, all the patterns are instructed to be implemented as said with the exception of the *Singleton*:

> When discussing which patterns to drop, we found that we still love them all. (Not really—I'm in favor of dropping Singleton. Its use is almost always a design smell.) - Erich Gamma

You tend to consent with that because quite often it's poor implemented particularly in systems which are theoretically supposed to be extensible and testable.

What we're going to inspect are the problems that *Singleton* has and ways to overcome them.

## Motivation

The motivation for using this pattern is due by some components that are unique for whole system. 

- Database repository

An object that accesses a repository, typically intended to performn a connection and loads up the database inside the constructor method and keeps it on memory to be able to provide information about some particular content, once will be able to access that info you don't really want more than one instance of it, it's pointless. Read it once, there is no reason to read it over and over especially when you're testing code because by definition tests are supposed to be numerous.

- Object factory

In case you have a separate factory component which actually just creates some components do you thing that it's really necessary have more than one instance of it? Of course not because a factory isn't assumed to have any state at all.

- Expensive constructors

There are situations where the constructor call of some object is extraordinarily expensive because it consumes a large amount of resources and we want to call it only once and then, subsequently, after constructing the object we provide to everyone, every single consumer, the exact same instance of that object.

- Prevent others to create copies the component

That could be due by security reasons or just to save resources.

- Need to lazy instantiation and thread safety

By that you assure that there is no way in which case the constructor is __executed__ twice and introducing some sort of race condition. 

That's what a correct implementation of *Singleton* is all about. It's essentially just a component which is instantiated only once and tries to resist the idea of being instantied more than once.
