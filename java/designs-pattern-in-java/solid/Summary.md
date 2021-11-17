# Summary

Here we will try to summarize what we've learnt in this section of the course about the SOLID design principle

## Single Responsability Principle

- A class should only have one reason to change.
- *Separation of concerns*, different classes handling different, independent tasks/problems. If you have a system which is handling different kinds of concerns it makes sense to put them in different classes so that these can be refactored independently or replaced by something else for example.

## Open-Closed Principle

- Classes should be open for extension but closed for modification. The idea is that if you're coming back into an already written, already tested class and modifying things in order to extend the functionality then this is probably not the best way to go and you should consider using the object-oriented paradigm and inheritance instead of just modifying existing code.

## Liskov Substitution Principle

- You should always be able to substitute a base type for a subtype. We took a look on a situation where the violation of this principle leads to rather unpleasant results through inheritance.

## Interface Segregation Principle

- The basically idea is that you shouldn't be putting too much into an interface. In the case of a protocol that you shoudn't be overloading instead you should split a protocol into separate protocols (or just separate interfaces) and thereby you don't force the implementor to put lots of stubs and throwing exceptions out of not implemented methods.
- This relates to the acronym "YAGNI" or just "You Ain't Gonna Need It". Why force people to implement unecessary methods on that interface in the first place?

## Dependency Inversion Principle

- This has nothing to do with dependency injection, it stands that high-level modules should not depend on low-level ones; use abstractions just like we've covered how code can be refactor to do exactly that.
