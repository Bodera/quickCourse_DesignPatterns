# Façade

> Exposing several components through a single interface.

Façade is all about exposing several components through a single interface, making things convenient for the end user.

Let's take a house for example. Essentially a house is a system which tries to balance complexity on the one hand because, it's built upon fairly complicated systems, and on the other hand it has to have both usability/presentation which means, when you're buying a house you're essentially buying the way the house looks and how usable it is for your liking.

If you think about a typical home then you know how many subsystems it has, such as:
- Electricity
- Heating
- Plumbing

And also you have complex internal structures like:
- Drywall
- Floor layers

The fact is that you don't want to worry about this and indeed the end users buy a house they are not really exposed to the internals, there might be unpleasant bugs in the flooring for example but, you'll never see them because you don't go opening floors at your house. When you buy a house you just give that superficial look at it, the location, and then you decide if you buy it or not.

The same goes for software, sometimes the consumers of your API just want something simple to work with. For sure that behind the scenes there are many systems working together performing different kind of complexity tasks to provide such flexibility, but for the API consumers what really matters is that it _just works_. You know, calling a couple of simple functions and get the system running.

A typical example of such mechanism is the console prompt. It's actually a complicated piece of machinery, it has buffers, plenty of settings, but the fact is that you can just call `System.out.println()` and get it working. You don't have to worry about different buffers, and reallocations, and buffer overruns, all of this is hidden from you, so you just have to interact with a public interface of what's essentially a system or indeed sets of systems working together.

Having said that, the façade design pattern is all about providing a simple easy to understand interface over a large and sophisticated body of code.

## Summary

Essentially you build a façade to provide a simplified API over a set of classes or indeed, if you want to take it bigger, a set of subsystems. It doesn't matter how you scale it because the idea is still the same.

You may wish at some point to optionally expose the internals of your subsystems through façades, this is typically done for the power users. Some people are happy with the superficial interface, but some people like to have more control to perform more deep optimizations. So you can provide a chunk of APIs which are high-level, and another chunk of APIs which are low-level. It can lead to unpredictable results, but this is the typical issue where with great power comes great responsibility and all that.

So you may allow users to actually *escalate* to use more complex APIs if they want to.