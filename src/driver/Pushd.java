package driver;

import java.util.ArrayList;

/**
 * Represents the Pushd command in the command line
 * 
 * @author Shamayum Rashad
 */
public class Pushd extends Command{

  /**
   * the file system the command will be working on
   */
  FileSystem<Directory> fileSystem;
  /**
   * the path of the directory to move to
   */
  Path directoryPath;

  /**
   * Constructs an object to represent the Pushd command on the command line.
   * 
   * @param fileSystem  the file system that contains the directory stack the
   *                    command works on
   */
  public Pushd(FileSystem<Directory> fileSystem) {
    super(false);
    this.fileSystem = fileSystem;
    this.directoryPath = new Path(fileSystem);
  }

  /**
   * Executes the Pushd command, pushing the current directory of the
   * fileSystem to the fileSystem's directory stack and changing the current
   * directory to the one given in arguments.
   * 
   * @param arguments  the directory to change the current directory to
   */
  public void executeCommand(ArrayList<String> arguments) {

    DirectoryStack directoryStack = 
        ((JFileSystem) fileSystem).getDirectoryStack();

    // if there are no arguments given, return an error
    if (arguments.isEmpty()) {
      this.setError("Pushd: no directory given");
   }
    // if there are more than one arguments given, return an error
    else if (arguments.size() > 1) {
      String invalidArgument = arguments.toString();
      this.setError(invalidArgument.substring
          (1, invalidArgument.length()-1) + ": too many directories given");
    }
    /* 
     * otherwise, if the argument is a directory, push the current directory
     * onto the stack and move the current directory to the directory given
     */
    else {
      try {
        if (directoryPath.searchByPath(arguments.get(0)) instanceof Directory) {
          directoryStack.push(fileSystem.getCurrentDirectory());
            fileSystem.setCurrentDirectory(
                (Directory) directoryPath.searchByPath(arguments.get(0)));
        }
        // if the argument is not a directory, return an error
        else {
          this.setError(arguments.get(0) + ": not a directory");
        }
      } catch (InvalidPathException e) {
          this.setError
          (arguments.get(0) + ": No such file or directory");
        }
    }
  }
}
