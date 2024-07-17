# Copy Constructors

Alright, so what we're going to do now is to implement an `Address` class and some other class that needs to be copyable, although we're not using `Cloneable`, we're going to use copy constructors.

Let's start our code by defining the `Address` class:

```java
class Address
{
    public String streetAddress, city, country;

    public Address(String streetAddress, String city, String country) 
    {
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() 
    {
        return "Address{" +
        "streetAddress='" + streetAddress + '\'' +
        ", city='" + city + '\'' +
        ", country='" + country + '\'' +
        '}';
    }
}
```

So what is a copy constructor and how they behave? Well in simple terms it's a constructor that takes another copy of some particular object as an argument and then tries to copy each one of the declared fields on it. Let's see it action:

```java
class Address
{
    //...
    public Address(Address other) {
        this(other.streetAddress, other.city, other.country);
    }
}
```

And that's all. Let's increase our example by adding another class which declares an `Address`, like an `Employee` class.

```java
class Employee
{
    public String name;
    public Address address;

    public Employee(String name, Address address) 
    {
        this.name = name;
        this.address = address;
    }

    public Employee(Employee other)
    {
        name = other.name;
        address = new Address(other.address);
    }

    @Override
    public String toString() 
    {
        return "Employee{" +
        "name='" + name + '\'' +
        ", address='" + address + '\'' +
        '}';
    }
}
```

Let's see it all working together in our `Demo` class.

```java
public class Demo
{
    public static void main(String[] args)
    {
        Employee steven = new Employee("Steven", new Address("123 London Road", "London", "UK"));

        Employee chris = new Employee(steven);
        chris.name = "Chris";
        chris.address = new Address("Rosenstrasse 41", "München", "DE");

        System.out.println(steven);
        System.out.println(chris);
    }
}
```

So the takeaway from this example is that one of the more palatable ways of explicitly specifying that an object is copyable is to provide a copy constructor, this is way better than implementing the `Cloneable` interface for example.