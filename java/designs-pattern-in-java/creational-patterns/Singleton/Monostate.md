# The Monostate 

There is a pernicious variation on the singleton design pattern called __The Monostate Design Pattern__, it's unintuitive and we're demonstrating how it works here just for the sake of knowledge.

Let's suppose you have a company that has a single CEO.

```java
class ChiefExecutiveOfficer
{
    private String name;
    private int age;

    //getters, setters and toString
}
```

Now the idea of __monostate implementation__ is the following: let's say that you want this class to suddenly become a __singleton__, all you have to do is to leave everything as it is which means it will seem like an instance class but, in counterpart, you make all the data storage as `static`.

```java
class ChiefExecutiveOfficer
{
    private static String name;
    private static int age;

    //getters, setters and toString
}
```

This leads to a very strange situation where people can go ahead and craft as many instances as they like, in the end, it will not matter because each one of those instances maps to the same static fields, they share common elements storage after all.

Guess the output for the following:

```java
public static void main(String[] args) {
    ChiefExecutiveOfficer ceo = new ChiefExecutiveOfficer("Evellyn", 31);
    ChiefExecutiveOfficer ceo_2 = new ChiefExecutiveOfficer();
    
    System.out.println(ceo);
    System.out.println(ceo_2);
}
```

It prints:

```txt
ChiefExecutiveOfficer [name=Evellyn, age=31]
ChiefExecutiveOfficer [name=Evellyn, age=31]
```

As you can see the object `ceo_2` has already been initialized even though we haven't added any kind of initialization code to it. This is why the __monostate pattern__ is mostly not the best approach because it becomes confusing when nothing is suggesting to our clients that they are working with a __singleton__.
