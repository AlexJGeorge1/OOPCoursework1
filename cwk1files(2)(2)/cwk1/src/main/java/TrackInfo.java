import java.io.FileNotFoundException;

/**
 * Program to provide information on a GPS track stored in a file.
 *
 * @author ALEX GEORGE
 */
public class TrackInfo {
  public static void main(String[] args) {
    // Check if a filename has been provided as a command line argument
    if (args.length != 1) {
      System.out.println("Please provide a filename as a command line argument.");
      System.exit(0);  // Exit the program with status code 0
    }

    try {
      // Create a new Track from the file
      Track track = new Track(args[0]);
      
      // Print information about the track
      System.out.println(track.size() + " points in track");
      System.out.println("Lowest point is " + track.lowestPoint());
      System.out.println("Highest point is " + track.highestPoint());
      System.out.printf("Total distance = %.3f km\n", track.totalDistance() / 1000);
      System.out.printf("Average speed = %.3f m/s\n", track.averageSpeed());
    } catch (GPSException e) {
      // Print the error message and exit the program with status code 1
      System.out.println("Error: " + e.getMessage());
      System.exit(1);
    }
  }
}
