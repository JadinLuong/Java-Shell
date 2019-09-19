package driver;

import java.util.ArrayList;

/**
 * Represents the tree command.
 * 
 * @author Jadin Luong
 *
 */
public class Tree extends Command {

  /**
   * Represents the file system the tree command is working with.
   */
  private FileSystem<Directory> fileSystem;

  /**
   * Creates a Tree object (tree) command.
   * 
   * @param fileSys Represents a fileSystem.
   */
  public Tree(FileSystem<Directory> fileSys) {
    super(true);
    this.fileSystem = fileSys;
  }

  /**
   * Executes the tree command.
   * 
   * @param arguments represents the arguments entered by the user.
   */
  @Override
  public void executeCommand(ArrayList<String> arguments) {
    if (arguments.isEmpty()) {
      this.setPrintCommand(this.fileSystem.getRoot().toString());
    } else {
      this.setError("Invalid syntax for tree command: tree must not take"
          + " any arguments.");
    }
  }

}
