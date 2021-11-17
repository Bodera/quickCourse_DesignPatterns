import java.util.List;
import java.util.stream.Stream;

enum Color
{
    RED, GREEN, BLUE
}

enum Size
{
    SMALL, MEDIUM, LARGE, HUGE
}

class Product
{
    public String name;
    public Color color;
    public Size size;

    public Product(String name, Color color, Size size){
        this.name = name;
        this.color = color;
        this.size = size;
    }
}

interface Specification<T>
{
    boolean isSatisfied(T item);
}

interface Filter<T>
{
    Stream<T> filter(List<T> items, Specification<T> spec);
}

class ColorSpecification implements Specification<Product>
{
    private Color color;

    public ColorSpecification(Color color) {
        this.color = color;
    }

    @Override
    public boolean isSatisfied(Product item) {
        return item.color == color;
    }
}

class SizeSpecification implements Specification<Product>
{
    private Size size;

    public SizeSpecification(Size size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfied(Product p) {
        return p.size == size;
    }
}

class BetterProductFilter implements Filter<Product>
{
    @Override
    public Stream<Product> filter(List<Product> items, Specification<Product> spec) {
        return items.stream().filter(p -> spec.isSatisfied(p));
    }
}

class AddSpecification<T> implements Specification<T>
{
    private Specification<T> first, second;

    public AddSpecification(Specification<T> first, Specification<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isSatisfied(T item) {
        return first.isSatisfied(item) && second.isSatisfied(item);
    }
}

class Demo
{
    public static void main(String[] args) {
        Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
        Product house = new Product("House", Color.BLUE, Size.LARGE);

        List<Product> products = List.of(apple, tree, house);

        BetterProductFilter bpf = new BetterProductFilter();
        System.out.println("Green products (new):");
        bpf.filter(products, new ColorSpecification(Color.GREEN))
                .forEach(p -> System.out.println(
                        " - " + p.name + " is green"));

        System.out.println("Large blue items:");
        bpf.filter(products, 
            new AddSpecification<>(
                new ColorSpecification(Color.BLUE),
                new SizeSpecification(Size.LARGE)
            ))
            .forEach(p -> System.out.println(
                " - " + p.name + " is large and blue"));
    }
}