public class ClassicVisitor 
{
    public static void main(String[] args) 
    {
        AdditionExpression e = new AdditionExpression(
            new DoubleExpression(1), 
            new AdditionExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);

        ExpressionCalculator ec = new ExpressionCalculator();
        ec.visit(e);

        System.out.println(ep + " = " + ec.result);
    }
}

class ExpressionPrinter implements ExpressionVisitor 
{
    private StringBuilder sb = new StringBuilder();

    /**
     * Appends the value of the given DoubleExpression to the StringBuilder.
     * @param de the DoubleExpression to append.
     */
    @Override
    public void visit(DoubleExpression de) 
    {
        sb.append(de.getValue());
    }

    /**
     * Appends the value of the given AdditionExpression to the StringBuilder.
     * The syntax is that of a binary expression, with the left and right
     * expressions surrounded by parentheses and joined by a plus sign.
     * @param ae the AdditionExpression to append.
     */
    @Override
    public void visit(AdditionExpression ae) 
    {
        sb.append("(");
        ae.getLeft().accept(this);
        sb.append("+");
        ae.getRight().accept(this);
        sb.append(")");
    }

    @Override
    public String toString() 
    {
        return sb.toString();
    }
}

class ExpressionCalculator implements ExpressionVisitor 
{
    public double result;

    /**
     * Sets the result to the value of the given DoubleExpression.
     * @param de the DoubleExpression to visit.
     */
    @Override
    public void visit(DoubleExpression de) 
    {
        result = de.getValue();
    }

    /**
     * Computes the sum of the left and right expressions of the given 
     * AdditionExpression and sets the result to this sum.
     * @param ae the AdditionExpression to visit.
     */

    @Override
    public void visit(AdditionExpression ae) 
    {
        ae.getLeft().accept(this);
        double leftResult = result;

        ae.getRight().accept(this);
        double rightResult = result;

        result = leftResult + rightResult;
    }
}

interface ExpressionVisitor 
{
    void visit(DoubleExpression de);
    void visit(AdditionExpression ae);
}

abstract class Expression
{
    abstract void accept(ExpressionVisitor ev);
}

class DoubleExpression extends Expression 
{
    public double value;

    public DoubleExpression(double value) 
    {
        this.value = value;
    }

    public double getValue() 
    {
        return value;
    }

    /**
     * Accepts the given ExpressionVisitor, calling its visit method
     * with this as the argument.
     * @param ev the ExpressionVisitor to accept.
     */
    @Override
    public void accept(ExpressionVisitor ev) 
    {
        ev.visit(this);
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

    public Expression getLeft() 
    {
        return left;
    }

    public Expression getRight() 
    {
        return right;
    }

    /**
     * Accepts the given ExpressionVisitor, calling its visit method
     * with this as the argument.
     * @param ev the ExpressionVisitor to accept.
     */
    @Override
    public void accept(ExpressionVisitor ev) 
    {
        ev.visit(this);
    }
}
