# Interpreter

Now we're going to take a look at how to implement the interpreter design pattern by hand as opposed to using any kind of commercial framework. We're going to have two sections, first one we're going to dive deep into the lexical part, then after that we're going explore more about the parsing part.

## Lexing

Before we start let's imagine that we're given a string just as a calculation like `(13+4)-(12+1)`, and we want to calculate the appropriate value for that after the user enters this expression on command line, in order to accomplish our task we have to change this string into a bunch of tokens.

```java
class Demo
{
    public static void main(String[] args)
    {
        String expression = "(13+4)-(12+1)";
        List<Token> tokens = lex(expression);
    }
}
```

When splitting the entire string into a series of lexical tokens we're going to have a token for the opening round bracket, a token for the numbers, and a token for the operators as well. Let's see how we can actually set all of this up.

First we have to define the tokens somewhere where we're going to list all the different tokens that exist in our little demo.

```java
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
```

Now we define a function that returns a list of tokens given a string.

```java
class Demo
{
    // ...

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
                    break;
            }
        }
    }
}
```

The interesting part in here is parsing the integers, an integer not always consists of a single digit, so we cannot just take the first character and make a token out of it we've to find all the other characters as well. To do so we make a `StringBuilder` initialized with the current character then we'll go through the rest of the characters and if they're also integers them we add them to the `StringBuilder` and if they're not integers we break out of the loop and make a token out of the `StringBuilder` and add it to the result list. By using the java API `Character.isDigit` we can check if a character is a digit or not, and for every matched digit we increment our outer counter `i` so that we can move on to the next character.

```java
class Demo
{
    // ...

    static List<Token> lex(String expression)
    {
        List<Token> result = new ArrayList<>();

        for (int i = 0; i < expression.length(); ++i)
        {
            switch (expression.charAt(i))
            {
                // ...
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
    }
}
```

With this we're done in terms of the lexing process, at some point we've to return the result with all the tokens, so let's verify that it works.

```java
class Demo
{
    public static void main(String[] args)
    {
        String expression = "(13+4)-(12+1)";
        List<Token> tokens = lex(expression);

        System.out.println(tokens.stream()
            .map(t -> t.value)
            .collect(Collectors.joining("\t")));
    }
}
```

Output:

```txt
(       13      +       4       )       -       (       12      +       1       )
```

As we can see we're getting the correct output, so we've successfully taken a string and separated it into these chunks of tokens. The next part is taking those tokens and turning them into a tree of objects so that we can traverse and get the final result.

## Parsing

When it comes to parsing what we need to do is take the list of tokens we got and turn them into an object-oriented structure, before that we have to define those objects mapping the actual operations and elements that we're going to be working with.

We start by defining an interface called `Element` and every single element happens to be a numeric values since we're processing numeric expressions, hence every instance of this interface can be evaluated as numeric value we need to define a method called `eval()`.

```java
interface Element
{
    int eval();
}
```

The simplest thing that we can have is an integer right.

```java
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
```

That's pretty much that we need for an integer. What we're more interested here are binary operations because since we deal with some arithmetic operations like addition and subtraction we're going have those as part of a separate class called `BinaryOperation`. Before defining it we need to make clear what makes up a binary operation, so first we have to define the type of operation that is, we can use an enum for that, in addition we want to define the left and right parts of this binary operation and if we declare those as `Element` as a result this can be both a binary operation inside a binary operation or can be just an ordinary integer, there is no limit to the depth of this binary tree.

```java
class BinaryOperation implements Element
{

    public enum Type
    {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    public Type type;
    public Element left, right;

    @Override
    public int eval()
    {

    }
}
```

Now what we need to do is to perform the actual calculation, start evaluating things, for that we need to figure out what kind of operation it is. So we're going to switch on the type of operation for that.

```java
class BinaryOperation implements Element
{
    // ...

    @Override
    public int eval()
    {
        switch (type)
        {
            case ADD:
                return left.eval() + right.eval();
            case SUBTRACT:
                return left.eval() - right.eval();
            case MULTIPLY:
                return left.eval() * right.eval();
            case DIVIDE:
                return left.eval() / right.eval();    
            default:
                return 0;
        }
    }
}
```

