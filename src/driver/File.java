package driver;

import java.io.Serializable;

/**
 * Represents a file in the filesystem.
 * 
 * @author Jadin Luong
 *
 */
public class File implements Serializable {

  /**
   * the serial version for serialization
   */
  private static final long serialVersionUID = 2L;

  /**
   * Represents the name of the file
   */
  private String fileName = "";

  /**
   * Represents the parent directory of the file
   */
  private Directory parentDir;

  /**
   * Creates and empty file object.
   */
  public File() {
    this.fileName = "";
    this.parentDir = null;
  }

  /**
   * Creates a File object with a specified name and parent directory.
   */
  public File(String name, Directory parentDirectory) {
    this.fileName = name;
    this.parentDir = parentDirectory;
  }

  /**
   * Returns the name of the File.
   * 
   * @return the name of the file.
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * Changes the name of a given file.
   * 
   * @param name Represents the new name of the file
   */
  public void setFileName(String name) {
    this.fileName = name;
  }

  /**
   * Returns the parent directory of the file.
   * 
   * @return the parent directory of the file.
   */
  public Directory getParentDir() {
    return this.parentDir;
  }

  /**
   * Changes the parent directory of the current directory.
   * 
   * @param directory represents a new parent directory
   */
  public void setParentDir(Directory directory) {
    this.parentDir = directory;
  }
}
