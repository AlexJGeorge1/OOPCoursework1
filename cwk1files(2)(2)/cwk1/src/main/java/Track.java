import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a track, recorded by a GPS sensor.
 *
 * @author ALEX GEORGE
 */
public class Track {
  // Instance variable to store the points in the track
  private ArrayList<Point> points;

  // Default constructor that initializes the points ArrayList
  public Track() {
    this.points = new ArrayList<>();
  }

  // Constructor that takes a filename, initializes the points ArrayList, and reads the file
  public Track(String filename) {
    this();
    readFile(filename);
  }

  // Method to read a file and add points to the track
  public void readFile(String filename) {
    // Clear old data
    this.points.clear();
    
    File file = new File(filename);
    // Check if the file exists
    if (!file.exists()) {
        throw new GPSException("File does not exist");
    }
    
    try (Scanner scanner = new Scanner(file)) {
      // Skip the first line if it contains headers
      if (scanner.hasNextLine()) {
        scanner.nextLine();
      }
      
      // Read each line, create a Point, and add it to the track
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] parts = line.split(",");
        if (parts.length != 4) {
          throw new GPSException("Invalid record in file");
        }
        ZonedDateTime time = ZonedDateTime.parse(parts[0]);
        double longitude = Double.parseDouble(parts[1]);
        double latitude = Double.parseDouble(parts[2]);
        double elevation = Double.parseDouble(parts[3]);
        Point point = new Point(time, longitude, latitude, elevation);
        this.points.add(point);
      }
    } catch (FileNotFoundException e) {
      throw new GPSException(e.getMessage());
    }
  }

  // Method to add a point to the track
  public void add(Point point) {
    this.points.add(point);
  }

  // Method to get a point at a specific index in the track
  public Point get(int index) {
    if (index < 0 || index >= this.points.size()) {
      throw new GPSException("Invalid index");
    }
    return this.points.get(index);
  }

  // Method to get the size of the track
  public int size() {
    return this.points.size();
  }

  // Method to get the lowest point in the track
  public Point lowestPoint() {
    if (this.points.size() < 1) {
      throw new GPSException("Not enough points in track");
    }
    Point lowest = this.points.get(0);
    for (Point point : this.points) {
      if (point.getElevation() < lowest.getElevation()) {
        lowest = point;
      }
    }
    return lowest;
  }

  // Method to get the highest point in the track
  public Point highestPoint() {
    if (this.points.size() < 1) {
      throw new GPSException("Not enough points in track");
    }
    Point highest = this.points.get(0);
    for (Point point : this.points) {
      if (point.getElevation() > highest.getElevation()) {
        highest = point;
      }
    }
    return highest;
  }

  // Method to calculate the total distance of the track
  public double totalDistance() {
    if (this.points.size() < 2) {
      throw new GPSException("Not enough points in track");
    }
    double totalDistance = 0.0;
    for (int i = 0; i < this.points.size() - 1; i++) {
      totalDistance += Point.greatCircleDistance(this.points.get(i), this.points.get(i + 1));
    }
    return totalDistance;
  }

  // Method to calculate the average speed of the track
  public double averageSpeed() {
    if (this.points.size() < 2) {
      throw new GPSException("Not enough points in track");
    }
    double totalDistance = totalDistance();
    long seconds = ChronoUnit.SECONDS.between(this.points.get(0).getTime(), this.points.get(this.points.size() - 1).getTime());
    return totalDistance / seconds;
  }
}
