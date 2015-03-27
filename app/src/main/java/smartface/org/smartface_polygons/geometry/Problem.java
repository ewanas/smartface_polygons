package smartface.org.smartface_polygons.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Iterator;

/**
 * Created by ewanas on 3/21/15.
 * <p/>
 * No setters or getters here, keeping it simple.
 */
public class Problem {
  public static Random generator;

  static {
    generator = new Random(1337);
  }

  public Polygon poly;
  public Set<Point> points;

  private boolean solved = false;
  public Set<Point> inside;
  public Set<Point> outside;
  public Set<Point> onPoly;

  /**
   * Generate a random problem instance
   *
   * @param sides number of sides of the polygon
   * @param points number of points to classify
   */
  public Problem(int sides, int points) throws Geometry.InvalidParameters {
    this.poly = randomPolygon(sides);
    this.points = randomPoints(points);
  }

  /**
   * A problem with the given polygon and points
   *
   * @param poly
   * @param points
   */
  public Problem(Polygon poly, Set<Point> points) {
    this.poly = poly;
    this.points = points;
  }

  /**
   * reference: wikipedia
   *
   * For a point, we'll consider the line segment going to 2.0,2.0
   * if it intersects an even number of line segments, it's outside
   * if it intersects an odd number of line segments, it's inside
   *
   * If the point is on the polygon, the behaviour is undefined.
   */
  private boolean insidePoly (Point p) throws Geometry.InvalidParameters {
    LineSegment ray = new LineSegment(p, new Point(2.0, 2.0));

    int intersections = 0;

    for (LineSegment l : poly) {
      if (ray.intersect(l)) intersections++;
    }

    return intersections % 2 != 0;
  }

  /**
   * Checks if a point is on the polygon, that is, it intersects with any line
   * segment on the polygon.
   */
  private boolean onPoly (Point p) throws Geometry.InvalidParameters {
    for (LineSegment l : poly) {
      if (l.pointOnLine(p)) return true;
    }
    return false;
  }

  /**
   * Fills up the set of points on the polygon, inside the polygon and outside
   * the polygon.
   *
   * However, later calls don't attempt to solve the problem again.
   */
  public void solve() throws Geometry.InvalidParameters {
    if (!solved) {
      onPoly = new HashSet<Point> ();
      inside = new HashSet<Point> ();
      outside = new HashSet<Point> ();

      for (Point p : points) {
        if (onPoly(p)) onPoly.add(p);
        else if (insidePoly(p)) inside.add(p);
        else outside.add(p);
      }
      solved = true;
    }
  }

  /**
   * Iterates over the set of points on the polygon
   */
  public Iterator<Point> pointsOnPoly () {
    return onPoly.iterator();
  }

  /**
   * Iterates over the set of points inside the polygon
   */
  public Iterator<Point> pointsInside() {
    return inside.iterator ();
  }

  /**
   * Iterates over the set of points outside the polygon
   */
  public Iterator<Point> pointsOutside() {
    return outside.iterator ();
  }

  /**
   * Something doesn't work right when I do it with 5 sides, but this tries to generate
   * a valid random polygon, bare with me :)
   * <p/>
   * It's not very very random, but random enough to make me continue this assignment.
   *
   * @param sideCount number of sides the polygon should have. Please use 6 or more for now.
   * @throws Geometry.InvalidParameters if the sides are less than 3.
   * @return a randomly generated polygon with the given number of sides
   */
  public static Polygon randomPolygon(int sideCount) throws Geometry.InvalidParameters {
    if (sideCount < 3)
      throw new Geometry.InvalidParameters("Can't create a polygon with less than 3 sides");

    final double gap_min = 0.4;
    final double gap_max = 0.6;

    List<LineSegment> segments;

    Polygon polygon = null;
    boolean valid = false;

    while (!valid) {
      try {
        segments = new ArrayList<LineSegment>();
        Point start = new Point(Geometry.EPSILON, Math.max(generator.nextDouble(), gap_min));

        double increment = 1.0 / ((sideCount - 1.0) / 2.0);

        for (int i = 0; i < (sideCount - 1) / 2; i++) {
          Point end = new Point(increment * (i + 1), Math.min(gap_min, generator.nextDouble()));
          segments.add(new LineSegment(start, end));
          start = end;
        }

        for (int i = (sideCount - 1) / 2; i > 0; i--) {
          Point end = new Point(increment * (i), Math.max(gap_max, generator.nextDouble()));
          segments.add(new LineSegment(start, end));
          start = end;
        }

        // Last line is the last end point with the first start point.
        segments.add(new LineSegment(
              segments.get(segments.size() - 1).end,
              segments.get(0).start
              ));
        polygon = new Polygon(segments);
        valid = true;
      } catch (Geometry.InvalidParameters e) {
        // do nothing, try gain, we should generate a polygon anyway.
      }
    }

    return polygon;
  }

  /**
   * Returns a set of points where the coordinates are both
   * in the MIN_PROBLEM_BOUNDARY, MAX_PROBLEM_BOUNDARY range.
   *
   * @param count number of points to generate.
   * @return a set of points.
   */
  public static Set<Point> randomPoints(int count) {
    Set<Point> points = new HashSet<Point>();

    while (points.size() < count) {
      try {
        double x = Geometry.map(
                generator.nextDouble(), 0.0, 1.0,
                Geometry.MIN_PROBLEM_BOUNDARY, Geometry.MAX_PROBLEM_BOUNDARY);

        double y = Geometry.map(
                generator.nextDouble(), 0.0, 1.0,
                Geometry.MIN_PROBLEM_BOUNDARY, Geometry.MAX_PROBLEM_BOUNDARY);

        Point p = new Point(x, y);
        points.add(p);
      } catch (Geometry.InvalidParameters e) {
        // Try again
      }
    }

    return points;
  }

  public static void main(String[] args) throws Geometry.InvalidParameters {
    Problem p = new Problem(30, 20);

    for (LineSegment line : p.poly) {
      System.out.println(line);
    }
    for (Point point : p.points) {
      System.out.println(point);
    }
  }
}
