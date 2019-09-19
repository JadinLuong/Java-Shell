package driver;

/**
 * Interface for saving file systems.
 * 
 * @author Shamayum Rashad
 *
 */
public interface FileSystemSaver {

  /**
   * Saves the file system to a file named fileName.
   * 
   * @param fileName    the file name that the file system will be saved to.
   * @param fileSystem  the file system that will be saved
   * @return            any errors
   */
  public String saveFileSystem
  (String fileName, FileSystem<Directory> fileSystem);

  /**
   * Load the saved file system to the specified file system.
   * 
   * @param fileName    the file name of the file the file system is saved to.
   * @param fileSystem  the file system that will overloaded.
   * @return            the contents of the file
   */
  public String getSavedFileSystem
  (String fileName, FileSystem<Directory> fileSystem);

}
