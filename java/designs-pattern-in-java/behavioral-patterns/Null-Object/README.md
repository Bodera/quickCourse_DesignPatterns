# Null object

> A behavioral design pattern with no behaviors 😅

In this section we're going to talk about the null object pattern. This pattern is not present in the Gang of Four Design Patterns book but, it's sufficiently important for us to discuss it. It's classified as a behavioral design pattern even though this pattern doesn't really have any behaviors.

Imagine a situation where you have components `A` and `B`, and component `A` uses component `B`. Typically, there is an assumption that `B` is non-null. If you're using dependency injection, what you do is to inject an instance of `B` instead to inject some `Optional<B>` type. Similarly, when you use `B` you don't do a null check on every single call you make, what makes sense.

There is no option of telling `A` not to use an instance of `B`, components are not configured that way, if `A` uses `B` then that usage is effectively hard-coded. But you can supply some inheritor of `B`, that is called _polymorphism_ and, it is allowed, what you can't do is not supplying any instance at all.

This is what gives rise to the null object pattern because sometimes we don't want `B` to be there, and we don't want any operations of `B` to be actually invoked. As a result what we do is to build a no-op, non-functioning inheritor of `B` (or some interface that `B` implements) and we pass that into `A`.

The null object is all about building some sort of no-op object that conforms to the required interface, satisfying a dependency requirement of some other object but don't do a single thing, just like an empty object.

## Summary

So the idea is very simple, you implement the required interface, and you rewrite the methods if there are any with empty bodies. There are some complications for example if a method is non-void you want to return the default value for a given type, and if these values are actually used anywhere then you're in real trouble because in this case the null object pattern kinds of falls apart.

What you do after you've implemented the null object is you supply an instance of the null object, and by the way yes you can make it a _Singleton_ if you want because there is no point in having multiple null objects unless these instances are compared internally in which case once again you're in trouble. So you can supply an instance of null object in the location of the actual object and then simply cross you fingers because essentially it's very difficult to say whether these operations are going to fail, so suddenly if you have an immutable kind of null object which is not mutating and the state isn't requested to actually do anything that mutates then you're fine, but this is a rare situation typically an object will have methods returning values and in this case is very difficult to say what is actually going to happen.

So the null object is not the safest design pattern still sometimes it does make sense to supply it like for testing for example when you want to have a unit test instead of an integration test, you want one part to be substituted but, you don't have the option to use dependency injection for that particular case, so what you do is you feed in a null object and that object kind of tries to behave like the underlying object without actually doing anything and thus modifying the state of the system.
