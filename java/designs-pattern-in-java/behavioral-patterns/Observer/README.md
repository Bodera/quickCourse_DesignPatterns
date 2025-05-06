# Observer

> I always feel like somebody's watching me

The motivation to learn the observer design pattern is quite simple, we need to be informed when certain things happen. For example, you have an object, and you want to be informed when an object's field changes, or when some object does something that worth to be aware of, or in case some external event occurs outside your system.

So what you want to do is to listen to those events and be notified when those events actually occur. The old-fashioned kind of Java approach for doing so is by adding some listening pattern like an `addFooListener()` method, and then you add the listener to the object to notify the listeners when the event occurs. But, nowadays, Java is a lot more flexible, and it's a lot easier to actually build _observers_ and _observables_. Java now has a **functional objects**, so if you want some action to occur you can ramp that action in a `Supplier<T>`, or a `Consumer<T>`, or a `Function<T>`, then you store those objects in an array and fire them whenever necessary.

So the observer design pattern is basically an object that wishes to be informed about events happening in some system or some object. The entity generating the events is an _observable_.

## Summary

The _observer_ is generally an intrusive approach so the _observable_ object must provide some sort of `Event` or other API to subscribe to. You also need to take special care when it comes to multi-threading and using an _observable_ object from multiple threads, particularly when it comes to unsubscribing from particular event.

We don't have any dedicated API in Java because Java used to have `Observer` and `Observable` interfaces which are now deprecated, but you can take a look at the re-active extensions: `org.reactivestreams.Observable` and `org.reactivestreams.Observer`, beware they're not the same thing as we're talking about here because re-active extensions deals primarily with sequences of data that's being fed, and it's a much more complicated topic. The idea is there are interfaces out there that you might want to try to use somehow, or simply build your own.
