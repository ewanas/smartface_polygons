package smartface.org.smartface_polygons.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

  boolean solved = false;
  public Set<Point> inside;
  public Set<Point> outside;
  public Set<Point> onPoly;

  /**
   * Generate a random problem instance
   *
   * @param sides number of sides of the polygon
   * @param points number of points to classify
   */
  public Problem(int sides, int points) {
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
  private boolean insidePoly (Point p) {
    LineSegment ray = new LineSegment(p, new Point(2.0, 2.0));

    int intersections = 0;

    for (LineSegment l : poly) {
      if (ray.intersect(l, false)) intersections++;
    }

    return intersections % 2 != 0;
  }

  private boolean onPoly (Point p) {
    for (LineSegment l : poly) {
      if (l.pointOnLine(p)) return true;
    }
    return false;
  }

  public void solve() {
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
   * Something doesn't work right when I do it with 5 sides, but this tries to generate
   * a valid random polygon, bare with me :)
   * <p/>
   * It's not very very random, but random enough to make me continue this assignment.
   *
   * @param sideCount number of sides the polygon should have. Please use 6 or more for now.
   * @return
   */
  public static Polygon randomPolygon(int sideCount) {
    if (sideCount < 3) return new Polygon(new ArrayList<LineSegment>());

    final double gap_min = 0.4;
    final double gap_max = 0.6;

    List<LineSegment> segments;

    Polygon polygon;

    do {
      segments = new ArrayList<LineSegment>();
      Point start = new Point(Point.EPSILON, generator.nextDouble());

      start.y = Math.max(gap_min, start.y);

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
    } while (!polygon.isValid());

    return new Polygon(segments);
  }

  public static Set<Point> randomPoints(int count) {
    Set<Point> points = new HashSet<Point>();

    while (points.size() < count) {
      Point p = new Point(generator.nextDouble(), generator.nextDouble());
      points.add(p);
    }

    return points;
  }

  public static void main(String[] args) {
    Problem p = new Problem(30, 20);

    for (LineSegment line : p.poly) {
      System.out.println(line);
    }
    for (Point point : p.points) {
      System.out.println(point);
    }
  }
}
