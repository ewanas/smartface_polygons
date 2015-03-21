package smartface.org.smartface_polygons.geometry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Given an instance of Polygon, it is a valid polygon with no loose ends
 * or weird intersections.
 */
public class Polygon implements Iterable<LineSegment> {
  List<LineSegment> lines;

  public Polygon(List<LineSegment> lines) {
    this.lines = lines;
  }

  /**
   * Changed the design a bit, I made validation optional, as I was having trouble generating
   * the polygon in a valid way.
   */
  public Polygon(List<LineSegment> lines, boolean validate) {
    this(lines);

    if (validate && !validDegrees())
      throw new IllegalArgumentException("Not all points have degree 2");
    if (validate && !validIntersections()) throw new IllegalArgumentException("Lines intersect");
  }

  /**
   * For a polygon to be valid, each point should belong to two line segments
   */
  private boolean validDegrees() {
    Map<Point, Integer> degrees = new HashMap<Point, Integer>();

    // Test for weird endpoints
    for (LineSegment l : lines) {
      if (degrees.containsKey(l.start)) {
        degrees.put(l.start, degrees.get(l.start) + 1);
      } else {
        degrees.put(l.start, 1);
      }

      if (degrees.containsKey(l.end)) {
        degrees.put(l.end, degrees.get(l.end) + 1);
      } else {
        degrees.put(l.end, 1);
      }
    }

    for (Entry<Point, Integer> entry : degrees.entrySet()) {
      if (entry.getValue() != 2) {
        return false;
      }
    }

    return true;
  }

  /**
   * Each line segment can't intersect any other line segment except at an endpoint, if at all.
   */
  private boolean validIntersections() {
    // Test for self intersections
    for (LineSegment i : lines) {
      for (LineSegment j : lines) {
        if (!i.equals(j)) {
          if (i.intersect(j, true)) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public boolean isValid() {
    return validIntersections() && validDegrees();
  }

  public Iterator<LineSegment> iterator() {
    return lines.iterator();
  }
}