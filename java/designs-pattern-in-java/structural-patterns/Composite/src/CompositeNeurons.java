import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

class Neuron implements SomeNeurons
{
    public ArrayList<Neuron> in = new ArrayList<>();
    public ArrayList<Neuron> out = new ArrayList<>();

    @Override
    public Iterator<Neuron> iterator()
    {
        return Collections.singleton(this).iterator();
    }

    @Override
    public Spliterator<Neuron> spliterator()
    {
        return Collections.singleton(this).spliterator();
    }

    @Override
    public void forEach(Consumer<? super Neuron> action)
    {
        action.accept(this);
    }
}

class NeuronLayer 
    extends ArrayList<Neuron> 
    implements SomeNeurons
{
    
}

interface SomeNeurons extends Iterable<Neuron>
{

    default void connectTo(SomeNeurons other)
    {
        if (this == other) return;

        for (Neuron from : this)
        {
            for (Neuron to : other)
            {
                from.out.add(to);
                to.in.add(from);
            }
        }
    }
}

public class CompositeNeurons
{
    public static void main(String[] args)
    {
        Neuron n2 = new Neuron();
        Neuron n1 = new Neuron();
        NeuronLayer layer2 = new NeuronLayer();
        NeuronLayer layer1 = new NeuronLayer();

        n1.connectTo(n2);
        n1.connectTo(layer1);
        layer1.connectTo(n1);
        layer1.connectTo(layer2);

        System.out.println("n1 has: " + n1.out.size());
        System.out.println("layer2 has: " + layer2.size());
    }
}