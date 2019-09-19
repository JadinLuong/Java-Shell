package driver;

import java.util.ArrayList;

/**
 * For command mv.
 * 
 * @author Ya-Tzu Wang
 */
public class Move extends Command {

  /**
   * path for searching file by its path
   */
  private Path path;

  /**
   * Constructs Move object.
   * 
   * @param fs the FileSystem
   */
  public Move(FileSystem<Directory> fs) {
    super(false);
    this.path = new Path(fs);
  }

  /**
   * Executes command mv.
   * 
   * @param arguments An list with information about command
   */
  public void executeCommand(ArrayList<String> arguments) {
    // if there is no files following command mv
    if (arguments.size() == 0) {
      this.setError("mv: missing file operand");// error
      // if there is only one file following command mv
    } else if (arguments.size() == 1) {
      this.setError(
          "mv: missing destination file operand after " + arguments.get(0));
      // if there is at least two files following command mv
    } else {
      try {
        // get the destination where files are going to move to
        String newPath = arguments.get(arguments.size() - 1);
        File destination = path.searchByPath(newPath);
        // move one file at a time
        for (int index = 0; index < arguments.size() - 1; index++) {
          String oldPath = arguments.get(index);
          File fileToMove = path.searchByPath(oldPath);
          this.moveFile(fileToMove, destination);
        }
      } catch (Exception e) {
        this.setError("Given paths invalid");// error
      }
    }
  }

  /**
   * Moves a file to the destination. If both file and destination are
   * TextFiles, the destination's content will be overwritten by the file's. If
   * both are directories, file will become destination's sub-directory.
   * 
   * @param fileToMove The file that is going to be moved to destination
   * @param destination The place where file will be moved to
   */
  private void moveFile(File fileToMove, File destination) {
    // if file can be moved
    if (this.filesValid(fileToMove, destination)) {

      // move a text file to the other one
      if ((fileToMove instanceof TextFile)
          && (destination instanceof TextFile)) {
        // overwrite the other's content
        ((TextFile) destination)
            .setContents(((TextFile) fileToMove).getContents());
        // delete itself
        this.removeFileFromParent(fileToMove);
        fileToMove.setParentDir(null);

        // move a file to a directory
      } else {
        try {
          // add child to parent
          ((Directory) destination).addContents(fileToMove);
          // delete file itself
          this.removeFileFromParent(fileToMove);
          // add parent to child
          fileToMove.setParentDir(((Directory) destination));
        } catch (Exception e) {
          this.setError("mv: file named " + fileToMove.getFileName()
              + " exists in " + destination.getFileName());
        }
      }
    }
  }

  /**
   * Removes the target file from its parent's contents
   * 
   * @param target A File we want to remove from its parent's contents
   */
  private void removeFileFromParent(File target) {
    // the list containing file that is to be removed
    ArrayList<File> files = target.getParentDir().getContents();
    // find the to-be-remove child then remove it, terminate loop if found
    int index = 0;
    boolean found = false;
    while (index < files.size() && !found) {
      if (files.get(index).equals(target)) {
        files.remove(index);
        found = true;
      }
      index++;
    }
  }

  /**
   * Returns true if files can be moved to destination; false if the procedure
   * cannot be completed and print error messages to user.
   * 
   * @param fileToMove The file that we want to move
   * @param destination The place where we want the file to move to
   * @return valid True if file can be moved, false otherwise
   */
  private boolean filesValid(File fileToMove, File destination) {
    boolean valid = false;
    // unable to move the same file
    if (fileToMove.equals(destination)) {
      this.setError("mv: " + fileToMove.getFileName() + " and "
          + destination.getFileName() + " are the same file");

      // unable to move the file to its parent
    } else if (fileToMove.getParentDir().equals(destination)) {
      this.setError("mv: " + fileToMove.getFileName() + " is already in "
          + destination.getFileName());

      // unable to move parent directory to any of its children
    } else if (destination.getParentDir().equals(fileToMove)) {
      this.setError("mv: cannot move a directory, " + fileToMove.getFileName()
          + ", into itself, " + destination.getFileName());

      // cannot move a directory to a text file
    } else if ((fileToMove instanceof Directory)
        && (destination instanceof TextFile)) {
      this.setError(
          "mv: cannot overwrite non-directory " + destination.getFileName()
              + " with directory " + fileToMove.getFileName());
      // return true if none of the above error happen
    } else {
      valid = true;
    }
    return valid;
  }
}
