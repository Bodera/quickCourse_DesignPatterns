public class ReflectiveVisitor 
{
    public static void main(String[] args) 
    {
        Expression e = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));
        StringBuilder sb = new StringBuilder();
        ExpressionPrinter.print(e, sb);
        System.out.println(sb);
    }
}

class ExpressionPrinter 
{
    /**
     * Recursively prints the given expression into the provided StringBuilder.
     * If the expression is a DoubleExpression, it appends its value.
     * If the expression is an AdditionExpression, it appends the left and right
     * expressions in the format "(left+right)".
     * 
     * @param e the Expression to print.
     * @param sb the StringBuilder to append the printed expression to.
     */
    public static void print(Expression e, StringBuilder sb) 
    {
        if (e.getClass() == DoubleExpression.class) 
        {
            DoubleExpression de = (DoubleExpression)e;
            sb.append(de.value);
        }
        else if (e.getClass() == AdditionExpression.class) 
        {
            AdditionExpression ae = (AdditionExpression)e;
            sb.append("(");
            print(ae.left, sb);
            sb.append("+");
            print(ae.right, sb);
            sb.append(")");
        }
    }
}

abstract class Expression
{
    abstract void print(StringBuilder sb);
}

class DoubleExpression extends Expression 
{
    public double value;

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
    public Expression left, right;

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
