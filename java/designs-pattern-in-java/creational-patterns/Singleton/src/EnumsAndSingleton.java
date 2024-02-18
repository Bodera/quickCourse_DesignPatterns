import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class EnumsAndSingleton
{
    class Demo 
    {
        static void saveToFile(EnumBasedSingleton singleton,
                               String fileName) throws Exception
        {
            try (FileOutputStream fileOutput = new FileOutputStream(fileName);
                    ObjectOutputStream objOut = new ObjectOutputStream(fileOutput)) 
            {
                objOut.writeObject(singleton);
            }
        }

        static EnumBasedSingleton readFromFile(String fileName) throws Exception
        {
            try (FileInputStream fileInput = new FileInputStream(fileName);
                    ObjectInputStream objInp = new ObjectInputStream(fileInput))
            {
                return (EnumBasedSingleton) objInp.readObject();
            }
        }
        
        public static void main(String[] args) throws Exception
        {
            String filename = "myfile.bin";

            //after serializing it to file once, try commenting the 3 lines below and check the output
            EnumBasedSingleton singleton = EnumBasedSingleton.INSTANCE;
            singleton.setValue(123);
            saveToFile(singleton, filename);

            EnumBasedSingleton otherSingleton = readFromFile(filename);

            System.out.println(singleton.getValue());
            System.out.println(otherSingleton.getValue());
        }
    }
}

enum EnumBasedSingleton
{
    INSTANCE;

    private int value;

    EnumBasedSingleton()
    {
        value = 42;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}