public class IntrusiveVisitor 
{
    public static void main(String[] args) 
    {
        AdditionExpression addExp = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)
            )
        );

        StringBuilder sb = new StringBuilder();
        addExp.print(sb);
        System.out.println(sb);
    }
}

abstract class Expression
{
    abstract void print(StringBuilder sb);
}

class DoubleExpression extends Expression 
{
    private double value;

    public DoubleExpression(double value) 
    {
        this.value = value;
    }

    /**
     * Prints the expression in the form "value".
     * @param sb the StringBuilder to append to.
     */
    @Override
    public void print(StringBuilder sb) 
    {
        sb.append(value);
    }
}

class AdditionExpression extends Expression 
{
    private Expression left, right;

    public AdditionExpression(Expression left, 
                                Expression right) 
    {
        this.left = left;
        this.right = right;
    }

    /**
     * Prints the expression in the form "(left+right)".
     * @param sb the StringBuilder to append to.
     */
    @Override
    public void print(StringBuilder sb) 
    {
        sb.append("(");
        left.print(sb);
        sb.append("+");
        right.print(sb);
        sb.append(")");
    }
}
