# Command

Essentially ordinary Java statements are perishable, which means that you can't just go ahead and undo a field assignment, you just did it because nobody bothered to save the previous value. So the very idea of undoing something like a field assignment is pretty much impossible. We can't directly serialize a sequence of actions like a series of field assignments, or a series of method calls. There is no way to serialize that structure into a chunk of memory that you can subsequently roll back.

What you want is really some sort of object-oriented representation of an *operation*. So for example we use an object to represent the idea that `X` should change its field `Y` till the value `Z`, or use an object to represent the idea that `X` should now call its member method `w()`.

Now you might be wondering where is this approach used and in actual fact it's used in a lot of places, the most common use is commands being sent from the graphic user interface. For example, you might have a top level menu and a toolbar which both allow you to save a file, so in both cases the file save operation will be wrapped in a command that will be sent to some sort of command processor.

One of the things that commands allow you to do is they allow you to have multi-level undo and redo functionality because essentially you're recording every single step with a command and those commands can be stored somewhere, and they can be played back in the reverse order.

And of course another functionality that's supported by commands is the idea of macros, that's where you record the sequence of steps that you make in the program, and you play back that sequence later on. There are lots of other use cases of commands as well.

So the Command design pattern generally it's a single object or combination of objects which represent an instruction that need to be performed. A command typically contains all the information for a particular action to be taken.

## Summary

The idea of this pattern is that you take an operation, encapsulate all of its details into a separate object. And the benefit of doing so is gaining the possibility of serializing it, store it somewhere, easier management, and also roll this operation back.

Then you define the instruction for actually apply the command. So having just the command isn't enough, you need some sort of command processor which is able to take this command and apply it to whatever is the subject of the command. Optionally you can define additional instructions, so in addition to applying the command you can also define instructions for rolling back or undoing the command.

And of course you can have all sorts of funds with commands, for example you can create composite commands which would typically be called macros in everyday speak, so this is where you have a set of commands and you kind of package them into a single command that can be invoked, and similarly can be rolled back as well.
