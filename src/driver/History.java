package driver;

import java.util.ArrayList;

/**
 * 
 * Represents the History command in the command line.
 * 
 * @author Shamayum Rashad
 *
 */
public class History extends Command {

  /**
   * the file system the command will be working on
   */
  private FileSystem<Directory> fileSystem;

  /**
   * Constructs an object to represent the history command on the command line.
   * 
   * @param fileSystem  the file system the command works on
   */
  public History(FileSystem<Directory> fileSystem) {
    super(true);
    this.fileSystem = fileSystem;
  }

  /**
   * Executes the history command, modifying the output string to include the
   * history of commands used on the fileSystem, listing the history starting
   * from the last command indicated by the argument until the most recent one. 
   * 
   * @param arguments  the number of history commands wanted, as given by the
   *                   user
   */
  public void executeCommand(ArrayList<String> arguments) {

    int historySize = this.fileSystem.getCommandHistory().size();

    // if there is no argument given, give the whole command history
    if (arguments.size() == 0) {
      this.setPrintCommand(historyTilNumber(historySize));
    }

    // if there is one argument, give command history until the argument given
    else if ((arguments.size() == 1) && (arguments.get(0).matches("[0-9]+"))) {
      int historyTil = Integer.parseInt(arguments.get(0)); 
      if (historyTil < historySize) {
        this.setPrintCommand(historyTilNumber(historyTil));
      }
      else {
        this.setPrintCommand(historyTilNumber(historySize));
      }
    }

    // if there is a non-integer argument or > 1 arguments, give an error
    else if (arguments.size() == 1) {
      this.setError(arguments.get(0) + ": not a positive integer");
    }
    else {
      String argument = arguments.toString();
     this.setError(argument.substring
         (1, argument.length()-1) + ": more than one argument given");
    }
  }

  /**
   * Returns the command history, from the most recent command to the number
   * specified.
   * 
   * @param number  the amount of command history to give
   * @return        the command history until the number
   */
  private String historyTilNumber(int number) {

    String history = "";
    int commandsAdded = 0;
    int numberOfCommands = this.fileSystem.getCommandHistory().size();

    // add command history from the array list to string with a newline
    while (!(commandsAdded == number)) {
      history = "\n" + String.valueOf(numberOfCommands)
      + ". " + this.fileSystem.getCommandHistory().get(numberOfCommands - 1) +
      history;
      numberOfCommands--;
      commandsAdded++;
    }

    if (history.length() > 0) {
      history = history.substring(1);
    }
    // return the string history without the first newline
    return history;
  }

  /**
   * Returns a String representing the commands inputed by the user with a
   * comma separating them.
   * 
   * @return  toString a String showing the command history
   */
  public String toString() {
    String toReturn = "";
    for (String command: this.fileSystem.getCommandHistory()) {
      toReturn += ", " + command;
    }
    return toReturn.substring(2, toReturn.length() - 1);
  }
}
