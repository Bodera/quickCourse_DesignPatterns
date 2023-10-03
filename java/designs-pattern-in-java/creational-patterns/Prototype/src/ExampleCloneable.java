import java.util.Arrays;

class Address implements Cloneable
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

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new Address(streetName, houseNumber);
    }
}

class Person implements Cloneable
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

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return new Person(names.clone(), (Address) address.clone());
    }
}

class ExampleCloneable
{
    public static void main(String[] args)
    {
        try {
            Person person = new Person(new String[]{"Name", "Surname"}, new Address("Broadway Street", 123));
    
            Person otherPerson =  (Person) person.clone();
            otherPerson.names[0] = "Other Name";
            otherPerson.address.houseNumber = 124;
    
            System.out.println(person);
            System.out.println(otherPerson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}