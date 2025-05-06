# Template Method

> A high-level blueprint for an algorithm to be completed by inheritors.

The template method design pattern is a way to define a skeleton for an algorithm in a super-class, but then let subclasses override specific steps of the algorithm without changing its structure. It is all about providing high level blueprints of an algorithm to be completed by its inheritors. We've sawn something like this already in the strategy design pattern.

- Algorithms can be decomposed into common parts + specific parts
- Strategy pattern does this through composition
  - High-level algorithm uses an interface
  - Concrete strategies implement the interface
- Template method does this through inheritance
  - High-level algorithm makes use of abstract members
  - Concrete strategies override the abstract members
  - Base class actually keeps the template for the actual algorithms which is invoked to orchestrate the algorithm at the high level

The template method design pattern allows us to define the 'skeleton' of the algorithm in the base class, with concrete implementation defined in subclasses.

## Summary

The idea is once again to define an algorithm at a reasonably high level, that would be typically an abstract class with one known abstract member where the algorithm actually resides, then you define the constituent parts as abstract methods or properties. Finally, you inherit the algorithm class, providing necessary overrides so that the whole thing plays together, the base class template algorithm is invoked but, it's actually invoking the overriding properties and overriding methods.
