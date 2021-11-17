import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
    * Retrieves the collection of entries in a String separated 
    * by the operating system default line separator.
    * 
    */
    @Override
    public String toString() {
        return String.join(System.getProperty("line.separator"), entries);
    }
}

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
                out.println(journal.toString());
            }
        }
    }

//        public Journal loadFromFilename(String filename) {}
//        public Journal loadFromURL(URL url) {}
}

class Demo
{
    public static void main(String[] args) throws Exception {
        Journal j = new Journal();
        j.addEntry("I cried today");
        j.addEntry("I ate a bug");
        System.out.println(j);

        Persistence p = new Persistence();
        String filename = "//.//home//rafael-brito//JavaCode//designPatterns//journal.txt";
        p.saveToFile(j, filename, true);
    }

    Runtime.getRuntime().exec("nano " + filename);
}