package driver;

/**
 * Represents a class for an Exception which will be used whenever the user
 * attempts to create a file with an invalid character which includes the
 * characters: "[", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}",
 * "~, "|", "<", ">", "?", ".", "/", "\", """, "]"
 * 
 * @author Jadin Luong
 *
 */
@SuppressWarnings("serial")
public class InvalidFileNameException extends Exception {

  /**
   * Creates an exception for an invalid file name and outputs a specific
   * message to the user.
   * 
   * @param message represents the message sent when file name is invalid
   */
  public InvalidFileNameException(String message) {
    super(message);
  }
}
