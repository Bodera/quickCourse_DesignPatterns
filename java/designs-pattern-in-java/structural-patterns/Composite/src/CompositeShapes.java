import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class GraphicObject {

    protected String name = "Group";
    public String color;
    public List<GraphicObject> children = new ArrayList<>();

    public GraphicObject() {
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    private void print(StringBuilder sb, int depth)
    {
        sb.append(String.join("", Collections.nCopies(depth, "*")))
            .append(depth > 0 ? " " : "")
            .append((Objects.isNull(color) || color.isEmpty()) ? "" : color + " ")
            .append(getName())
            .append(System.lineSeparator());

        for (GraphicObject child : children)
            child.print(sb, depth + 1);
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        print(sb, 0);
        return sb.toString();
    }
}

class Circle extends GraphicObject 
{
    public Circle(String color)
    {
        name = "Circle";
        this.color = color;
    }    
}

class Square extends GraphicObject 
{
    public Square(String color)
    {
        name = "Square";
        this.color = color;
    }    
}

public class CompositeShapes
{
    public static void main(String[] args)
    {
        GraphicObject drawing = new GraphicObject();
        drawing.setName("My Drawing");
        drawing.children.add(new Circle("Red"));
        drawing.children.add(new Square("Yellow"));

        GraphicObject group = new GraphicObject();
        group.setName("Group 1");
        group.children.add(new Square("Black"));
        group.children.add(new Square("Gray"));
        drawing.children.add(group);

        System.out.println(drawing);
    }
}
