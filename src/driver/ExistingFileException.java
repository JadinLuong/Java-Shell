package driver;

/**
 * Represents a class for an Exception which will be used whenever the user
 * attempts to create a file that already exists within the directory they are
 * creating the specified file.
 * 
 * @author Jadin Luong
 *
 */
@SuppressWarnings("serial")
public class ExistingFileException extends Exception {

  /**
   * Creates an exception whenever the user creates a file with a name that
   * already exists within the directory.
   * 
   * @param message represents the message sent when file already exists
   */
  public ExistingFileException(String message) {
    super(message);
  }
}
