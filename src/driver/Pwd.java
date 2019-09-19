package driver;

import java.util.ArrayList;

/**
 * Represents the pwd command in the command line.
 * 
 * @author Shamayum Rashad
 */
public class Pwd extends Command {

  /**
   * the file system the command will work on
   */
  FileSystem<Directory> fileSystem;
  /**
   * the path of the current directory
   */
  Path currentPath;

  /**
   * Constructs an object to represent the Pwd command on the command line.
   * 
   * @param fileSystem  the file system the command works on
   */
  public Pwd(FileSystem<Directory> fileSystem) {
    super(true);
    this.fileSystem = fileSystem;
    this.currentPath = new Path(fileSystem);
  }

  /**
   * Executes the pwd command, modifying the output string to the path of the
   * current directory of the fileSystem.
   * 
   * @param arguments an empty list
   */
  public void executeCommand(ArrayList<String> arguments) {

    Directory currentDirectory = fileSystem.getCurrentDirectory();
    /*
     * if there are no arguments given, add the path of the current directory to
     * printCommand
     */
    if (arguments.isEmpty()) {
      this.setPrintCommand(currentPath.getPath(currentDirectory));
    }
    // otherwise, add an error message to printCommand
    else {
      String invalidArgument = arguments.toString();
      this.setError(
          invalidArgument.substring(1, invalidArgument.length() - 1)
              + ": pwd does not take any inputs");
    }
  }
}
