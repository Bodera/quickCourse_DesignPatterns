# Adapter

Now that we're ready to move on structural design patterns let's first see the _Adapter_ which concept is very simple. Is all about getting the interface that you want from the interface that you were given by some system or other.

So the typical representation of an _Adapter_ is just the same as you have in power adapters.

<img src="./src/assets/power_adapter.png" width="250" height="250">

So you know that electrical devices in the real world have different power requirements, and you can speak of these requirements as **interface requirements**. Let's say, voltage, plug type, all of these may vary from place to place, and we cannot take the gadgets that we have with us just modify them to support every possible interface and indeed the manufacturers of electrical devices do not give you an adapter for every country out there, so is up to you to buy one.

We need a special device which is called an adapter to give us the interface that we require from the interface that we have. The _Adapter_ pattern consists in a software, which typically can be a class or an interface, which adapts an existing interface _X_ to conform to the required interface _Y_.

In short terms, let's say that you are getting interface `B` from some system, but some other system requires interface `A`. What you have to do is sort of connect one to the other, and you write an extra piece of code to make that possible.

## Summary

Implementing an Adapter is rather easy because all you have to do is to determine the API that you are consuming and the API that you need to provide. Then you create the components which typically aggregates the adapter. Also, you may end up having intermediate representations of state, so if your adapter has to generate certain information for the adapted interface (the required one) then you would have to store additional data, and this means that you have to be careful about how long this data is stored for and where to store this. This is where you would use caching and other optimizations to make sure that as you adapt one interface to another, you're not spawning too much data, you're not taking up too much memory, and also you're cleaning up memory when it's no longer required.
