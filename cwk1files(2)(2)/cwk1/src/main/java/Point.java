import java.time.ZonedDateTime;
import static java.lang.Math.*;

/**
 * Represents a point in space and time, recorded by a GPS sensor.
 *
 * @author ALEX GEORGE
 */
public class Point {
  // Constants for bounds checking
  private static final double MIN_LONGITUDE = -180.0;
  private static final double MAX_LONGITUDE = 180.0;
  private static final double MIN_LATITUDE = -90.0;
  private static final double MAX_LATITUDE = 90.0;
  private static final double MEAN_EARTH_RADIUS = 6.371009e+6;

  // Instance variables for time and coordinates
  private ZonedDateTime time;
  private double longitude;
  private double latitude;
  private double elevation;

  // Constructor
  public Point(ZonedDateTime t, double lon, double lat, double elev) {
    // Check if longitude and latitude are within valid range
    if (lon < MIN_LONGITUDE || lon > MAX_LONGITUDE || lat < MIN_LATITUDE || lat > MAX_LATITUDE) {
      throw new GPSException("Invalid longitude or latitude");
    }
    // Initialize instance variables
    this.time = t;
    this.longitude = lon;
    this.latitude = lat;
    this.elevation = elev;
  }

  // Getter methods
  public ZonedDateTime getTime() {
    return this.time;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getElevation() {
    return this.elevation;
  }

  // Method to return a string representation of the point
  public String toString() {
    return String.format("(%.5f, %.5f), %.1f m", this.longitude, this.latitude, this.elevation);
  }

  // IMPORTANT: Do not alter anything beneath this comment!

  /**
   * Computes the great-circle distance or orthodromic distance between
   * two points on a spherical surface, using Vincenty's formula.
   *
   * @param p First point
   * @param q Second point
   * @return Distance between the points, in metres
   */
  public static double greatCircleDistance(Point p, Point q) {
    // Convert latitude and longitude to radians
    double phi1 = toRadians(p.getLatitude());
    double phi2 = toRadians(q.getLatitude());
    double lambda1 = toRadians(p.getLongitude());
    double lambda2 = toRadians(q.getLongitude());

    // Calculate the absolute difference in longitude
    double delta = abs(lambda1 - lambda2);

    // Calculate the terms used in the formula
    double firstTerm = cos(phi2)*sin(delta);
    double secondTerm = cos(phi1)*sin(phi2) - sin(phi1)*cos(phi2)*cos(delta);
    double top = sqrt(firstTerm*firstTerm + secondTerm*secondTerm);
    double bottom = sin(phi1)*sin(phi2) + cos(phi1)*cos(phi2)*cos(delta);

    // Return the great-circle distance
    return MEAN_EARTH_RADIUS * atan2(top, bottom);
  }
}
