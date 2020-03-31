import java.lang.Math;

public class Point {
    private int x, y;

    // Constructor
    // Point initialization (0, 0)
    public Point() {
      x = 0;
      y = 0;
    }

    // Point initialization (x, y)
    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }
    public int getY() {
      return y;
    }
    public void setX(int x) {
      this.x = x;
    }
    public void setY(int y) {
      this.y = y;
    }

    public void translate(Point p) {
      this.x += p.x;
      this.y += p.y;
    }

    public void translate(int x, int y) {
      this.x += x;
      this.y += y;
    }

    public double distance(Point p) {
      return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
    }

    // Print with format "(x,y)"
    public String print() {
      return ("(" + this.x + "," + this.y + ")");
    }
}