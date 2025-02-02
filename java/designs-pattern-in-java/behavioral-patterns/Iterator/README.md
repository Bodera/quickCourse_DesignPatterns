# Iterator

This design pattern is designed specifically to help you traverse data structures, to help you walk data structures and iterate their elements. The motivation for using the iterator is because traversal is a core functionality of various data structures, a tree for example, there are different ways of traversing that three and so you can build different iterators for exactly this purpose.

An `iterator` is just a class which facilitates that traversal. Typically, what the Iterator does is to keep reference of the current element that you're actually kind of "sitting on" and it knows how to move to a different element.

To this end Java has interfaces `Iterable<T>` and `Iterator<T>`. The `Iterator<T>` is the interface that you need to implement in order to specify you own iterator, so it basically is used for specify the iterator API that can be used to traverse a particular thing. The reason why you need `Iterable<T>` is due that this tells you that a class can yield an iterator and if a class implements `Iterable<T>` it can be used to support for example sticking it in a for loop like `for (Foo foo : bar) { ... }` only works if `bar` implements `Iterable<Foo>`.

The iterator design pattern is just an object that facilitates the traversal of a particular data structure.

## Summary

We saw that an iterator is quite simply an object which specifies how you can traverse some other data structure. One very sad downside of iterator is they cannot be recursive - they cannot suspend execution and then resume execution later on, as result some simple algorithms like tree traversal algorithm ends up hideous because there is no support for coroutines.

Just a reminder, the iterator typically implements `Iterator<T>` and the object that's meant to be iterable implements the `Iterable<T>` interface. Here is a challenge because for example for a binary tree there are different ways of iterating the object and `Iterable` asks you to make a choice upon which iterator is going to be the default, but we can always expose other iterators using additional methods, so that's not such a big deal.
 