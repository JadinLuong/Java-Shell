package driver;

import java.util.ArrayList;

/**
 * For command cd.
 * 
 * @author Yihao Song
 */
public class DirectoryChanger extends Command {

  /**
   * the file system the command will be working on
   */
  private FileSystem<Directory> fileSystem;
  /**
   * the path of the directory in the file system
   */
  private Path path;

  /**
   * Construct a DirectoryChanger with a fileSystem to work with.
   * DirectoryChanger cannot be printed.
   * 
   * @param fileSystem  the file system the command will be working on
   */
  public DirectoryChanger(FileSystem<Directory> fileSystem) {
    super(false);
    this.fileSystem = fileSystem;
    this.path = new Path(fileSystem);
  }

  /**
   * Change the current Directory in the file system to given directory. Only
   * the first element in arguments would be checked. Input ".." would change
   * the current directory back to its parent. No parent directory implies no
   * change. Input "." would not change the current directory. Input any valid
   * path would change the current directory to that directory or print
   * appropriate warning message when path is not found or it is not a
   * directory. No input would also print error message.
   * 
   * @param arguments  a list of the path the current directory changes to
   */
  @Override
  public void executeCommand(ArrayList<String> arguments) {
    // if there is arguments
    if (arguments.size() > 0) {
      if (arguments.get(0).equals("..")) {
        // get the parentDirectory
        Directory parent = fileSystem.getCurrentDirectory().getParentDir();
        // set the currentDirectory as previous directory
        if (parent != null) {
          fileSystem.setCurrentDirectory(parent);
        }
      } else if (arguments.get(0).equals(".")) {
        // Does nothing
      } else {
        // change the directory to the given Directory
        changeDirectory(arguments.get(0));
      }
    } else {
      this.setError("Given a Directory to change.");
    }
  }

  /**
   * Change the current directory in file system to given Directory. Print
   * appropriate warning message when path is not found or it is not a
   * directory.
   * 
   * @param directory The String representing the path to directory
   */
  private void changeDirectory(String directory) {
    try {
      File file = path.searchByPath(directory);
      if (file instanceof Directory) {
        // Change the directory.
        fileSystem.setCurrentDirectory((Directory) file);
      } else {
        // TextFile is not a directory.
        this.setError(directory + " is a path to TextFile");
      }
    } catch (InvalidPathException e) {
      // Path to specified directory is not found.
      this.setError(directory + " is not a path to Directory");
    }
  }

  /**
   * Return a tree representation of current directory.
   * 
   * @return tree, a tree representation of current directory
   */
  public String toString() {
    return fileSystem.getCurrentDirectory().toString();
  }
}
