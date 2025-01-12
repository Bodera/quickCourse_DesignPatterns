import java.util.List;
import java.util.stream.Collectors;

public class Parsing 
{
    public static void main(String[] args)
    {
        String input = "(13+4)-(12+1)";
        List<Token> tokens = Lexing.lex(input);
        Element result = parse(tokens);

        System.out.println(input + " = " + result.eval());
    }

    static Element parse(List<Token> tokens)
    {
        BinaryOperation result = new BinaryOperation();
        boolean haveLHS = false; // short for left hand side

        for (int i = 0; i < tokens.size(); ++i)
        {
            Token token = tokens.get(i);

            switch (token.type)
            {
                case INTEGER:
                    IntegerElement intElement = new IntegerElement(Integer.parseInt(token.value));

                    if (!haveLHS)
                    {
                        result.left = intElement;
                        haveLHS = true;
                    
                    } else result.right = intElement;
                    break;
                case PLUS:
                    result.type = BinaryOperation.Type.ADD;
                    break;
                case MINUS:
                    result.type = BinaryOperation.Type.SUBTRACT;
                    break;
                case LPAREN:
                    int j = i + 1;

                    for (; j < tokens.size(); ++j)
                        if (tokens.get(j).type == Token.Type.RPAREN) 
                            break;

                    List<Token> subExpression =
                        tokens.stream()
                            .skip(i + 1) // skip the current token (the left parenthesis)
                            .limit(j - i - 1) // limit to the number of tokens between the left and right parenthesis
                            .collect(Collectors.toList());
        
                    Element element = parse(subExpression);
        
                    if (!haveLHS)
                    {
                        result.left = element;
                        haveLHS = true;
        
                    } else result.right = element;
        
                    i = j - 1; // skips past the right parenthesis
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}

interface Element
{
    int eval();
}

class IntegerElement implements Element
{

    private int value;

    public IntegerElement(int value)
    {
        this.value = value;
    }

    public int eval()
    {
        return value;
    }
}

class BinaryOperation implements Element
{

    public enum Type
    {
        ADD,
        SUBTRACT
    }

    public Type type;
    public Element left, right;

    @Override
    public int eval()
    {
        switch (type)
        {
            case ADD:
                return left.eval() + right.eval();
            case SUBTRACT:
                return left.eval() - right.eval();
            default:
                return 0;
        }
    }
}