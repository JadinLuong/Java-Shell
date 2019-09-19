package driver;

import java.util.ArrayList;

/**
 * A Class for command ls, to be used in the JShell.
 * 
 * @author Yihao Song
 */
public class Ls extends Command {

  /**
   * The current fileSystem that Ls is working with and the pathFinder.
   */
  private FileSystem<Directory> fileSystem;
  /**
   * the path of the directory in the file system
   */
  private Path path;

  /**
   * Construct a Ls object with the path of the current directory and a root.
   * 
   * @param fileSystem the fileSystem that Ls operates on
   */
  public Ls(FileSystem<Directory> fileSystem) {
    super(true);
    this.fileSystem = fileSystem;
    this.path = new Path(fileSystem);
  }


  /**
   * Given an ArrayList of path to TextFile or Directory. executeCommand would:
   * 1) If there is no element in arguments, executeCommand would print the
   * content of the current directory. 2)If the element a path of TextFile,
   * executeCommand would print the full path of that TextFile. 3) If the
   * element is a path of Directory, executeCommand would print the the full
   * path of that Directory and the contents of it. 4) If the path given cannot
   * be found, a warning would be printed. Eventually, all input would be
   * executed.
   * 
   * @param arguments, An arrayList of path of TextFile or Directory.
   */
  @Override
  public void executeCommand(ArrayList<String> arguments) {
    /*
     * if there is nonempty arguments, interpret it. if there is Empty
     * arguments, print the contents of current directory.
     */
    if (arguments.size() > 0) {
      if (!arguments.get(0).equals("-R")) {
        this.setPrintCommand(listContents(arguments));
      } else if (arguments.size() == 1) {
        // print the contents of current Working directory.
        this.setPrintCommand(fileSystem.getCurrentDirectory().toString());
      } else {
        this.setPrintCommand(listAllContents(arguments));
      }
    } else {
      // print the contents of current Working directory.
      this.setPrintCommand(
          this.printContentsOf(fileSystem.getCurrentDirectory()));
    }

  }

  /**
   * Return the name of the current directory that Ls is working with.
   * 
   * @return String the name of the CurrentDirectory
   */
  @Override
  public String toString() {
    // return the name of the current directory
    return fileSystem.getCurrentDirectory().getFileName();
  }

  /**
   * Print the contents of the given directory vertically.
   * 
   * @param dr the directory to be printed
   * @return the contents of the directory
   */
  private String printContentsOf(Directory dr) {
    String output = "";
    ArrayList<File> contents = dr.getContents();
    for (File content : contents) {
      output += content.getFileName() + "\n";
    }
    return output;
  }

  /**
   * Extract the name of each content in the specified File and list them
   * vertically. If the given File is a TextFile, just list its name. All of the
   * results will be displayed in the order of arguments, and they could be
   * retrieved by using getPrintCommand().
   * 
   * @param arguments The ArrayList containing all user desired
   * @return output
   */
  private String listContents(ArrayList<String> arguments) {
    String output = "";
    for (String argument : arguments) {
      try {
        File file = path.searchByPath(argument);
        // If the file is a Directory, then list the contents of the
        // Directory.
        if (file instanceof Directory) {
          // Extract the name of each content and list them vertically.
          output += argument + ":\n";
          output += printContentsOf((Directory) file);
        } else {
          // the file is a TextFile, just print the path.
          output += argument + "\n";
        }
      } catch (InvalidPathException e) {
        // Warn the user that there is no file at given path.
        this.setError("There is no such file as:" + argument + "\n");
      }
    }
    return output;
  }

  /**
   * Recursively extract the name of all content and sub-contents in the
   * specified File and list them vertically. If the given File is a TextFile,
   * just list its name. All of the results will be displayed in the order of
   * arguments, and they could be retrieved by using getPrintCommand().
   * 
   * @param arguments the list of the paths
   * @return the contents of all the paths int the list
   */
  private String listAllContents(ArrayList<String> arguments) {
    String output = "";
    for (int i = 1; i < arguments.size(); i++) {
      String argument = arguments.get(i);
      try {
        File file = path.searchByPath(argument);
        // If the file is a Directory, then list the contents of the
        // Directory.
        if (file instanceof Directory) {
          // For each sub-content of this Directory, List all the all the
          // sub-directory
          output += argument += ":\n";
          for (File subcontent : ((Directory) file).getContents()) {
            output += recursiveHelper(subcontent, "\t");
          }
        } else {
          // the file is a TextFile, just print the path.
          output += argument + "\n";
        }
      } catch (InvalidPathException e) {
        // Warn the user that there is no file at given path.
        this.setError("There is no such file as:" + argument + "\n");
      }
    }
    return output;
  }

  /**
   * Recursively find all the sub-directory in the given directory. List only
   * one of them each line, and separate the level visually by tabs.
   * 
   * @param file The file to be extracted.
   * @param level The level relative to the first iteration represented by tabs.
   * @return output The list of the contents of the first directory and its
   *         subdirectory's contents.
   */
  private String recursiveHelper(File file, String level) {
    String output = "";
    if (file instanceof TextFile) {
      output += level + file.getFileName() + "\n";
    } else {
      if (((Directory) file).getContents().isEmpty()) {
        output += level + file.getFileName() + "\n";
      } else {
        for (File content : ((Directory) file).getContents()) {
          output += recursiveHelper(content, level + "\t");
        }
      }
    }
    return output;
  }
}
