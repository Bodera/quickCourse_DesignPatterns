import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class SerializationProblems
{
    static void saveToFile(BasicSingleton singleton,
                            String fileName) throws Exception
    {
        //performing serialization
        try (FileOutputStream fileOutput = new FileOutputStream(fileName);
                ObjectOutputStream objOut = new ObjectOutputStream(fileOutput)) 
        {
            objOut.writeObject(singleton);
        }
    }

    static BasicSingleton readFromFile(String fileName) throws Exception
    {
        //performing deserialization
        try (FileInputStream fileInput = new FileInputStream(fileName);
                ObjectInputStream objInp = new ObjectInputStream(fileInput))
        {
            return (BasicSingleton) objInp.readObject();
        }
    }

    public static void main(String[] args) throws Exception
    {
        BasicSingleton carrier = BasicSingleton.getInstance();
        carrier.setSecret(12345);

        String testSerializationFileName = "carrier.bin";
        saveToFile(carrier, testSerializationFileName);

        carrier.setSecret(54321);
        
        BasicSingleton newCarrier = readFromFile(testSerializationFileName);

        System.out.println(carrier == newCarrier);

        System.out.println(carrier.getSecret());
        System.out.println(newCarrier.getSecret());
    }
}

class BasicSingleton implements Serializable
{
    private BasicSingleton()
    {
    }

    private static final BasicSingleton INSTANCE = new BasicSingleton();
    private int secret = 100;

    public static BasicSingleton getInstance()
    {
        return INSTANCE;
    }

    public int getSecret()
    {
        return secret;
    }

    public void setSecret(int secret)
    {
        this.secret = secret;
    }

    protected Object readResolve()
    {
        return INSTANCE;
    }
}
