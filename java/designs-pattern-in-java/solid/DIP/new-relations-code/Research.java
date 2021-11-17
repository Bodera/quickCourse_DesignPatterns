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
