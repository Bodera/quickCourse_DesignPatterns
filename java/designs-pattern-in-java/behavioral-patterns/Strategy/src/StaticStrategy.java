import java.util.List;
import java.util.function.Supplier;

enum OutputFormat {
    MARKDOWN, HTML
}

interface ListStrategy 
{
    default void start(StringBuilder sb) {}
    void addListItem(StringBuilder sb, String item);
    default void end(StringBuilder sb) {}
}

class MarkdownListStrategy implements ListStrategy 
{
    /**
     * Adds a list item to the StringBuilder in Markdown format.
     * <p>
     * This method appends a Markdown list item, prefixed by " - ", 
     * followed by the item and a newline character.
     *
     * @param sb the StringBuilder to be modified
     * @param item the item to be added
     */
    @Override
    public void addListItem(StringBuilder sb, String item) {
        sb.append(" - ").append(item)
            .append(System.lineSeparator());
    }
}

class HTMLListStrategy implements ListStrategy 
{
    /**
     * Starts a new list with the given StringBuilder.
     * <p>
     * This method adds the HTML code to start a new list, i.e., &lt;ul&gt;.
     *
     * @param sb the StringBuilder to be modified
     */
    @Override
    public void start(StringBuilder sb) {
        sb.append("<ul>").append(System.lineSeparator());
    }

    /**
     * Adds a list item to the StringBuilder.
     * <p>
     * This method adds the HTML code to add a new list item, i.e., &lt;li&gt;
     * followed by the item and &lt;/li&gt;.
     *
     * @param sb the StringBuilder to be modified
     * @param item the item to be added
     */
    @Override
    public void addListItem(StringBuilder sb, String item) {
        sb.append("  <li>")
            .append(item)
            .append("</li>")
            .append(System.lineSeparator());
    }

    /**
     * Ends a list with the given StringBuilder.
     * <p>
     * This method adds the HTML code to end a list, i.e., &lt;/ul&gt;.
     *
     * @param sb the StringBuilder to be modified
     */
    @Override
    public void end(StringBuilder sb) {
        sb.append("</ul>").append(System.lineSeparator());
    }
}

class StaticTextProcessor<LS extends ListStrategy> 
{
    private StringBuilder sb = new StringBuilder();
    private LS listStrategy;

    public StaticTextProcessor(Supplier<? extends LS> constructor) 
    {
        listStrategy = constructor.get();
    }

    /**
     * Appends a list of items to the internal StringBuilder using the current list strategy.
     *
     * @param items the list of items to be appended
     */
    public void appendList(List<String> items)
    {
        listStrategy.start(sb);
        for (String item : items) {
            listStrategy.addListItem(sb, item);
        }
        listStrategy.end(sb);
    }

    /**
     * Clear the text processor.
     * <p>
     * This method erases all the content written to the text processor.
     * </p>
     */
    public void clear() {
        sb.setLength(0);
    }

    /**
     * Returns the content of the text processor as a String.
     * <p>
     * This method returns the content that was written to the text processor.
     * </p>
     * @return the content of the text processor as a String
     */
    @Override
    public String toString() {
        return sb.toString();
    }
}

public class StaticStrategy 
{
    public static void main(String[] args) 
    {
        StaticTextProcessor<MarkdownListStrategy> stp = 
            new StaticTextProcessor<>(MarkdownListStrategy::new);
        stp.appendList(List.of("Item 1", "Item 2", "Item 3"));
        System.out.println(stp);

        StaticTextProcessor<HTMLListStrategy> stp2 = 
            new StaticTextProcessor<>(HTMLListStrategy::new);
        stp2.appendList(List.of("Item A", "Item B", "Item C"));
        System.out.println(stp2);
    }
}
