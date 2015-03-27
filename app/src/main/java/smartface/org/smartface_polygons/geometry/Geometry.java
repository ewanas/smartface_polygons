package smartface.org.smartface_polygons.geometry;

public abstract class Geometry {
  public static final double MIN_BOUNDARY = -3.1;
  public static final double MAX_BOUNDARY = 3.1;

  public static final double MIN_PROBLEM_BOUNDARY = 0.0;
  public static final double MAX_PROBLEM_BOUNDARY = 1.0;

  // Whether or not any point can be constructed outside the boundaries
  public static final boolean PEDANTIC = true;

  // Two points are considered equal if they are within EPSILON of each other
  public static final double EPSILON = 0.00001;

  /**
   * Validates a double based on the boundaries and pedantic setting.
   *
   * MIN_BOUNDARY and MAX_BOUNDARY are used. So, if for any reason, an intermediate calculation
   * yields a point that is not within that range, then you're probably doing something wrong
   * and it's best that your program fails at that point. Or you can turn the pedantic setting
   * off if you don't know the bounds you need to operate within.
   */
  static boolean valid (double i) {
    if (PEDANTIC) {
      return i > MIN_BOUNDARY && i < MAX_BOUNDARY;
    }

    return true;
  }

  /**
   * This exception should be used or extended to differentiate user errors
   * from development errors.
   */
  public static class InvalidParameters extends Exception {
    public InvalidParameters(String msg) {
      super(msg);
    }

    public InvalidParameters(String msg, Throwable cause) {
      super(msg, cause);
    }
  }

  /**
   * Maps a value from some range to another range
   *
   * @param v the value to be mapped
   * @param min the minimum of the original range
   * @param max the maximum of the original range
   * @param min_a the minimum of the target range
   * @param max_a the maximum of the target range
   * @return a value within the target range that lies as far in the target range as it was
   *          in the original range.
   */
  public static double map(double v, double min, double max, double min_a, double max_a) {
    double fraction = (v - min) / (max - min);

    return min_a + (max_a - min_a) * fraction;
  }
}
