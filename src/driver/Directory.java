package driver;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a directory in the filesystem
 * 
 * @author Jadin Luong
 *
 */
public class Directory extends File {

  /**
   * the serial version for serialization
   */
  private static final long serialVersionUID = 4L;

  /**
   * Represents the contents/children of the directory
   */
  private ArrayList<File> contents;

  /**
   * Creates a directory with the given name and parent directory.
   * 
   * @param name Represents the name of the new directory.
   * @param parent Represents the parent directory of the new directory.
   */
  public Directory(String name, Directory parent) {
    super(name, parent);
    this.contents = new ArrayList<File>();
  }

  /**
   * Adds a new file into the specified directory.
   * 
   * @param data Represents the contents added into the directory.
   */
  public void addContents(File data)
      throws InvalidFileNameException, ExistingFileException {
    // Regex: check if there are any special characters
    String newFileName = data.getFileName();
    Pattern regex = Pattern.compile("[!@#$%^&*(){}~|<>?.]");
    Matcher match = regex.matcher(newFileName);
    // Check if there exists a file with the same name as the new file
    boolean fileExists = false;
    // Compare the new data's file name with each of the current directory's
    // files
    for (File content : this.getContents()) {
      if (content.getFileName().equals(newFileName)) {
        fileExists = true;
      }
    }
    // Regex checker
    if (match.find()) {
      throw new InvalidFileNameException(
          "File name must not contain any special characters.");
      // Existing file name checker
    } else if (fileExists) {
      throw new ExistingFileException("File already exists.");
    } else {
      this.getContents().add(data);
    }
  }

  /**
   * Returns the contents of the directory.
   * 
   * @return the contents of the directory.
   */
  public ArrayList<File> getContents() {
    return this.contents;
  }

  /**
   * Overwrites the content of the specified directory with new content.
   * 
   * @param newContent Represents the new content overwriting the old content
   */
  public void setContents(ArrayList<File> newContent) {
    this.contents = newContent;
  }

  /**
   * Prints the tree of a specified directory.
   * 
   * @return a string representation of the directory and it's content in a tree
   *         format
   */
  public String toString() {
    String retVal = "";
    int level = 0;
    // Call the recursive helper function
    retVal = this.printTree(this.getFileName(), level);
    return retVal;
  }

  /**
   * Checks to see if the two directory objects have similar properties.
   * 
   * @param directory represents the directory being compared to
   * @return whether the two directories being compare are the same
   */
  public boolean equals(Directory directory) {
    boolean isEqual = false;
    isEqual = this.getContents().equals(directory.getContents())
        && this.getFileName().equals(directory.getFileName());
    if (this.getParentDir() != null && directory.getParentDir() == null
        || this.getParentDir() == null && directory.getParentDir() != null) {
      isEqual = isEqual && false;
    } else if (this.getParentDir() == null
        && directory.getParentDir() == null) {
      isEqual = isEqual && true;
    } else {
      isEqual = isEqual && this.getParentDir().equals(directory.getParentDir());
    }
    return isEqual;
  }

  /**
   * Helper function for the toString() method.
   * 
   * @param output Contains the names of files of the specified directory
   * @param level Represents the level of the directory (used for recursion)
   * @return a tree representation of all files within a directory
   */
  private String printTree(String output, int level) {
    // Go through each file within the current directory
    for (int i = 0; i < this.getContents().size(); i++) {
      // For organization purposes
      output += "\n";
      // Retrieve and work with each of the current directory's files
      File dirContent = this.getContents().get(i);
      // Add spaces in correlation to the level of the current directory
      // (For organization purposes for the final output)
      for (int j = 0; j < level; j++) {
        output += "\t";
      }
      // Check whether the directory's content is either another directory or
      // a text file
      if (dirContent instanceof Directory) {
        output = ((Directory) dirContent)
            .printTree(output + "\t" + dirContent.getFileName(), level + 1);
      } else {
        output += "\t" + dirContent.getFileName();
      }
    }
    return output;
  }
}
