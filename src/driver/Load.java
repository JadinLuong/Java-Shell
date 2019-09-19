package driver;

import java.util.ArrayList;

/**
 * Represents the Load command in the command line.
 * 
 * @author Shamayum Rashad
 */
public class Load extends Command {

  /**
   * the file system the command will be working with
   */
  private FileSystem<Directory> fileSystem;
  private FileSystemSaver saver;

  /**
   * Constructs an object to represent the Load command on the command line.
   * 
   * @param fileSystem the file system that should be overridden with the saved
   *        file system
   */
  public Load(FileSystem<Directory> fileSystem, FileSystemSaver saver) {
    super(false);
    this.fileSystem = fileSystem;
    this.saver = saver;
  }

  /**
   * Executes the load command, overriding the current file system to the one
   * saved in the file given by arguments. The file system should only be
   * overridden if load is the first command used.
   * 
   * @param arguments a list of file paths
   */
  public void executeCommand(ArrayList<String> arguments) {

    // if there is a file given and load is the first command used
    if ((arguments.size() == 1) && 
        (this.fileSystem.getCommandHistory().size() == 1)) {
      String file = arguments.get(0);
      this.setError(this.saver.getSavedFileSystem(file, fileSystem));
    }

    // otherwise, give the appropriate error
    else {
      manageErrors(arguments.size());
    }
  }

  /**
   * Sets the appropriate errors depending on the number of arguments given by
   * numberOfArguments.
   * 
   * @param numberOfArguments the number of arguments for the command given by
   *        the user
   */
  private void manageErrors(int numberOfArguments) {

    // if no arguments are given, give an error stating no arguments given
    if (numberOfArguments == 0) {
      this.setError("load: no file given");
    }

    /*
     * if one argument is given, the load command is used after the another
     * command so give an error to indicate that
     */
    else if (numberOfArguments == 1) {
      this.setError(
          "load: disabled (load command has to be first command used)");
    }

    // otherwise, there was more than one argument, give an error
    else {
      this.setError("load: too many files given");
    }
  }
}
