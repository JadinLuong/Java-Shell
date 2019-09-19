package driver;

import java.util.ArrayList;

/**
 * Represents a generic command on the command line.
 * 
 * @author Shamayum Rashad
 */
public abstract class Command {

  /**
   * generic command output string
   */
  private String printCommand = "";
  /**
   * generic command error string
   */
  private ArrayList<String> error = new ArrayList<>();
  /**
   * to check if the command has errors
   */
  private boolean hasError = false;

  /**
   * to determine if the command output can be redirected
   */
  private final boolean redirectable;

  /**
   * Constructor for use in the command subclasses to determine the value of
   * the final variable, redirectable.
   * 
   * @param isRedirectable  true if the command output is redirectable, false
   *                        if it is not
   */
  protected Command(boolean isRedirectable) {
    redirectable = isRedirectable;
  }
  
  /**
   *  Executes the command with the arguments of the commands given.
   *  
   * @param arguments  list of arguments to execute
   */
  public abstract void executeCommand(ArrayList<String> arguments);

  /**
   * Get the command output that needs to be printed.
   * 
   * @return  the command output
   */
  public String getPrintCommand() {

    return this.printCommand;
  }

  /**
   * Set the command output that needs to be printed.
   * 
   * @param output  the output of the command that needs to be printed.
   */
  public void setPrintCommand(String output) {

    this.printCommand += output;
  }

  /**
   * Get the error the command has.
   * 
   * @return  the command error
   */
  public String getError() {

    String outputErrors = "";

    // put all the errors in a string
    for (String errors: error) {
      outputErrors += errors + "\n";
    }

    // take the newline out at the end of results
    if (!outputErrors.isEmpty()) {
      outputErrors = outputErrors.substring(0, outputErrors.length() - 1);
    }
    return outputErrors;
  }

  /**
   * Set the errors of the command.
   * 
   * @param error  the error that the command has
   */
  public void setError(String error) {

    this.error.add(error);

    this.setHasError();
  }

  /**
   * Get whether or not the command has an error.
   * 
   * @return  whether the command has an error
   */
  public boolean getHasError() {

    return this.hasError;
  }

  /**
   * Get whether or not the command output is redirectable.
   * 
   * @return  true if the command output is redirectable, false if it is not
   */
  public boolean isRedirectable() {

    return this.redirectable;
  }

  /**
   * Set whether or not the command has an error.
   */
  private void setHasError() {

    if (!this.error.isEmpty()) {
      this.hasError = true;
    }
  }

}
