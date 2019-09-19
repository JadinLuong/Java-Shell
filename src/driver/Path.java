package driver;

import java.util.ArrayList;

/**
 * Represents path of a File (File includes a Directory or a TextFile).
 * 
 * @author Ya-Tzu Wang
 */
public class Path {
  /**
   * the root directory of the fileSystem
   */
  private Directory root;
  /**
   * the current directory of the fileSystem
   */
  private Directory current;
  /**
   * the symbol used to separate filenames
   */
  private static final String SEPARATOR = "/";

  /**
   * Construct Path with a FileSystem for access to root and current
   * directories.
   * 
   * @param fs the FileSystem which has access to root and current directories.
   */
  public Path(FileSystem<Directory> fs) {
    this.root = fs.getRoot();
    this.current = fs.getCurrentDirectory();
  }

  /**
   * For command pwd (print working directory). Given a File (could be a
   * Directory or a TextFile), return the path of the file in proper format.
   * 
   * @param file a File that we wish to find its path
   * @return res a String containing the path of the given file
   */
  public String getPath(File file) {
    String result = this.getPathHelper(file);
    // trim excess separator at the end
    if (result.length() > 1) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  /**
   * Given a File (could be a Directory or a TextFile), return the path of the
   * file.
   * 
   * @param file a File that we wish to find its path
   * @return result a String containing the path of the given file
   */
  private String getPathHelper(File file) {
    String result = "";
    // base case
    if (file.equals(this.root)) {
      result = this.root.getFileName();
      // recursive case
    } else {
      result = this.getPathHelper(file.getParentDir()) + file.getFileName()
          + SEPARATOR;
    }
    return result;
  }

  /**
   * Given the path of a File (could be a Directory or a TextFile), return the
   * file if found, or return error message if not found.
   * 
   * @param path a String representing the location of the target File
   * @return file a File found by using given path
   * @throws InvalidPathException meaning file with this path does not exist
   */
  public File searchByPath(String path) throws InvalidPathException {
    // get an array of the file names that we wish to find in the fileSystem
    ArrayList<String> targetFileNames = this.RetrieveFileNames(path);
    // determine to start at root or current directory
    File file = this.getStartingDirectory(path);
    // get a list of existing files from starting directory
    ArrayList<File> actualFiles = ((Directory) file).getContents();
    for (int i = 0; i < targetFileNames.size(); i++) {
      String name = targetFileNames.get(i);
      // get parent if given ..
      if (name.equals("..")) {
        file = file.getParentDir();
        if (file == null) {
          file = this.root;
        }
        // if target file name is not one of "..", ".", and "", search for it
      } else {
        file = this.findFileWithName(actualFiles, name);
      }
      // advance to next level
      if (file instanceof Directory) {
        actualFiles = ((Directory) file).getContents();
      }
    }
    return file;
  }

  /**
   * Given a path (absolute or relative), return the appropriate starting
   * directory such as root or current directory. This is a helper function for
   * searchByPath.
   * 
   * @param path a string where filenames are separated by forward slash
   * @return startingDirectory a Directory which is either the root of the
   *         FileSystem or the current directory
   */
  private Directory getStartingDirectory(String path) {
    // if absolute path (starting at root)
    Directory startingDirectory = this.root;
    // if relative path
    if (!(path.startsWith(this.root.getFileName()))) {
      startingDirectory = this.current;
    }
    return startingDirectory;
  }

  /**
   * Given a path (absolute or relative), return an Array containing fileNames.
   * 
   * @param path a string where filenames are separated by forward slash
   * @return result a list containing Strings representing fileNames
   */
  private ArrayList<String> RetrieveFileNames(String path) {
    path = path.trim();
    // break down path using separator /
    String[] fileNames = path.split(SEPARATOR);
    // initialize list to store file names
    ArrayList<String> result = new ArrayList<String>();
    for (String name : fileNames) {
      name = name.trim();
      // empty string and . will not affect the location of the file
      if ((!(name.equals(""))) && (!(name.equals(".")))) {
        result.add(name);
      }
    }
    return result;
  }

  /**
   * Given An ArrayList containing Files and a String representing a filename ,
   * return the File with the name or throw an exception if the File is not
   * found.
   * 
   * @param files An ArrayList containing Files
   * @param name the name of the file that we are looking for
   * @return file the File with the desired name
   * @throws InvalidPathException if File with this path does not exist
   */
  private File findFileWithName(ArrayList<File> files, String name)
      throws InvalidPathException {
    // search for file with the given name
    for (int index = 0; index < files.size(); index++) {
      File file = files.get(index);
      if (file.getFileName().equals(name)) {
        return file;
      }
    }
    // at this point, it's clear the File with given fileName does not exist
    throw new InvalidPathException("File with this path does not exist.");
  }
}
