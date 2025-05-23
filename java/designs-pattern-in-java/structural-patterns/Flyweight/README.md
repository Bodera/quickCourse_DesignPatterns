# Flyweight

> Space optimization!

The Flyweight design pattern is concerned with space optimization. One thing that we want to do if we're storing lots of data is to avoid any redundancy, just kind of like compression in images or films if you have the same block repeating over and over again you probably want to actually avoid having that block take up memory, so you write it once and say how many times it repeats so if you imagine an online game like some random massively multiplayer online RPG, you're going to have tons of users with identical first and last names, there is no point in actually storing the same first and last name combinations over and over again because you are simply __wasting memory__. What you do instead is go for storing a list of names somewhere else, and then you keep the pointers to those names and by pointers it means virtually anything, not necessarily physical memory pointers, you can keep indexes for example, if you're dealing with relational databases you can keep all the first and last names in one table, so then you can have an index as a number pointing you to the record on that table.

Another example, suppose you work processing texts and have some personal style for outputting those texts, you want to format the individual characters to print the words as bold or italic, they can go capitalized or not. Here you don't want to store additional information per character because typically what you want to do is not just format individual characters, but the whole words, sentences, or paragraphs. In that case you could introduce the idea of ranges where you define the start and final position on the line where you would apply the flyweight pattern to actually decorate this with some additional meaning.

Flyweight design pattern is quite simply a space optimization technique that allows you to use less memory by storing externally the data associated with similar objects. 

## Summary

The goal of the Flyweight design pattern is that whenever you have data repeating you'll be able to store the common data externally, and then you specify some index or reference into the external data store so that you can retrieve the data back. Here we looked at a specific examples dealing with the concept of _ranges_ on homogeneous collections and store data related to those _ranges_.

Also, we've looked at an example dealing with strings where we decomposed the names by splitting them. In case you deal with a similar scenario where decomposing data is not a requirement, check out the Java `String.intern()` API.