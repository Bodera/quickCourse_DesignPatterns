# Abstract Factory

Now we are going to talk about the abstract factory design pattern. This design pattern isn't particularly common, there's hardly any cases where you're likely to be using it but for sure there are situations where you want to use an abstract factory.

So what's an abstract factory all about? Well the abstract factory is essentially a pattern which makes as correspondence between the hierarchy of objects that you have. We are going to see this in practice using drink machines as an example. Let's suppose you are modeling a drink machine, you can have `Tea` and `Coffe` both implementing some hot drink interface, it makes correspondence between hierarchy of objects in a hierarchy of factories which are used to construct those objects and this is precisely the kind of them that we're going to be doing here.

