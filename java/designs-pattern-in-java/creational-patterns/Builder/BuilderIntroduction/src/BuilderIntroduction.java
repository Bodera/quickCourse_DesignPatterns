import static java.lang.String.format;
import java.util.stream.Stream;

class BuilderIntroduction 
{
    public static void main(String[] args) 
    {
        // %n will only work inside the String.format() method
        String hello = "hello";
        System.out.println(format("<p>%s</p>%n", hello));

        String [] words = {"hello", "world"};
        StringBuilder sb = new StringBuilder();
        sb.append(format("<ul>%n"));
        
        Stream.of(words).forEach(word -> {
            sb.append(format("  <li>%s</li>%n", word));
        });
        sb.append("</ul>");

        System.out.println(sb);
    }
}