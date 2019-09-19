package driver;

import java.util.ArrayList;

/**
 * For command cp.
 * 
 * @author Ya-Tzu Wang
 */
public class Copy extends Command {

  /**
   * path for searching file by its path
   */
  private Path path;

  /**
   * Constructs Copy object.
   * 
   * @param fs The FileSystem
   */
  public Copy(FileSystem<Directory> fs) {
    super(false);
    this.path = new Path(fs);
  }

  /**
   * Executes command cp.
   * 
   * @param arguments A list with information about command
   */
  public void executeCommand(ArrayList<String> arguments) {
    // if there is no files following command cp
    if (arguments.size() == 0) {
      this.setError("cp: missing file operand");
      // if there is only one file following command cp
    } else if (arguments.size() == 1) {
      this.setError(
          "cp: missing destination file operand after " + arguments.get(0));
      // if there is at least two files following command cp
    } else {
      try {
        String newPath = arguments.get(arguments.size() - 1);
        File destination = path.searchByPath(newPath);
        for (int index = 0; index < arguments.size() - 1; index++) {
          String oldPath = arguments.get(index);
          File fileToCopy = path.searchByPath(oldPath);
          this.copyFile(fileToCopy, destination);
        }
      } catch (Exception e) {
        this.setError("Given paths invalid");
      }
    }
  }

  /**
   * Copy the given file and store the copy in the given destination file.
   * 
   * @param fileToCopy The file that we want a copy of
   * @param destination The place where we want the copy go
   */
  private void copyFile(File fileToCopy, File destination) {

    if (this.filesValid(fileToCopy, destination)) {

      // if we are copying a Directory to another Directory
      if ((fileToCopy instanceof Directory)
          && (destination instanceof Directory)) {
        this.copyDirectory((Directory) fileToCopy, (Directory) destination);

        // if we are copying a TextFile to a Directory
      } else if ((fileToCopy instanceof TextFile)
          && (destination instanceof Directory)) {
        TextFile newFile = new TextFile(fileToCopy.getFileName(),
            (Directory) destination, ((TextFile) fileToCopy).getContents());
        try {
          ((Directory) destination).addContents(newFile);
        } catch (Exception e) {
          this.setError("cp: file named " + newFile.getFileName()
              + " exists in " + destination.getFileName());
        }

        // if we are copying a TextFile's content to overwrite another
        // TextFile's content
      } else {
        String newContent = ((TextFile) fileToCopy).getContents();
        ((TextFile) destination).setContents(newContent);
      }
    }
  }

  /**
   * Returns true if files can be copied to destination; false if the procedure
   * cannot be completed and print error messages to user.
   * 
   * @param fileToCopy The file that we want a copy of
   * @param destination The place where we want the copy go
   * @return valid True if file can be copied, false otherwise
   */
  private boolean filesValid(File fileToCopy, File destination) {
    boolean valid = false;
    // unable to copy the same file
    if (fileToCopy.equals(destination)) {
      this.setError("cp: " + fileToCopy.getFileName() + " and "
          + destination.getFileName() + " are the same file");

      // unable copy a file to its parent
    } else if (fileToCopy.getParentDir().equals(destination)) {
      this.setError("cp: " + fileToCopy.getFileName() + " is already in "
          + destination.getFileName());

      // cannot copy a directory to itself
    } else if (destination.getParentDir().equals(fileToCopy)) {
      this.setError("cp: cannot copy a directory, " + fileToCopy.getFileName()
          + ", into itself, " + destination.getFileName());

      // unable to copy directory to text file
    } else if ((fileToCopy instanceof Directory)
        && (destination instanceof TextFile)) {
      this.setError(
          "cp: cannot overwrite non-directory " + destination.getFileName()
              + " with directory " + fileToCopy.getFileName());

      // return true when none of the above errors happen
    } else {
      valid = true;
    }
    return valid;
  }

  /**
   * Copy the given directory and set the copy's parent as the given parent.
   * 
   * @param dir The directory that we want to copy
   * @param parent The parent of the copy of dir (newDir)
   * @return newDir The copy of dir and has parent as the given one
   */
  private Directory copyDirectory(Directory dir, Directory parent) {
    // get a list of children that are to be copied
    ArrayList<File> Children = dir.getContents();
    // initialize the copy directory
    Directory newDir = new Directory(dir.getFileName(), parent);
    // add the new copy as parent's child
    try {
      parent.addContents(newDir);
    } catch (Exception e1) {
      this.setError("cp: file named " + newDir.getFileName() + " exists in "
          + parent.getFileName());
    }
    // copy children one by one
    for (int index = 0; index < Children.size(); index++) {
      File file = Children.get(index);
      // recursive case: copy a directory
      if (file instanceof Directory) {
        this.copyDirectory((Directory) (file), newDir);
        // base case: copy TextFile
      } else {
        TextFile newFile = new TextFile(file.getFileName(), newDir,
            ((TextFile) (file)).getContents());
        try {
          newDir.addContents(newFile);
        } catch (Exception e) {
        }
      }
    }
    // return the copy directory
    return newDir;
  }
}
