import java.util.List;

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

class TextProcessor 
{
    private StringBuilder sb = new StringBuilder();
    private ListStrategy listStrategy;

    public TextProcessor(OutputFormat format) 
    {
        setOutputFormat(format);
    }

    /**
     * Sets the output format for the text processor.
     * <p>
     * This method selects the list strategy to be used for appending lists of items to the internal StringBuilder.
     * <p>
     * @param format the output format to use
     * @throws IllegalArgumentException if the given output format is not supported
     */
    public void setOutputFormat(OutputFormat format) 
    {
        switch (format) {
            case MARKDOWN:
                listStrategy = new MarkdownListStrategy();
                break;
            case HTML:
                listStrategy = new HTMLListStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown output format");
        }
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

public class DynamicStrategy
{
    public static void main(String[] args) 
    {
        TextProcessor tp = new TextProcessor(OutputFormat.MARKDOWN);
        tp.appendList(List.of("Item 1", "Item 2", "Item 3"));
        System.out.println(tp);

        tp.clear();
        tp.setOutputFormat(OutputFormat.HTML);
        tp.appendList(List.of("Item A", "Item B", "Item C"));
        System.out.println(tp);
    }
}