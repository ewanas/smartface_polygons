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
  public final List<LineSegment> lines;

  /**
   * Only polygons which are valid can be constructed. It's up to the user
   * to provide valid lines.
   *
   * @throws Geometry.InvalidParameters when there's a null list of lines, or the lines constitute
   *          an invalid polygon.
   */
  public Polygon(List<LineSegment> lines) throws Geometry.InvalidParameters {
    this.lines = lines;

    if (lines == null) {
      throw new Geometry.InvalidParameters("Null list");
    } else if (!validDegrees ()) {
      throw new Geometry.InvalidParameters("Invalid degrees for vertices");
    } else if (!validIntersections ()) {
      throw new Geometry.InvalidParameters("Lines intersect");
    }
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
   *
   * @throws Geometry.InvalidParameters if during the test, points outside the boundaries had to
   * be created.
   */
  private boolean validIntersections() throws Geometry.InvalidParameters {
    // Test for self intersections
    for (LineSegment i : lines) {
      for (LineSegment j : lines) {
        if (!i.equals(j)) {
          if (!i.shareEndpoint(j)) {
            if (i.intersect(j)) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  /**
   * Allows the user to iterate over all line segments in this polygon
   */
  public Iterator<LineSegment> iterator() {
    return lines.iterator();
  }
}
