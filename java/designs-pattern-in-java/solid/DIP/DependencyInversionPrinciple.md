# Dependency Inversion Principle
We reach the final design principle of SOLID, it doesn't really connect directly to the idea of dependency injection because we heard a lot about dependency injection but dependency inversion is something which is close but it's not the same thing and you shouldn't equate one with another.

This time around what we're going to do is learn the definition of dependency inversion because it's even less intuitive than some of the previous ones we looked at, we can split in two parts:

- High-level modules should not depend on low-level modules. Both should depend on abstractions.
- Abstractions should not depend on details. Details should depend on abstractions.

What does abstraction means here? Typically by abstraction we mean either an abstract class an interface, so by abstraction we mean that you get a signature of something which performs a particular operation but you don't necessarily work with the concrete implementation, instead you're working with an abstraction that's what an interface or abstract class is.

So the second item is fairily easy to catch the ideia, basically means that if you can use interfaces and abstract classes instead of concrete classes because by doing it you can substitute one implementation for another without breaking anything.

Now let's focus on first item which is far more important, to illustrate the conception of high and low level modules let's go straigth to some coding.

This demo consists in an application for modeling simple relationships between different people as some short of bare basic family tree.

```java
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

enum Relationship
{
    PARENT,
    CHILD,
    SIBLING
}

class Person
{
    public String name;

    public Person(String name) {
        this.name = name;
    }
}

class Relationships
{
    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

}
```

There we go, so far we've just setup the abstractions which our application will manipulate, the `Relationship` *enum* was created to store a set of common relationships between two people, to represent the entity which the relationship based on we've created a *class* `Person`, and in order to model the relationship between persons there is a *class* `Relationships`, that address two persons to one relationship.

Now we're going to add a method into `Relationships` to add a relationship between two persons as parent and chield:

```java 
class Relationships
{
    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

    public void addParentAndChild(Person parent, Person chield)
    {
        relations.add(new Triplet<>(parent, Relationship.PARENT, chield));
        relations.add(new Triplet<>(chied, Relationship.CHILD, parent));
    }

}
```

Let's evolve our application by creating adding another module called `Research` that will enable us to perform research in our list of relationships, notice that this call will depend on `Relationships`. and all the research will be made directly on the constructor method that way we won't have any additinal methods. 

So to perform researches on relationships we need to take `Relationships` module as a constructor argument to retrieve the data from it. And this relates to the Dependency Inversion Principle which states that high level modules should not depend on low level modules, we'll discuss it more soon but, for now, let's just say that we want to performn this kind of research.

The problem is that the List of relationships in `Relationships` has private access, so we need to build the getter method for accessing it.

```java
class Relationships
{
    //...
    public List<Triplet<Person, Relationship, Person>> getRelations() {
        return relations;
    }
    //...
}
```

And now we start coding at our `Research` class.

```java
class Research
{
    public Research(Relationships relationships)
    {
        List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();

        relations.stream()
            .filter(x -> x.getValue0().name.equals("John") && x.getValue1() == Relationship.PARENT)
            .forEach(ch -> System.out.println(
                "Jhon has a child called " + ch.getValue2().name
            ));
    }
}
```

Building class for demonstration.

```java
class Demo {

    public static void main(String[] args) {
        Person parent = new Person("John");
        Person chield1 = new Person("Chris");
        Person chield2 = new Person("Matt");

        Relationships relationships = new Relationships();
        relationships.addParentAndChield(parent, chield1);
        relationships.addParentAndChield(parent, chield2);
    
        new Research(relationships);
    }

}
```

Let's check what we got so far.

```bash
$ export CLASSPATH=$CLASSPATH:/home/<ordinary-user>/<ordinary-path>/libs/javatuples-1.2.jar 

$ javac Research.java

$ java Demo
```

The output is exactly what we expected, everything seems to be working and you may wondering what is the problem whit this code. Well in actual fact there is a fairly major problem we are exposing an internal storage implementation of `Relationships` as a public getter for everyone to access.

