class Point
{
    private double x, y;

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point newCartesianPoint(double x, double y) {
        return new Point(x, y);
    }

    public static Point newPolarPoint(double rho, double theta) {
        return new Point(rho * Math.cos(theta), rho * Math.sin(theta));
    }

    @Override
    public String toString() {
        return "Point{" +
        " point A = " + x + "," +
        " point B = " + y +
        "}";
    }
}

class Demo
{
    public static void main(String[] args) {
        Point polarPoint = Point.newPolarPoint(10, 100);
        Point cartPoint = Point.newCartesianPoint(12, 37);

        System.out.println(polarPoint);
        System.out.println(cartPoint);
    }
}