# Why not use Cloneable

So before we get ourselves acquainted with the prototype design pattern we need to discuss an additional set of concepts which are related to it. Those concepts have to do with copying of course, that's what prototype is all about.

Let's jump to our example:

```java
class Address
{
    public String streetName;
    public int houseNumber;

    public Address(String streetName, int houseNumber)
    {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString()
    {
        return "Address{streetName = '" + streetName + "', houseNumber = " + houseNumber + '}';  
    }
}

class Person
{
    public String[] names;
    public Address address;

    public Person(String[] names, Address address)
    {
        this.names = names;
        this.address = address;
    }

    @Override
    public String toString()
    {
        return "Person{names = " + Arrays.toString(names) + ", address = " + address + '}';
    }
}

class Demo
{
    public static void main(String[] args)
    {
        Person person = new Person(new String[]{"Name", "Surname"}, new Address("Broadway Street", 123));
        System.out.println(person);
    }
}
```

Let's say we want to make another `Person` who lives next door to our first one. Setting this as a goal, do we really need  to recreate the large constructor call again? No, that's for sure not how we'll do it, we want to somehow make a copy of the `person` object and then modify only the relevant bits.

Now we're going to see that it's not simple to achieve the desired behavior using the equals operator.

```java
public static void main(String[] args)
{
    Person person = new Person(new String[]{"Name", "Surname"}, new Address("Broadway Street", 123));
    System.out.println(person);

    Person otherPerson = person;
    otherPerson.names[0] = "Other Name";
    otherPerson.address.houseNumber = 124;

    System.out.println(person);
    System.out.println(otherPerson);
}
```

What this piece of code do `Person otherPerson = person;` is basically copying the memory reference of an Object to another, as a result they both we will store always the exact same data. We actually overwrite the original values of our first `Person`.

What we really want to somehow take all the data inside `person` and make a copy into `otherPerson`. The most tempting thing would be for us to somehow start cloning the object. The JVM kind of hints that this is what you should be doing when copying objects by implementing the _Cloneable_ interface, it's a mark interface so it doesn't have any members therefore you're not required to do any implementation at all. By implementing it then you gave the hint that the object is in fact clonable, but beside the misspelling it's ill specified so we do not have any suggestions as to whether a deep copy or a shallow copy is actually taking place. 

```java
class Address implements Cloneable
{
    ...

    //shallow copy
    @Overrite
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
```

```java
class Address implements Cloneable
{
    ...

    //deep copy
    @Overrite
    public Object clone() throws CloneNotSupportedException
    {
        return new Address(streetName, houseNumber);
    }
}
```

```java
class Person implements Cloneable
{
    ...

    @Overrite
    public Object clone() throws CloneNotSupportedException
    {
        return new Person(names.clone(), (Address) address.clone());
    }
}
```

Typically implementation of _Cloneable_ it's not recommended, it doesn't state what the `clone()` method really does and the default behavior is to perform a shallow copy as opposed to a deep copy.
