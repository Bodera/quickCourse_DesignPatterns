import java.io.File;
import java.io.IOException;

class StaticBlockSingleton 
{
    private StaticBlockSingleton() throws IOException
    {
        System.out.println("Singleton is initializing.");
        File.createTempFile(".", ".");
    }

    private static StaticBlockSingleton instance; //no longer final

    static 
    {
        try 
        {
            instance = new StaticBlockSingleton();
            System.out.println("Singleton is now initialized.");
        } 
        catch(Exception e)
        {
            //handle exception here.
            System.err.println("Failed to initialize Singleton.");
        }
    }

    public static StaticBlockSingleton getInstance()
    {
        return instance;
    }
}