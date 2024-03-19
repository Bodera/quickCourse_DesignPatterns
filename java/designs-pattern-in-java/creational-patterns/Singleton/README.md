# README 

Suggested order to read those chapters:

- [Presenting the pattern](./Singleton.md)
- [Your First Singleton](./Your_First_Singleton.md)
- [Serialization Problems](./Serialization_Problems.md)
- [Singleton and Threads](./Singleton_And_Threads.md)
- [Inner Static Singleton](./Inner_Static_Singleton.md)
- [Enum Based Singleton](./Enum_Based_Singleton.md)
- [Monostate](./Monostate.md)
- [Multiton](./Multiton.md)
- [Testability Issues](./Testability_Issues.md)
- [Singleton in Dependency Injection](./Singleton_in_Dependency_Injection.md)

## Summary

First of all we saw that making a safe Singleton, and with safe I mean that kind of lazy thread safe construct, is fairly easy by just using diamond operators on a static instance of `Lazy<T>` and then returning it's value.

Second we saw that Singletons are difficult to test, and if you hardcoded Singletons into your designs it's going to be really difficult for you to change it to something else later on. That's why you should follow the dependency inversion principle which means that instead of depending on a concrete implementation of a singleton like `MySinglenton.getInstance()` you should depend on a abstraction, so your components which need a singleton should take an argument perhaps of an interface type so that instead of the singleton that you're originally feeding you can feed them with something else.

Finally you should really consider defining a singleton lifetime not explicitly in that component but instead doing it in a dependency injection container.
