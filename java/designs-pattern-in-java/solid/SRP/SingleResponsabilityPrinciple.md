# Single responsibility Principle
In short, it states that a *class* should have just one reason to change. So that class must have one purpose instead to accumulate heavy load of responsabilities (god-object anti-pattern).

Code time:

```java
import java.util.ArrayList;
import java.util.List;

class Journal
{
    private final List<String> entries = new ArrayList<>();
    private static int count = 0;


    /**
    * Adds some entry to list of entries and prefix it with
    * the total number of entries then increment it.
    *
    * @param text 
    */
    public void addEntry(String text)
    {
        entries.add("" + (++count) + ": " + text);
    }

    /**
    * Remove an element at given index from the list of entries.
    *
    * @param index 
    */
    public void removeEntry(int index)
    {
        entries.remove(index);
    }

    /**
    * Retrieves the collection of entries in a String delimited 
    * by the operating system default line separator.
    * 
    */
    @Override
    public String toString() {
        return String.join(System.getProperty("line.separator"), entries);
    }
}

class Demo
{
    public static void main(String[] args) {
        Journal j = new Journal();
        j.addEntry("I cried today");
        j.addEntry("I ate a bug");
        System.out.println(j);
    }
}
```

Compile it and run with:

```bash
$ javac Journal.java
$ java Demo
```

__Variables dictionary:__
* Journal - a class to store the most intimate thoughs.
* entries - to keep the journal, iniatializes a container for the journal entries.
* count - to index each of those entries across however many journals that has been created.

## The responsibility of the Journal class
* Allow some level of access to the entries.
* Add entries and remove as well.

Suppose we get tempted to add an extra responsibility to Journal class, something that doesn't really belong to Journal itself, may be a good idea to persists the Journal in a file.

Like this:

```java
import java.io.PrintStream;

public void save(String filename) throws FileNotFoundException
{
    try (PrintStream out = new PrintStream(filename))
    {
        out.println(toString());
    }
}

// main method in class Demo now must throw Exception
```

It handles pretty well. What about enabling to load content of a Journal?

Then we have:

```java
public void load(String filename) {}

public void load(URL filename) {}
```

These are all examples of violation of the Single responsibility Principle. Why? They got the concern of entries too far. Initially we were handling the __adding and removal of entries__ and now we're also handling __persistence__.

Think of persistence as a separate responsibility, a popular term in the world of OOP and software design to facilitate your research is the separation of concerns.

Our approache now will be to move the responsibility inherent of the methods *save()* and *load()* to a separate class so as to preserve the adherence to SRP.

So if persistence now matters, then we create a Persistence class:

```java
class Persistence
{
    // all persistence functionality

    public void saveToFile(Journal journal,
                            String filename,
                            boolean overwrite) throws FileNotFoundException
    {
        if (overwrite || new File(filename).exists())
        {
            try (PrintStream out = new PrintStream(filename))
            {
                out.println(toString());
            }
        }
    }

    public Journal loadFromFilename(String filename) {}
    public Journal loadFromURL(URL url) {}
}
```

This is not just for the Journal class, but any additional classes you might have in your application and that becomes a lot easier to manage later on when, let's say, you decide to change the mechanism by which objects are serialized to file.

Actually the manipulation of a Journal at the moment it is loaded, and the location to retrieve from, are reasons to create another class as it is a different concern.

This principle is great at forcing you to put just one responsability into any single class, mading refactor process easier. Changing in mass is pointless if you have ways to made a single pont of change.
