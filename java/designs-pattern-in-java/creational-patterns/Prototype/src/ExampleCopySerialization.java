import java.io.Serializable;
import org.apache.commons.lang3.SerializationUtils;

class Foo implements Serializable
{
    public int number;
    public String text;

    public Foo(int number, String text)
    {
        this.number = number;
        this.text = text;
    }

    @Override
    public String toString()
    {
        return "Foo{" +
        "number=" + number +
        ", text='" + text + '\'' +
        '}';
    }
}

public class ExampleCopySerialization
{
    public static void main(String[] args)
    {
        Foo original = new Foo(42, "life");
        Foo copy = SerializationUtils.roundtrip(original);

        copy.number = 23;
        copy.text = "laugh";
        System.out.println(original);
        System.out.println(copy);
    }
}