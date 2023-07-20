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
