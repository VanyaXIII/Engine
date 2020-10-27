package physics.sphere;

public class Point2D {
    public double x, y;

    Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + "; " + y;
    }
}
