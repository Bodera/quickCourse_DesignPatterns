public class AcyclicVisitor 
{
    public static void main(String[] args) 
    {
        SumExpression e = new SumExpression(
            new DoubleExpression(1), 
            new SumExpression(
                new DoubleExpression(2), 
                new DoubleExpression(3)));

        ExpressionPrinter ep = new ExpressionPrinter();
        ep.visit(e);

        ExpressionCalculator ec = new ExpressionCalculator();
        ec.visit(e);

        System.out.println(ep + " = " + ec.result);
    }
}

class ExpressionPrinter 
    implements DoubleExpressionVisitor, 
                SumExpressionVisitor
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
     * Appends the value of the given SumExpression to the StringBuilder.
     * The syntax is that of a binary expression, with the left and right
     * expressions surrounded by parentheses and joined by a plus sign.
     * @param se the SumExpression to append.
     */
    @Override
    public void visit(SumExpression se) 
    {
        sb.append("(");
        se.getLeft().accept(this);
        sb.append(" + ");
        se.getRight().accept(this);
        sb.append(")");
    }

    @Override
    public String toString() 
    {
        return sb.toString();
    }
}

class ExpressionCalculator
    implements DoubleExpressionVisitor, 
                SumExpressionVisitor
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
     * SumExpression and sets the result to this sum.
     * @param ae the SumExpression to visit.
     */

    @Override
    public void visit(SumExpression ae) 
    {
        ae.getLeft().accept(this);
        double leftResult = result;

        ae.getRight().accept(this);
        double rightResult = result;

        result = leftResult + rightResult;
    }
}

abstract class Expression
{
    /**
     * Accepts the given Visitor if it is an instance of ExpressionVisitor,
     * calling its visit method with this as the argument.
     * If the visitor is not an ExpressionVisitor, the method returns without
     * invoking any visit method.
     * @param visitor the Visitor to accept.
     */
    public void accept(Visitor visitor)
    {
        if (!(visitor instanceof ExpressionVisitor))
            return;

        ((ExpressionVisitor) visitor).visit(this);
    }
}

interface Visitor {} // marker interface

interface ExpressionVisitor extends Visitor 
{
    void visit(Expression obj);

}

interface DoubleExpressionVisitor extends Visitor 
{
    void visit(DoubleExpression obj);
}

interface SumExpressionVisitor extends Visitor 
{
    void visit(SumExpression obj);
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
     * Accepts the given Visitor if it is an instance of DoubleExpressionVisitor,
     * calling its visit method with this as the argument.
     * If the visitor is not a DoubleExpressionVisitor, the method returns without
     * invoking any visit method.
     * @param visitor the Visitor to accept.
     */
    @Override
    public void accept(Visitor visitor)
    {
        if (!(visitor instanceof DoubleExpressionVisitor))
            return;

        ((DoubleExpressionVisitor) visitor).visit(this);
    }
}

class SumExpression extends Expression 
{
    public Expression left, right;

    public SumExpression(Expression left, 
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
     * Accepts the given Visitor if it is an instance of SumExpressionVisitor,
     * calling its visit method with this as the argument.
     * If the visitor is not a SumExpressionVisitor, the method returns without
     * invoking any visit method.
     * 
     * @param visitor the Visitor to accept.
     */
    @Override
    public void accept(Visitor visitor)
    {
        if (!(visitor instanceof SumExpressionVisitor))
            return;

        ((SumExpressionVisitor) visitor).visit(this);
    }
}
