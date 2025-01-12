import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lexing {

    public static void main(String[] args)
    {
        String expression = "(13+4)-(12+1)";
        List<Token> tokens = lex(expression);

        System.out.println(tokens.stream()
            .map(t -> t.value)
            .collect(Collectors.joining("\t")));
    }

    static List<Token> lex(String expression)
    {
        List<Token> result = new ArrayList<>();

        for (int i = 0; i < expression.length(); ++i)
        {
            switch (expression.charAt(i))
            {
                case '(':
                    result.add(new Token(Token.Type.LPAREN, "("));
                    break;
                case ')':
                    result.add(new Token(Token.Type.RPAREN, ")"));
                    break;
                case '+':
                    result.add(new Token(Token.Type.PLUS, "+"));
                    break;
                case '-':
                    result.add(new Token(Token.Type.MINUS, "-"));
                    break;
                default:
                    StringBuilder number = new StringBuilder("" + expression.charAt(i));

                    for (int j = i + 1; j < expression.length(); ++j)
                    {
                        if (Character.isDigit(expression.charAt(j)))
                        {
                            number.append(expression.charAt(j));
                            ++i;
                        }
                        else
                        {
                            result.add(new Token(Token.Type.INTEGER, number.toString()));
                            break;
                        }
                    }
                    break;
            }
        }

        return result;
    }
}

class Token
{
    public enum Type
    {
        INTEGER,
        PLUS,
        MINUS,
        LPAREN,
        RPAREN
    }

    public Type type;
    public String value;

    public Token(Type type, String value)
    {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return String.format("%s: %s", type, value);
    }
}