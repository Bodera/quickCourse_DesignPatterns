# Faceted Builder

In the previous lessons we looked at scenarios the way you typically had just a single builder for building up a particular object but that's not always the case. Sometimes your object, the object you're building up, is so complicated that a single builder it even isn't sufficient and you need more than one builder, so in this demo we're going to take a look at how you can use multiple builders working in tandem to provide functionality for actually building up a particular object.

Let's create a new classe `Person`. This `Person` has certain address information and certain employment information, suppose that we want to build different aspects of `Person` using different builders.

```java
class Person
{
    //adress
    private String streetAddress, postCode, city;

    //employment
    private String companyName, position;
    private int annualIncome;

    @Override
    public String toString() {
        return new StringBuilder()
        .append("Person{")
        .append("streetAddress='").append(streetAddress).append('\'')
        .append(", postCode='").append(postCode).append('\'')
        .append(", city='").append(city).append('\'')
        .append(", companyName='").append(companyName).append('\'')
        .append(", position='").append(position).append('\'')
        .append(", annualIncome=").append(annualIncome)
        .append("}").toString();
    }
}
```

See, that's pretty much all we have inside a `Person`, now what we want is different builders for the address part and for the employment part. By doing so, we're going to build something called a build facade which is a combination of the builder pattern as well as the facade design pattern.

So the first thing we're going to define is a class called `PersonBuilder`, since we're building up a `Person` it makes sense to start with a person builder. A `PersonBuilder` is going to store the `Person` or object that we're going to build up.

```java
//builder facade
class PersonBuilder
{
    protected Person person = new Person();
}
```

Of course we need to expose this `Person` so we're going to have some sort of `build()` method for actually returning the object that we've actually built.

```java
public Person build()
{
    return person;
}
```

Notice the **public** access modifier to `build()`, the object `person` is protected so you don't have direct access to it but you can be given the object indicating that you are done building it up essentially. Now we're going to do something interesting, we're going to have two methods called `works()` and `lives()` but they are going to return brand new builders, so in addition to `PersonBuilder` we're going to have even more builders in here.

So let's start with the `PersonAddressBuilder`. It's a dedicated builder for building up the address information on a `Person` and is going to extend the `PersonBuilder`. Notice this is critical to having faceted builders.

```java
class PersonAddressBuilder extends PersonBuilder
{

}
```

So now we're going to add a constructor wich takes essentially a reference to the object that's being built up and simply reassign that reference.

```java
public PersonAddressBuilder(Person person)
{
    this.person = person;
}
```

For all cases there is only a sigle `Person` that we're actually building up, we're not making copies of it or anything but we do need a reference to this person inside every sub-builder.

Now we are set to provide a fluent interface for building up the address information.

```java
// sets the street addres of Person
// return the same type in order to keep it fluent
public PersonAddressBuilder at(String streetAddress)
{
    person.streetAddress = streetAddress;
    return this;
}

// sets the post code of Person
// return the same type in order to keep it fluent
public PersonAddressBuilder withPostCode(String postCode)
{
    person.postCode = postCode;
    return this;
}

// sets the city of Person
// return the same type in order to keep it fluent
public PersonAddressBuilder in(String city)
{
    person.city = city;
    return this;
}
```

Now that we basically defined all resources to our `PersonAddressBuilder` we can jump back into the `PersonBuilder` and add a method called `lives()` which returns a new `PersonAddressBuilder` passing the reference of the object that's being built up.

```java
class PersonBuilder
{
    protected Person person = new Person();

    public PersonAddressBuilder lives()
    {
        return new PersonAddressBuilder(person);
    }

    public Person build()
    {
        return person;
    }

}
```

That's how you would implement the `PersonAddressBuilder` and expose it through `PersonBuilder`, and the same will be done for the `PersonJobBuilder`.


```java
class PersonJobBuilder extends PersonBuilder
{
    public PersonJobBuilder(Person person)
    {
        this.person = person;
    }

    public PersonJobBuilder at(String companyName)
    {
        person.companyName = companyName;
        return this;
    }

    public PersonJobBuilder asA(String position)
    {
        person.position = position;
        return this;
    }

    public PersonJobBuilder earning(int annualIncome)
    {
        person.annualIncome = annualIncome;
        return this;
    }
}
```

Now we are able to expose `PersonJobBuilder` inside `PersonBuilder`.

```java
class PersonBuilder
{
    protected Person person = new Person();

    public PersonAddressBuilder lives()
    {
        return new PersonAddressBuilder(person);
    }

    public PersonJobBuilder works()
    {
        return new PersonJobBuilder(person);
    }

    public Person build()
    {
        return person;
    }

}
```

Now in case if you're wondering why did we have to inherit from `PersonBuilder`, why do the sub-builders have to inherit from the base class builder, the answer is that as soon as they inherit from it they both expose both, the `works()` and `lives()` methods which means that you can switch from one sub-builder to another sub-builder inside a single fluent API call.

Now to appreciate how this all looks, let's perform some demo.

```java
class Demo
{
    public static void main(String[] args)
    {
        PersonBuilder pb = new PersonBuilder();
        Person person = pb
            .lives()
                .at("97 Fritz St.")
                .in("Nebraska")
                .withPostCode("A3C4B2")
            .works()
                .at("TechnoHippies")
                .asA("intern")
                .earning("9.99")
            // after configuring the object we can now build it.
            .build();

        System.out.println(person);
    }
}
```

The takeway from this example is that if you have an object that's so complicated that needs several builders what you can do is give them a common interface or a common class (which is our case using `PersonBuilder` class) that actually exposes the different sub-builders through it's methods. It's very convenient to have these as part of the interface and certainly the fact it is a fluent interface allows you to jump from one sub-builder to another with a single call, pressing a single dot, to the appropriate method.

In fact it is rather neat trick to allow you to sort of build up complicated objects through more than one builder.
