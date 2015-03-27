package smartface.org.smartface_polygons.geometry;

import java.util.Collection;

/**
 * A line segment in space. Immutable.
 */
public class LineSegment {
  public final Point start, end;

  /**
   * A line segment is created where the start is always the closer
   * point to the origin
   */
  public LineSegment(Point start, Point end) throws Geometry.InvalidParameters {
    this.start = start;
    this.end = end;

    // Makes other code easier with *some* convention like this
    if (start == null || end == null) {
      throw new Geometry.InvalidParameters("Null end points");
    } else if (start.dist(Point.origin) > end.dist(Point.origin)) {
      Point tmp = start;
      start = end;
      end = tmp;
    }
  }

  /**
   * Returns a translated line segment
   */
  LineSegment translate(double deltaX, double deltaY) throws Geometry.InvalidParameters {
    return new LineSegment(
        start.translate(deltaX, deltaY),
        end.translate(deltaX, deltaY)
        );
  }

  /**
   * Two lines are equal if their start and end points are equal
   */
  public boolean equals(Object other) {
    return (other instanceof LineSegment) &&
            start.equals(((LineSegment) other).start) &&
            end.equals(((LineSegment) other).end);
  }

  /**
   * Returns whether or not two line segments share exactly one endpoint.
   *
   * If they share both endpoints, or no end points it returns false.
   */
  public boolean shareEndpoint(LineSegment other) {
    return start.equals(other.start) ^ end.equals(other.end);
  }

  /**
   * If lines share an endpoint, it will return true
   *
   * @param other is another line segment
   * @return true if two line segments intersetct
   * reference: http://alienryderflex.com/intersect/
   * <p/>
   * Ignoring detecting lines which overlap. TODO
   */
  public boolean intersect(LineSegment other) throws Geometry.InvalidParameters {
    if (equals(other) || shareEndpoint(other)) return true;
    else {
      // Translate all lines such that the first lines' start is at the origin.
      LineSegment A = translate(-start.x, -start.y);
      LineSegment B = other.translate(-start.x, -start.y);

      double lenA = A.end.dist(Point.origin);

      // Make line segment A horizontal, by rotating the entire system
      double sin = A.end.y / lenA;
      double cos = A.end.x / lenA;

      A = new LineSegment(A.start, new Point(lenA, 0)); // Just to help you visualize

      B = new LineSegment(
          new Point(
            B.start.x * cos + B.start.y * sin,
            B.start.y * cos - B.start.x * sin),
          new Point(
            B.end.x * cos + B.end.y * sin,
            B.end.y * cos - B.end.x * sin)
          );

      if ((B.start.y < 0 && B.end.y < 0) || (B.start.y >= 0 && B.end.y >= 0)) return false;

      double x_intercept = B.end.x + (B.start.x - B.end.x) * B.end.y / (B.end.y - B.start.y);

      return x_intercept > 0.0 && x_intercept < lenA;
    }
  }

  /**
   * Whether or not a point is on the line
   *
   * @param p a point to check if it belongs on the line segment or not
   */
  public boolean pointOnLine(Point p) throws Geometry.InvalidParameters {
    // Like LineSegment-LineSegment, I'll translate the
    // system, rotate it, and test if the point
    // is within the length of the transformed line segment
    LineSegment A = translate(-start.x, -start.y);
    p = p.translate(-start.x, -start.y);

    double lenA = A.end.dist(Point.origin);

    double sin = A.end.y / lenA;
    double cos = A.end.x / lenA;

    p = new Point(p.x * cos + p.y * sin, p.y * cos - p.x * sin);

    return (Math.abs(p.y) < Geometry.EPSILON) && (p.x > 0.0 && p.x - Geometry.EPSILON < lenA);
  }

  /**
   * Returns the intersections this line segment has with any of the line
   * segments in the given collection
   * 
   * @param segments is any collection of line segments
   */
  public int intersectsAny(Collection<LineSegment> segments) throws Geometry.InvalidParameters {
    int intersections = 0;

    for (LineSegment s : segments) {
      if (intersect(s)) intersections++;
    }
    return intersections;
  }

  public String toString() {
    return start.toString() + ", " + end.toString();
  }
}
