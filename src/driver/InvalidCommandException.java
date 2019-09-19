package driver;

/**
 * A class for an Exception used when the user inputs a command that does not
 * exist.
 *  
 * @author Shamayum Rashad
 */
@SuppressWarnings("serial")
public class InvalidCommandException extends Exception{

  /**
   * Constructs an exception for an incorrect command and gives a message.
   * 
   * @param message  the message given when the exception is thrown
   */
  public InvalidCommandException(String message) {
    super(message);
  }

}
