package smartface.org.smartface_polygons.geometry;

/**
 * Stores information about a point in space. Immutable.
 */
public class Point {
  static final Point origin;

  static {
      origin = new Point();
  }

  public final double x, y;

  private Point() {
    this.x = 0.0;
    this.y = 0.0;
  }

  /**
   * Creates a new point.
   *
   * @param x is the x coordinate of the point
   * @param y is the y coordinate of the point
   */
  public Point(double x, double y) throws Geometry.InvalidParameters {
    this.x = x;
    this.y = y;

    if(!(Geometry.valid(x) && Geometry.valid(y))) {
      throw new Geometry.InvalidParameters(
          "Coordinates outside boundaries" + toString ()
          );
    }
  }

  /**
   * Translates a point in space.
   *
   * @param deltaX is the translation on the x axis.
   * @param deltaY is the translation on the y axis.
   */
  public Point translate(double deltaX, double deltaY) throws Geometry.InvalidParameters {
    return new Point(x + deltaX, y + deltaY);
  }

  /**
   * Returns the distance between two points
   */
  public double dist(Point other) {
    return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
  }

  /**
   * Shows the coordinates of the point
   */
  public String toString() {
    return "(" + x + ", " + y + ")";
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
    return other instanceof Point && dist((Point) other) < Geometry.EPSILON;
  }

}
