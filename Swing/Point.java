public class Point {
    private int x, y; // absis dan ordinat

    // Constructor
    // Set titik mula-mula ke (0, 0)
    public Point() {
      x = 0;
      y = 0;
    }

    // Set titik mula-mula ke (x, y)
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

    // Mengubah titik dengan penambahan Point argumen
    public void translate(Point p) {
      this.x += p.x;
      this.y += p.y;
    }

    // Mentranslasikan titik sebesar (x, y)
    public void translate(int x, int y) {
      this.x += x;
      this.y += y;
    }

    // Menghasilkan jarak antara titik sekarang dengan Point argumen
    public double distance(Point p) {
      return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
    }

    // Menuliskan titik ke layar dengan format "(x,y)"
    public void print() {
      System.out.println("(" + this.x + "," + this.y + ")");
    }
}