import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

interface RelationshipBrowser 
{    
    List<Person> findAllChildrenOf(String name);
}

class Relationships implements RelationshipBrowser
{
    //2022-12-06
    //the use of Triplets is for illustration purpose and tuples should be handled carefully in Java
    //we could reach similar effect of this external library using native API like Map<Person, Entry<Relationship, Person>>, but again, such data structure most be used wisely
    //thoughs I came trhough after reading: http://mail.openjdk.java.net/pipermail/core-libs-dev/2010-March/003973.html

    //low-level & Single Responsability (retrieve data)
    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

    public void addParentAndChild(Person parent, Person child)
    {
        relations.add(new Triplet<>(parent, Relationship.PARENT, child));
        relations.add(new Triplet<>(child, Relationship.CHILD, parent));
    }

    @Override
    public List<Person> findAllChildrenOf(String name) {
        return relations.stream()
            .filter(x -> x.getValue0().name == name && x.getValue1() == Relationship.PARENT)
            .map(Triplet::getValue2)
            .collect(Collectors.toList());
    }
}

class Research 
{
    //high-level (business rules)
    public Research(RelationshipBrowser browser)
    {
        browser.findAllChildrenOf("John").forEach(child -> System.out.println("John has a child called: " + child.name));
    }
}

class Demo {

    public static void main(String[] args) {
        Person parent = new Person("John");
        Person child1 = new Person("Chris");
        Person child2 = new Person("Matt");

        Relationships relationships = new Relationships();
        relationships.addParentAndChild(parent, child1);
        relationships.addParentAndChild(parent, child2);
    
        new Research(relationships);
    }
}
