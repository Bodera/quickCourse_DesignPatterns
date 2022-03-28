# Gamma Categorization

Regardless of which language of design patterns we're talking about, design patterns can be categorized in three ways (inspired by Erich Gamma one of the authors of Gang of 4):

## Creational Patterns

As the name suggests, these patterns are concerned to the creation (construction) of objects. Such a thing could seem quite simply as invoking the constructor but in actual fact things are quite often a bit more complicated so sure you have __explicit__ creation: that's when you call the constructor you provide a few arguments and you get your initialized object but, sometimes, the creation of an object is actually implicit, and by that I mean that it may happens by the use of some dependency injection framework, or by using reflection, or some other mechanism which actually creates the object behind the scenes. That's also creation it isn't explicit, so it's worth to know about it. 

In addition there are different processes for actually initializing the objects before they are ready to use, sometimes you'll have wholesale creation of an object which means that with a single statement like a single constructor call is actually sufficient to initialize the object but in certain situations initialization itself is a complicated process, this is where we get the so-called piecewise or step-by-step initialization when we need to have several statements or several steps that need to be taken before an object is actually initialized and ready to use.

## Structural Patterns

Takes a role mainly about the structure of the classes, it's concerned with class members, things like class adhering to some interface or not. In this section we're going to see patterns which are wrappers which mimic the underlying interface, so you have a wrapper around the class and that wrapper tries to mimic as much as possible the underlying class the class is actually wrapping. That was a simple example of structural approach, these patterns generally put extra weight on the importance of good API design and some of these patterns are actually all about this idea of replicating the interface as much as possible or making the interface as convenient to use as possible. So in studyng structural patterns you're going to see  some good applications for good API design that makes objects usable and makes API usable for other people.

## Behavioral Patterns

These do not really follow a central theme, they're all differente and each one does their own thing. There is some overlap here and there for example, the Stategy and the Template Method design patterns do basically the same thing but each one does it using completely different object-oriented mechanisms. Despites that the approach of each of these patterns is unique, and tries to solve a particular problem. 
