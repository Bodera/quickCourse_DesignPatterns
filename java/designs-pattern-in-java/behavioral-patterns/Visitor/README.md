# Visitor

> Allows adding extra behaviors to entire hierarchies of classes.

Essentially, the visitor design pattern allows us to give extra behaviors, not just to individual classes like you would if you were building just an ordinary decorator, but the idea is to add extra behaviors to an entire hierarchy of classes. You take a base class with all of its descendants and their descendants, and you want to give them some new capability.

How we do this? Well we want to define a new operation on an entire class hierarchy right, let's suppose you have a document model, where you have this idea of paragraphs and lists and all the rest, then all of a sudden you have this document model fixed, and you want the model to be printable, and furthermore, you want the printable into different formats like `HTML` or `Markdown` or something else. We don't want to go into every single element of the hierarchy and tell this element how to render itself, because this concept breaks the open-closed principle, thus we want to avoid this kind of behavior.

What we do want, though, is some sort of external component which actually knows how to correctly traverse and print the entire hierarchy. And, as a result, we need access to the non-common aspects of classes in the hierarchy. See, we cannot just extract a single interface and go through that interface for every single class, this isn't suitable. We need access to the actual members, but how?

We can for sure create an external component, let's say a something like `HandleRendering`, but we have to make sure to avoid any type checks, any kind of casting, or `instanceOf`, or comparing the class values and so on.

This is precisely the kind of functionality that the visitor pattern provides, which we're going to build. We're going through different examples of how to implement this pattern, starting from a very intrusive till some visitors which use reflection all the way up to whatever the best visitor we can build.

Ultimately, the visitor pattern is all about having a component called _visitor_, which is allowed to traverse the entire inheritance hierarchy. Implemented by propagating a single `visit()` method throughout the entire hierarchy.

It's quite intrusive because you have to add at least this one method to every to single element in the hierarchy, but that's only one method. We do this once and that's it, we're done and never have to do it again. 

This is the classical implementation of visitor, we're also going to look at a few variations as well.

Suggested order to follow the lessons on this chapter:

- Intrusive visitor
- Reflective visitor
- Classic visitor (double dispatch)
- Acyclic visitor
