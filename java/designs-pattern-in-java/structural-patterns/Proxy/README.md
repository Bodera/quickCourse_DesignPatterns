# Proxy

> An interface for accessing a particular resource.

A proxy is really just an interface for accessing a particular resource. But the key thing about the proxy is that it's interface looks just like the interface of the object that you're actually attempting to access.

So what is the motivation for using a proxy? Let's assume that you are calling `foo.bar()` okay, you have an object called `foo`, and you're calling the method `bar()` on it. Just this already make a lot of assumptions. For example, you're assuming that when you call `foo.bar()` the stuff that's inside `bar()` is going to happen in the same process as the process you're actually in. This might be ok for the time being, but what if later on you decide that you want to put all the `Foo` related operations into a separate process? Could it be either be on the same machine, could even be in the cloud. Another question is, given that you want to put all of `Bar` operations somewhere else is it really possible to avoid changing your code in any significant way? 

That's precisely what the proxy design pattern is for. It basically provides you the same interface as the one you've been using, but it gives you an entirely different behavior behind the scenes.

The example you've got introduced so far which looks like a local object but actually works as a remote interface to some system somewhere else is called a *communication proxy*, and just one kind of large number of proxies that you can build. You have different proxies available for things like logging, virtual proxies, guarding proxies, and so on and so forth.

Here you're going to be presented to a small selection of some proxies that people actually build for their development needs.

Just to make it clear, a formal definition of a proxy is a class that functions as an interface to a particular resource. That resource may be remote, expensive to construct, or may require logging or some other added functionality.

## Summary

We saw that typically a proxy has the same interface as the underlying object. And to make a proxy you simply replicate the existing interface of an object, then of course you change its underlying functionality, so you add behind the scenes into those replicated members, new functionality as you're kind of redefining what that member actually does.

We also get acquainted with a large variety of proxies (communication proxies, logging proxies, virtual proxies, guarding proxies, and so on and so forth). And all of these proxies have completely different behaviors.

The goal of the proxy it to kind of change the behavior of an object behind the scenes but preserve the interface that everyone is used to.