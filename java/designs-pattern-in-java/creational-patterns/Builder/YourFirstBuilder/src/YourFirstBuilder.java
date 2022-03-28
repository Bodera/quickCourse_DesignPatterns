import static java.lang.String.format;
import static java.lang.String.join;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.Collections;

class HtmlElement
{
    public String name, text;
    public ArrayList<HtmlElement> elements = new ArrayList<>();
    private final int indentSize = 2;
    private final String newLine = System.lineSeparator();

    public HtmlElement() {
    }

    public HtmlElement(String name, String text) {
        this.name = name;
        this.text = text;
    }

    private String toStringImpl(int depth)
    {
        StringBuilder sb = new StringBuilder();
        String indent = join("", Collections.nCopies(depth * indentSize, " "));
        sb.append(format("%s<%s>%s", indent, name, newLine));
        
        if (null != text && !text.isEmpty())
        {
            sb.append(join("", Collections.nCopies(indentSize * (depth + 1), " ")))
                .append(text)
                .append(newLine);
        }
        
        elements.forEach(element -> sb.append(element.toStringImpl(depth + 1)));

        sb.append(format("%s</%s>%s", indent, name, newLine));
        return sb.toString();
    }

    @Override
    public String toString() {
        return toStringImpl(0);
    }
}

class HtmlBuilder
{
    private String rootName;
    private HtmlElement root = new HtmlElement();

    public HtmlBuilder(String rootName)
    {
        this.rootName = rootName;
        root.name = rootName;
    }

    public void addChild(String childName, String childText)
    {
        HtmlElement child = new HtmlElement(childName, childText);
        root.elements.add(child);
    }

    public void clear()
    {
        // keeps the type of root that it is but still resets the actual content.
        root = new HtmlElement();
        root.name = rootName; 
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
 
class YourFirstBuilder
{
    public static void main(String[] args)
    {
        HtmlBuilder builder = new HtmlBuilder("ul");
        builder.addChild("li", "hello");
        builder.addChild("li", "world");
        System.out.println(builder);
    }
}