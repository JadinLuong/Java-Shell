package driver;

import java.util.ArrayList;

/**
 * Represents the echo commands in the command line.
 * 
 * @author Shamayum Rashad
 * @author Ya-Tzu Wang
 *
 */
public class Echo extends Command {

  /**
   * Constructs an object to represent the Echo command on the command line.
   */
  public Echo() {
    super(true);
  }

  /**
   * Executes the echo command, giving the input as the output without the
   * quotation marks.
   * 
   * @param arguments the file system that contains the text file to modify
   */
  public void executeCommand(ArrayList<String> arguments) {

    // if there is only one string provided, the output is the string
    if ((arguments.size() == 1) && (arguments.get(0).startsWith("\""))
        && (arguments.get(0).endsWith("\""))) {
      this.setPrintCommand(arguments.get(0).substring
          (1, arguments.get(0).length() - 1));
    }

    // else if the argument provided is not a string, give an error
    else if (arguments.size() == 1) {
      this.setError(arguments.get(0) + ": missing quotations");
    }

    // else if there are no strings provided, give an error
    else if (arguments.isEmpty()) {
      this.setError("echo: no string provided");
    }

    // otherwise, if there are too many arguments provided, give an error
    else {
      this.setError("echo: too many arguments given");
    }
  }
}
