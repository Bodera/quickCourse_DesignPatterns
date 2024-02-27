import com.google.common.collect.Iterables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.junit.jupiter.api.Test;

class SingletonDatabase
{
    private Dictionary<String, Integer> capitals = new Hashtable<>();
    private static int instanceCount = 0; //only for illustration purpose

    public static int getCount()
    {
        return instanceCount;
    }

    private SingletonDatabase()
    {
        ++instanceCount;
        System.out.println("Initializing database...");
        final String FILENAME = "/assets/Capitals.txt";

        try 
        {
            File f = new File(
                TestingIssuesWithSingleton.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
            );

            Path fullPath = Paths.get(f.getPath(), FILENAME);
            List<String> lines = Files.readAllLines(fullPath);

            Iterables.partition(lines, 2)
                .forEach(data -> capitals.put(
                    data.get(0).trim(),
                    Integer.parseInt(data.get(1))
                ));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static final SingletonDatabase INSTANCE = new SingletonDatabase();

    public static SingletonDatabase getInstance()
    {
        return INSTANCE;
    }

    public int getPopulation(String capital)
    {
        return capitals.get(capital);
    }
}

class SingletonRecordFinder
{
    public int getTotalPopulation(List<String> cityNames)
    {
        int result = cityNames
            .stream()
            .mapToInt(city -> SingletonDatabase
                .getInstance()
                .getPopulation(city))
            .sum();

        return result;
    }
}

class TestingIssuesWithSingleton
{
    @Test
    void getTotalPopulation()
    {
        SingletonRecordFinder rf = new SingletonRecordFinder();
        List<String> cities = List.of("Seoul", "Mexico City");
        int totalPop = rf.getTotalPopulation(cities);
        assertEquals(34900000, totalPop);
    }
}