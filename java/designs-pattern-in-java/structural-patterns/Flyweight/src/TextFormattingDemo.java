import java.util.ArrayList;
import java.util.List;

public class TextFormattingDemo {
    
    public static void main(String[] args)
    {
        FormattedText ft = new FormattedText("Hello World!");
        ft.capitalize(0, 5);
        System.out.println(ft);

        FormattedTextImproved fti = new FormattedTextImproved("Hello World!");

        fti.getRange(6, 11).capitalize = true;

        System.out.println(fti);
    }
}

class FormattedText
{
    private String plainText;
    private boolean [] capitalized;

    public FormattedText(String plainText)
    {
        this.plainText = plainText;
        this.capitalized = new boolean[plainText.length()];
    }

    public void capitalize(int start, int end)
    {
        for (int i = start; i < end; ++i)
            capitalized[i] = true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plainText.length(); ++i)
        {
            if (capitalized[i])
                sb.append(Character.toUpperCase(plainText.charAt(i)));
            else
                sb.append(plainText.charAt(i));
        }
        return sb.toString();
    }
}

class FormattedTextImproved
{
    private String plainText;
    private List<TextRange> formatting = new ArrayList<>();

    public FormattedTextImproved(String plainText)
    {
        this.plainText = plainText;
    }

    public TextRange getRange(int start, int end)
    {
        TextRange range = new TextRange(start, end);
        formatting.add(range);
        return range;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plainText.length(); ++i)
        {
            char c = plainText.charAt(i);
            boolean capitalize = false, bold = false, italic = false, underline = false;
            for (TextRange range : formatting)
            {
                if (range.covers(i))
                {
                    capitalize |= range.capitalize;
                    bold |= range.bold;
                    italic |= range.italic;
                    underline |= range.underline;
                }

                if (capitalize)
                    c = Character.toUpperCase(c);

                /*if (bold)
                    c = (char) ("\u001B[1m" + c).codePointAt(0);

                if (italic)
                    c = (char) ("\u001B[3m" + c + "\u001B[0m").codePointAt(0);

                if (underline)
                    c = (char) ("\u001B[4m" + c + "\u001B[0m").codePointAt(0);*/
                
                }
            sb.append(c);
        }
        return sb.toString();
    }

    public class TextRange
    {
        private int start;
        private int end;
        public boolean capitalize, bold, italic, underline;

        public TextRange(int start, int end)
        {
            this.start = start;
            this.end = end;
        }

        public boolean covers(int position)
        {
            return position >= start && position < end;
        }
    }
}