On this example `Relationships` is a low-level module. It's low-level because it's related to data storage, it simply provides a list or it keeps a list and it gives us some sort of access to that list, so it's a low-level implementation that doesn't have any business logic on it's own and it simply allows you to manipulate the list, that's also a Single Responsability, which is to allow manipulations of the list that we have here.

Now on other hand `Research` is a high-level module because it allows us to perform some sort of operations on those low level constructs. And so it it a kind of "higher to the end user" so to speak, because end users doesn't really care about the storage implementation they care about the actual research, they want results, they want to know where is John and who John's children are.

Remember the Dependency Inversion Principle states that high-level modules should not depend on low-level modules but unfortunately that's exactly what we have in this constructor it takes a low-level module as an argument, depending on it, and that's really bad because it's violating the Dependency Inversion Principle.

Now the question that may be hanging in your head is "How do we fix it?", remember the second hint of the dependency inversion principle was that you should depend on abstractions instead. So how would we depend on abstractions? Let's introduce an abstraction first.

Our abstraction will be an interface called `RelationshipBrowser` which will have just a single method which is going to find all the children of a particular person, that means, it will return a list of persons based on a particular name. It returns all chidren of that person assuming they have any otherwise it will retun an empty list.

```java
interface RelationshipBrowser 
{    
    List<Person> findAllChildrenOf(String name);
}
```

That's the abstraction that you would be expected to implement on the low-level module for example. See that now `Relationships` implements `RelationshipsBrowser` and this is precisely the location where you'd actually be expected to implement the method and perform the search so the search doesn't happen in the higher level module it, the search for the kind of core functionality is happening in a low-level module.

Why do it like this? Suppose you decide to change the implementation from a list to something else. With direct dependency on a low-level module you cannot do this because, remember, we're getting relations as a list on method `getRelationships`. So if you change it from a list to something else like an array for example you're in trouble because you're going to have to rewrite lots and lots of code, you aren't also be able to do stream and will have to do it differently. In short this is a problem.

So instead we're doing everything in a low-level module, this is where you would actually go about finding all the children of a particular person so you would actually go ahead and perform the search. You can see that we have this very similar idea of just finding all the children of a particular person's name and returning that.

```java
class Relationships implements RelationshipBrowser
{
    //...
    @Override
    public List<Person> findAllChildrenOf(String name) {
        return relations.stream()
            .filter(x -> Objects.equals(x.getValue0().name, name
                && x.getValue1() == Relationship.PARENT))
            .map(Triplet::getValue2)
            .collect(Collectors.toList());
    }
    //...
} 
```

Now the upside is that back in the research module we no longer have to break the Dependency Inversion Principle because instead of depending on a low-level module directly we're going to depend on an abstraction we're going to have a `Research` which is going to depend on a `RelationshipBrowser`, that's the interface we've defined.

```java
class Research
{
    //...
    public Research(RelationshipBrowser browser)
    {
        List<Person> children = browser.findAllChildrenOf("John");
        children.forEach(child -> System.out.println("John has a child called: " + child.name));
    }
    //...
}
```

And that's pretty much it, notice how we have simplified the code somewhat and we've also added flexibility because now if you want to change your code, if you want to stop using a list of `Triplets` because maybe you may think it's clumsy since getting data from unnamed tables is particulary ugly you may want to change to something else. Maybe you want it strongly typed, maybe you want each of the `<Person, Relationship, Person>` constructors to appear as part of a separate class that you define. Now you can just do it, previously when we were violating the Dependency Inversion Principle you couldn't even do it because you were so strongly dependent on that implementation detail in your high-level modules and you shouldn't depend on implementations details, you should depend on abstractions like the browser here.

The interesting thing about this set up is if we go back into our `main()` method we don't really have to change anything. What I want you to realize is that we can take this implementation of the old `Research` constructor and remove it so we can start using the new one instead and there's not a single line modified inside our `main()` method, everything continues to work as before. 
