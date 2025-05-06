# Interpreter

> Interpreters are all around us. Even now, in this very room.

The interpreter design pattern is everywhere, if you're a programmer you see the interpreter design pattern almost every day.

This design pattern is actually a reflection of an entire field of computer science called compiler theory, and we could spend another 10 hours discussing the ins and outs of building compilers and parses and so on and so forth, but we're going to keep it simple.

The general idea of interpretation is that you have some textual input which needs to be processed, and by processed I typically mean that this input needs to be turned into objected-oriented structures. If this repo was about *"Design Patterns in C"* we wouldn't be talking about OOP structures, we would rather talk about ordinary C like structures, since we're talking about Java our context relates to taking textual input and turning it into something that's object-oriented.

Some sort of things that you would have to build an interpreter for:

- Programming languages compilers, interpreters and IDEs
- HTML, XML and similar
- Numeric expressions (mathematical expressions)
- Regular expressions

Generally, turning strings into OOP structures is a fairly complicated process and this is why this design pattern exist because this is a separate concern, a separate problem within computer science.

The interpreter is a component that process structured text data. Does so by turning it into separate lexical tokens - *lexing* - and then interpreting sequences of said tokens - *parsing*.

## Summary

Except for the absolute simplest cases, an interpreter acts in two stages.

1. The lexing process

    Takes a chunk of text and turns it into a set of tokens.

2. The parsing process

    Takes the sequence of tokens and turn them into meaningful constructs. It ends up building what's called an abstract syntax tree.

Also the parsed data can then be traversed, interpreted, transformed, and so on.
