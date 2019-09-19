package driver;

import java.util.ArrayList;

/**
 * Represents the mkdir command.
 * 
 * @author Jadin Luong
 *
 */
public class DirectoryMaker extends Command {

  /**
   * Represents the file system the command is going to work with.
   */
  private FileSystem<Directory> fileSystem;

  /**
   * Creates a DirectoryMaker object (mkdir) command.
   * 
   * @param fileS Represents a fileSystem.
   */
  public DirectoryMaker(FileSystem<Directory> fileS) {
    super(false);
    this.fileSystem = fileS;
  }

  /**
   * Executes the mkdir command.
   * 
   * @param arguments Represents the arguments given by the user.
   */
  public void executeCommand(ArrayList<String> arguments) {
    if (arguments.size() > 0) {
      FileSystem<Directory> fileSys = this.fileSystem;
      Path argumentPaths = new Path(fileSys);
      String newDirName = "";
      // Check each argument and perform operations correspondingly
      for (String args : arguments) {
        // Check if the argument is an absolute path
        if (args.charAt(0) == '/' && args.split("/").length <= 2) {
          newDirName = args.substring(1);
          this.makeDirectory(fileSys.getRoot(), newDirName);
          // Check if the argument is just the name of the new folder
        } else if (args.charAt(0) != '/' && args.split("/").length <= 1) {
          this.makeDirectory(fileSys.getCurrentDirectory(), args);
        } else {
          // Create new directory whether it is an absolute or relative path
          String existingPath = args.substring(0, args.lastIndexOf('/'));
          newDirName = args.substring(args.lastIndexOf('/') + 1, args.length());
          // Ensures the path exists
          try {
            File directoryPath = argumentPaths.searchByPath(existingPath);
            if (directoryPath instanceof Directory) {
              this.makeDirectory((Directory) directoryPath, newDirName);
            } else {
              this.setError(
                  "Specified path is not a path of directories: " + args);
            }
          } catch (InvalidPathException e) {
            this.setError(
                "The following path specified does not exist: " + args);
          }
        }
      }
    } else {
      this.setError("No arguments were specified.");
    }
  }

  /**
   * Makes a new directory into a specified directory. Mostly used as a helper
   * function for the DirectoryMaker class's executeCommand method.
   * 
   * @param parent represents the parent directory of the new directory
   * @param newDirName represents the name of the new directory
   */
  private void makeDirectory(Directory parent, String newDirName) {
    // Only create directory if it doesn't exist or it doesn't contain any
    // illegal characters.
    try {
      // Create and add the new directory into the file system.
      Directory newDirectory = new Directory(newDirName, parent);
      parent.addContents(newDirectory);
      // The corresponding error will occur depending on what the error is.
    } catch (ExistingFileException e) {
      this.setError("The following directory already exists: " + newDirName);
    } catch (InvalidFileNameException e) {
      this.setError(
          "The following directory being created contains illegal characters: "
              + newDirName);
    }
  }
}
