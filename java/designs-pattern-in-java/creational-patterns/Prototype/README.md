# README

In this section of the course we're going to take a look at the _Prototype_ design pattern and how it's all about object copying because it's easier to copy an existing object instead of fully initialize a new one. So we're going to delve deep into the replication of objects.

## Motivation

Happens that complicated objects like Cars or Smartphones for example, aren't designed from scratch. If you think that anytime a factory wants to make a new model of a car they just do it from scratch you'd be mistaken. They take an existing design of a car and they change it, improving it somehow, they reiterate existing designs making them more palatable to consumer and the _Prototype_ design pattern is exactly the same approach in software engineering.

So essentially the prototype is an object which can be partially or fully constructed. Let's say you've already defined a very complicated type, maybe you initialize it using some _Builder_, and then you simply want to make variations on it. In order to make these variations you need to make a copy of the prototype and then customize it. Copy is also sometimes referred as cloning, what is really needed here is something called **deep copy**, not only we're copying the object reference but all the objects references inside it as well by making new instances of them that replicates the state of those references in recursively mode. By doing that you'll be able to make a complete copy of an object and changing something on this primary object doesn't affect the copies that have been made.

Another important thing is that we've to provide an API, let's say a _Factory_ for instance, to make the cloning of existing objects a convenient affair.

In resume a protoype is a partially or fully initialized object that you make a copy of, and then you subsequently use for your own benefit.

- [Why not use cloneable](./Why-Not-Use-Cloneable.md)
- [Use copy constructors instead](./Copy-Constructors.md)
