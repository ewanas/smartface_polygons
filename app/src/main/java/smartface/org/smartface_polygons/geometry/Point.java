package smartface.org.smartface_polygons.geometry;

/**
 * Stores information about a point in space.
 */
public class Point {
  public static final double MIN_BOUNDARY = 0.0;
  public static final double MAX_BOUNDARY = 1.0;

  // Whether or not any point can be constructed outside the boundaries
  public static final boolean PEDANTIC = false;

  // Two points are considered equal if they are within EPSILON of each other
  public static final double EPSILON = 0.00000000000001;

  static final Point origin;

  static {
    origin = new Point(0.0, 0.0);
  }

  public double x, y;

  /**
   * Creates a new point.
   *
   * @param x is the x coordinate of the point
   * @param y is the y coordinate of the point
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;

    if (PEDANTIC && (x < MIN_BOUNDARY || x > MAX_BOUNDARY ||
            y < MIN_BOUNDARY || y > MAX_BOUNDARY)) {
      throw new IllegalArgumentException("Illegal point, not matching spec.");
    }
  }

  /**
   * Just an attempt at a hash that would spread this kind of data out.
   * <p/>
   * Namely 0 < x < 1 and 0 < y < 1
   * <p/>
   * reason: used in other code in a Set.
   */
  public int hashCode() {
    return ((int) (1 / x) << 7 ^ (int) (1 / y) << 11);
  }

  /**
   * Two points are equal if their coordinates are within epsilon of each other
   */
  public boolean equals(Object other) {
    return other instanceof Point && dist((Point) other) < EPSILON;
  }

  public Point translate(double deltaX, double deltaY) {
    return new Point(x + deltaX, y + deltaY);
  }

  /**
   * Returns the distance between two points
   */
  public double dist(Point other) {
    return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
  }

  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}