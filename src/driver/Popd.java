package driver;

import java.util.ArrayList;

/**
 * Represents the Popd command in the command line.
 * 
 * @author Shamayum Rashad
 */
public class Popd extends Command{

  /**
   * the file system the command will be working with
   */
  private FileSystem<Directory> fileSystem;

  /**
   * Constructs an object to represent the Popd command on the command line.
   * 
   * @param fileSystem  the file system that contains the directory stack the
   *                    command works on
   */
  public Popd(FileSystem<Directory> fileSystem) {
    super(false);
    this.fileSystem = fileSystem;
  }

  /**
   * Executes the Popd command, changing the current directory of the
   * fileSystem to the directory at the top of the fileSystem's directory stack.
   * 
   * @param arguments  an empty list
   */
  public void executeCommand(ArrayList<String> arguments) {
    
    DirectoryStack directoryStack = 
        ((JFileSystem) fileSystem).getDirectoryStack();

    /*
     *  if there are no arguments and the directory stack is empty, return an
     *  error, otherwise move the current directory to the directory at the top
     *  of the stack
     */
    if (arguments.isEmpty()) {
      if (directoryStack.isEmpty()) {
        this.setError("Popd: Directory Stack is empty");
      }
      else {
        fileSystem.setCurrentDirectory(directoryStack.pop());
      }
    }
    // if there are arguments given, return an error
    else {
      String invalidArgument = arguments.toString();
      this.setError(invalidArgument.substring
          (1, invalidArgument.length()-1)+ ": arguments given for popd");
    }
  }
}
