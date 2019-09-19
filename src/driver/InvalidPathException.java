package driver;

/**
 * For exception raised when path is invalid.
 * 
 * @author Ya-Tzu Wang
 */
@SuppressWarnings("serial")
public class InvalidPathException extends Exception {

  /**
   * construct the InvalidPathException with a message.
   * 
   * @param message the message shown when the exception is raised
   */
  public InvalidPathException(String message) {
    super(message);
  }

}
