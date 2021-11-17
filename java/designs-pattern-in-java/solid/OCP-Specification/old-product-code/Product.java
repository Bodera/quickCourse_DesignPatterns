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

class ProductFilter
{
    public Stream<Product> filterByColor(List<Product> products,
                                         Color color)
    {
        return products.stream().filter(p -> p.color == color);
    }

    public Stream<Product> filterBySize(List<Product> products,
                                         Size size)
    {
        return products.stream().filter(p -> p.size == size);
    }

    public Stream<Product> filterBySizeAndColor(List<Product> products,
                                         Size size,
                                         Color color)
    {
        return products.stream().filter(
            p -> p.size == size
            && p.color == color
        );
    }
}

class Demo
{
    public static void main(String[] args) {
        Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
        Product house = new Product("House", Color.BLUE, Size.LARGE);

        List<Product> products = List.of(apple, tree, house);

        ProductFilter pf = new ProductFilter();
        System.out.println("Green products (old):");
        pf.filterByColor(products, Color.GREEN)
                    .forEach(p -> System.out.println(
                            " - " + p.name + " is green"));
    }
}