We're done for now but the things start to increase in complexity now because we need to take a sequence of tokens and turn it into an actual object-oriented structure using the things that we've just defined. For now let's declare a static method that is going to be called `parse()` which returns an `Element` because an element is the root of the tree that we're parsing.

When we parse the binary operation we do that for both the left and right part, we're going to need a flag for indicating whether we've gone over the left part already. So we can use a boolean for doing just that, when it is true means that we already got the left-hand side and we need to process the right otherwise we need to process the left.

```java
class Demo
{
    // ...

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
                    break;
                case PLUS:
                    break;
                case MINUS:
                    break;
                case LPAREN:
                    break;
                case RPAREN:
                    break;
            }
        }
    }
}
```

This time around that is going to be easy to turn these into object-oriented structures. In case of `Integer` we need to make our new `IntegerElement` class parsing the text. This is where that `haveLHS` flag comes in.

```java
static Element parse(List<Token> tokens)
{
    // ...
    case INTEGER:
        IntegerElement element = new IntegerElement(Integer.parseInt(token.value));

        if (!haveLHS)
        {
            result.left = element;
            haveLHS = true;
        
        } else result.right = element;
        break;
    // ...
}
```

And that's it, let's move on to the arithmetic operations. Here all we have to do is to take the existing result and specify its type.

```java
static Element parse(List<Token> tokens)
{
    // ...
    case PLUS:
        result.type = BinaryOperation.Type.ADD;
        break;
    case MINUS:
        result.type = BinaryOperation.Type.SUBTRACT;
        break;
    // ...
}
```

Now with the left and right parenthesis will be kind of a challenge, when hitting the left parenthesis we need to search until we find the right, and then we recursively parse whatever is inside the brackets.

```java
static Element parse(List<Token> tokens)
{
    // ...
    case LPAREN:
        int j = 0; // index to the right parenthesis

        for (; j < tokens.size(); ++j) // go through j while j is less than the number of tokens
        {
            // if the token type is a RPAREN then that's where we break and at the end j points to the right parenthesis
            if (tokens.get(j).type == Token.Type.RPAREN) break;
        }
        break;
    // ...
}
```

The adopted approach above assumes that the isn't nesting of brackets for the sake of simplicity. The next step is to grab a sub-expression to retrieve the list of tokens to parse them recursively.

```java
static Element parse(List<Token> tokens)
{
    // ...
    case LPAREN:
        int j = 0;

        for (; j < tokens.size(); ++j)
        {
            if (tokens.get(j).type == Token.Type.RPAREN) break;

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

            i = j; // move to the right parenthesis to continue after the current expression
            break;
        }
    // ...
}
```

So now that we have some parsing code we can take those tokens that we acquired from the lexing part of our overall demo, and we can parse them and check the output that we get. Notice that our approach allows us to discard handling the right parenthesis.

```java
public static void main(String[] args)
{
    String input = "(13+4)-(12+1)";
    List<Token> tokens = lex(input);
    Element result = parse(tokens);

    System.out.println(input + " = " + result.eval());
}
```

Output:

```txt
(13+4)-(12+1) = 4
```

### Update

The code above has a bug regarding keeping track of the indexes of our sub-expressions, respectively the `i` and `j` variables. The right logic should be:

```java
case LPAREN:
    int j = i + 1;

    // ...

    i = j - 1;
    break;
```

**Suggested improvements:**

- Add support for the multiplication and division operations.
- Add support for negation.
- Add support for nested brackets.
- Try to change the input with expressions without brackets, there is room for improvement there.

## ANTLR

In the real world nobody designs parses by hand unless they're very simple. For the most cases what you do is go ahead and find a powerful tool which actually generates these parses for you, and one of them is called ANTLR which is a very popular parser generator for Java. It's a parser generator framework for translating structured text and even binary files as well.
