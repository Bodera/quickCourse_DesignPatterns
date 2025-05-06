# Composite

> Treating individual and aggregate objects uniformly.

This pattern has a simple and very noble goal. It allows us to treat individual or scalar components as well as aggregate objects or collections components uniformly, so both of these can be treated in the same fashion. Now the question is why do we want this in the first place?

We know that objects can generally use other objects fields or methods through just two different mechanisms: either inheritance or composition. Composition is a principle which states that an object *has* another, enabling us to make compound objects, grouping objects together.

Some examples:

- A mathematical expression composed of simpler expressions.
- A group of different shapes which are all grouped together (polygons).

That's the goal of the composite design pattern, different kind of objects can share the same API. Like `Foo` and `List<Foo>` have commons APIs.

The composite design pattern is all about creating some sort of mechanism for treating individual or scalar objects and compositions of objects in a uniform manner.

## Summary

We've sawn that objects can use other objects via inheritance or composition. Also, some composed and singular objects need similar or indeed identical behaviors, so they need to provide a similar API which we typically do through the use of interfaces. The composite design pattern lets us treat both types of these objects uniformly, we can achieve this using many approaches, but Java supports the idea of iterating a container with `Iterable`. So an object can masquerade as a collection by returning a single element collection containing only `this`.
