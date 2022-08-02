import java.math.BigDecimal;

class Person 
{
    //address
    public String streetAddress, postCode, city;

    //employment
    public String companyName, position;
    public BigDecimal annualIncome;

    @Override
    public String toString() {
        return new StringBuilder()
        .append("Person{")
        .append("\n    streetAddress = '").append(streetAddress).append('\'')
        .append(", \n    postCode = '").append(postCode).append('\'')
        .append(", \n    city = '").append(city).append('\'')
        .append(", \n    companyName = '").append(companyName).append('\'')
        .append(", \n    position = '").append(position).append('\'')
        .append(", \n    annualIncome = ").append(annualIncome.toString())
        .append("\n}")
        .toString();
    }
}

//builder facade (builder pattern + facade pattern)
class PersonBuilder
{
    protected Person person = new Person();

    public PersonAddressBuilder lives() {
        return new PersonAddressBuilder(person);
    }

    public PersonJobBuilder works()
    {
        return new PersonJobBuilder(person);
    }

    public Person build() {
        return person;
    }
}

class PersonAddressBuilder extends PersonBuilder
{
    public PersonAddressBuilder(Person person) 
    {
        this.person = person;
    }

    public PersonAddressBuilder at(String streetAddress)
    {
        person.streetAddress = streetAddress;
        return this;
    }

    public PersonAddressBuilder withPostCode(String postCode)
    {
        person.postCode = postCode;
        return this;
    }

    public PersonAddressBuilder in(String city)
    {
        person.city = city;
        return this;
    }
}

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

    public PersonJobBuilder earning(double annualIncome)
    {
        person.annualIncome = BigDecimal.valueOf(annualIncome);
        return this;
    }
}

public class FacetedBuilder 
{
    public static void main(String[] args) {
        PersonBuilder pb = new PersonBuilder();
        Person person = pb
            .lives()
                .at("97 Fritz St.")
                .in("Nebraska")
                .withPostCode("A3C4B2")
            .works()
                .at("TechnoHippies")
                .asA("intern")
                .earning(9.99)
            .build();

        System.out.println(person);
    }
}


