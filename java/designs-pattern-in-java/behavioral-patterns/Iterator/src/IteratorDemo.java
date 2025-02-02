import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class IteratorDemo {

    public static void main(String[] args)
    {
        // tree declaration
        Node<Integer> root = new Node<>(1,
                new Node<>(2),
                new Node<>(3));
        
        InOrderIterator<Integer> iterator = new InOrderIterator<>(root);

        while (iterator.hasNext())
            System.out.print("" + iterator.next() + ", ");

        System.out.println();

        // binary tree
        BinaryTree<Integer> tree = new BinaryTree<>(root);
        
        // traversal
        for (Integer i : tree)
            System.out.print("" + i + ", ");
        
        System.out.println();
    }
}

class Node<T>
{
    T value;
    Node<T> left, right, parent;

    public Node(T value)
    {
        this.value = value;
    }

    public Node(T value, Node<T> left, Node<T> right)
    {
        this.value = value;
        this.left = left;
        this.right = right;

        left.parent = right.parent = this;
    }
}

class InOrderIterator<T> implements Iterator<T>
{
    private Node<T> current, root;
    private boolean yieldedStart;

    public InOrderIterator(Node<T> root)
    {
        this.root = current = root;

        while (current.left != null)
            current = current.left;
    }

    private boolean hasRightmostParent(Node<T> node)
    {
        if (node.parent == null) return false;
        else {
            return (node.parent.left == node)
                    || hasRightmostParent(node.parent);
        }
    }

    @Override
    public boolean hasNext()
    {
        return current.left != null
                || current.right != null
                || hasRightmostParent(current);
    }

    @Override
    public T next()
    {
        if (!yieldedStart)
        {
            yieldedStart = true;
            return current.value;
        }

        if (current.right != null)
        {
            current = current.right;

            while (current.left != null)
                current = current.left;

            return current.value;
        }
        else {
            Node<T> parent = current.parent;

            while (parent != null && parent.right == current)
            {
                current = parent;
                parent = parent.parent;
            }

            current = parent;
            return current.value;
        }
    }
}

class BinaryTree<T> implements Iterable<T>
{
    private Node<T> root;

    public BinaryTree(Node<T> root)
    {
        this.root = root;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new InOrderIterator<>(root);
    }

    @Override
    public void forEach(Consumer<? super T> action)
    {
        for (T t : this)
            action.accept(t);
    }

    @Override
    public Spliterator<T> spliterator()
    {
        return null;
    }
}