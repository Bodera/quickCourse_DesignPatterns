# Strategy

The strategy design pattern allows you to partially specify the behavior of the system and then augment it later on at runtime. That may sound a bit cryptic, let's explore it further.

Many algorithms can be decomposed into higher and lower level parts. Let's take the 'making a tea' task for example, it can be decomposed into the following:

- Making a hot beverage (higher level) - *boil water*, *pour into cup* 
- Tea-specific things (lower level) - *add lemon*, *add tea-leaves*

The high-level algorithm can then be reused for making coffee or hot chocolate, supported by beverage-specific strategies.

That's the idea behind the strategy design pattern, specific things are expected to be specific but everything else is expected to be reusable. It essentially enables the exact behavior of a system to be selected either at runtime (dynamic) or at compile time (static).

This pattern is also known as *policy* in other programming languages including C++.

## Summary

When using the strategy design pattern our goal is:

- First define an algorithm at a high level
- Then define the interface we expect each strategy to follow
- Finally, provide for either dynamic or static composition of strategies in the overall algorithm

