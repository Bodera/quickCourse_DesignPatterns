# README

In this section of the course we're going to talk about factories and also going to cover two Gang of Four Design Patterns, we're going to cover the factory method as well as the abstract factory.

Suggested order to follow the lessons on this chapter:

- Factory Method
- Factory
- Abstract Factory

## Motivation

- Object creation logic becomes too convoluted
- Constructor is not descriptive
  - Name mandated by name of containing type
  - Cannot overload with same sets of arguments with different names
  - Avoids "overloading hell"
- Wholesale object creation (non peacewise, unlike Builder) can be outsourced to.
  - A separate function (Factory Method)
  - That may exist in a separate class (Factory)
  - Can create hierarchy of factories with Abstract Factory

So what is the motivation for using factories in the first place. Well we're once again talking about the creation of objects and the fact that sometimes the logic which is involved in the creation of an object can get too complicated and unfortunately the constructors are rather limiting in terms of their capabilities of how they could help us creating objects.

So one of the problems with constructors is typically the name of the constructor is the name of the containing type. That's how most of the object oriented programming language is actually working, this is a particular problem because since we don't have any flexibility in the name what happens is we don't have any ability to provide the user additional hints as to what the arguments mean for example.

Now we can't unfortunately overload a constructor with same sets of arguments so if you have a point being initialized in either Cartesian coordinates X and Y or a Polar coordinates row and theta you cannot simply make two overloads with different names of arguments. This is something that's quite possible in languages such as Objective-C and Swift but unfortunately it's not possible in Java, and typically you end up with something popular known as "overloading hell" when you end up with lots and lots of different constructors you eventually will have constructors calling others in order to avoid duplication of logic and you will depend on help of some powerful tool or IDE just to help you figure out what the hell is going on.

So just as a reminder we're still talking about wholesale object creation so we're not talking about something like the builder scenario where you have an object and you call a couple of different methods in order to initialize it, here at wholesale object creation you just make a single call but that call doesn't have to beat to a constructor in deed that can be to something else and that's where the factory patterns kind of come in.

So one of the options is a factory method, that's a design pattern where you effectively make a separate method typically a static method which actually returns an object, and this static method of course can have an arbitrary name it's not restricted like a constructor and because it has arbitrary names you can also have lots of flexibility in naming arguments because you don't have to do overloads unlike with a constructor.

Then we're going to talk about factorise proper, the thing is that a factory doesn't get mentioned in the gang of four books but of course it's also another pattern and it's probably the most common pattern, so basically this idea that when you have the construction logic for several different kinds of classes or one class with different arguments you migh want to do separation of concerns on that as well and put that into a separate factory class and that's what we call a factory.

Now there is also a slightly more complicated scenario where you have a hierarchy of objects that you want to create and you want to have a corresponding hierarchy of factories related to those objects and that is represented by the abstract factory design pattern.

So what is a factory im simple terms? Well quite simply a factory is a component whose sole responsibility is the wholesale creation of objects.
