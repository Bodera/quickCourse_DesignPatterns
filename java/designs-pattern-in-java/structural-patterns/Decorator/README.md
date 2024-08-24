# Decorator

What is the motivation for using a decorator?

First you want to augment existing objects with additional functionality. So you already have classes defined, and you want additional functionality in those classes, but you don't want to go into those classes and somehow rewrite them or change their existing code because that would break the __Open Closed Principle__, and also you want to maybe keep the new functionality entirely separate which is consistent with the __Single Responsibility Principle__. However, you do want the constructs that you end up with to interact with existing structure somehow, you do want to decorate that object to be compatible with an API that uses the old object.

Ultimately you have two options on how to do this: if the class isn't final then you can just inherit from that class and you kind of automatically; get some behaviors of that class and them you can build on top of that, is ok to implement you decorator like this, but some classes are final, like the `String` class, you cannot inherit from it in order to create additional functionality, is just not possible. So option two (that we're going to explore here) is building a decorator which is simply an object which references the decorated object, typically it aggregates it, and it adds new functionality on the side.

So the decorator design pattern facilitates the addition of behaviors to individual objects without directly inherit from them.

## Summary

A decorator typically keeps a reference to the decorated objects, and it may or may not forward the calls to the decorated object (in case you want then use `Delegate Methods`). We also learned about the static variation of the decorator, where you can bake the decorated types right inside the type definition, which looks like this:

```java
X<Y<Foo>> (
    /*heavy initialization*/
)
```

Unfortunately due to type erasure and the inability to inherit from type parameters in Java we got this unpleasant syntax. The downside of a static decorator is that once you write it you cannot change it anymore, whereas the dynamic decorator's the composition can actually happen at runtime which is a great benefit.
