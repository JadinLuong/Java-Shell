package driver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Find command in the command line.
 * 
 * @author Shamayum Rashad
 */
public class Find extends Command{

  /**
   * the file system the command will be working with
   */
  private FileSystem<Directory> fileSystem;

  /**
   * Constructs an object to represent the Find command on the command line.
   * 
   * @param fileSystem  the file system the command works on
   */
  public Find(FileSystem<Directory> fileSystem) {
    super(true);
    this.fileSystem = fileSystem;
  }

  /**
   * Executes the Find command, modifying the output string to include relative
   * paths of files with a given file name in the paths given.
   * 
   * @param arguments  the paths and parameters for the command
   */
  public void executeCommand(ArrayList<String> arguments) {

    String result = "";

    // if the syntax is valid, separate the paths and the parameters
    if (validSyntax(arguments)) {
      int typeIndex = arguments.indexOf("-type");
      List<String> pathsToSearch = arguments.subList(0, typeIndex);
      List<String> params = arguments.subList(typeIndex, arguments.size());
      String type = params.get(1);
      String fileName = params.get(3).substring(1, params.get(3).length() - 1);
      // search for files with the given name in each path in pathsToSearch
      for (String filePath: pathsToSearch) {
        result += findFiles(filePath, type, fileName) + "\n";
      }
    // if the syntax is not valid, give an error
    } else {
      this.setError("Find: invalid syntax: valid syntax as follows: find "
          + "path ... -type [f|d] -name expression");
    }

    // take the newline out at the end of results
    if (!result.isEmpty()) {
      result = result.substring(0, result.length() - 1);
    }

    this.setPrintCommand(result);
  }

  /**
   * Find files, specifically text files if type is f or directories if type is
   * d, with the given name fileName in the file given by the path filePath.
   * 
   * @param filePath  the file that will be searched
   * @param type      d if looking for directories and f if looking for text
   *                  files
   * @param fileName  the name of the files that are being searched for
   * @return          a string with the relative paths of all the files in 
   *                  filePath with the name fileName, each in a new line
   */
  private String findFiles(String filePath, String type, String fileName) {

    String result = "";
    ArrayList<String> fileMatches;
    Path path = new Path(this.fileSystem);

    try {
      File fileToSearch = path.searchByPath(filePath);

      // if the file is a directory, search for files with the given name in it
      if (fileToSearch instanceof Directory) {
        if (type.equals("f")) {
          fileMatches = recursiveTextFile(fileToSearch, fileName);
        } else {
          fileMatches = recursiveDirectory(fileToSearch, fileName);
        }
        for (String files: fileMatches) {
          result = files + "\n" + result;
        }
      }
      // if the file is not a directory, give an error
      else {
        this.setError(filePath + ": not a directory");
      }
    // if the path is not a valid path in the file system, give an error
    } catch (InvalidPathException e) {
      this.setError(filePath + ": directory not found");
    }

    // take the newline out at the end of results
    if (!result.isEmpty()) {
      result = result.substring(0, result.length() - 1);
    }

    return result;
  }

  /**
   * Recursively search for text files in currFile with the name FileName.
   * 
   * @param currFile  the file that is being searched
   * @param fileName  the name of the files being searched for
   * @return          a list with the relative paths of the text files with the
   *                  names matching fileName
   */
  private ArrayList<String> recursiveTextFile(File currFile, String fileName) {

    ArrayList<String> result = new ArrayList<>();

    // if the file is a text file and has the given file name, add it to result
    if ((currFile instanceof TextFile) &&
        (currFile.getFileName().equals(fileName))) {
      result.add(currFile.getFileName());
    }
    /*
     * otherwise if its a directory, see if there is a text file with the given
     * file name as a child of the directory
     */
    else if (currFile instanceof Directory) {
      ArrayList<File> fileChildren = ((Directory) currFile).getContents();
      for (File child: fileChildren) {
        ArrayList<String> recursiveResult = recursiveTextFile(child, fileName);
        if (!recursiveResult.isEmpty()) {
          for (int i = 0; i < recursiveResult.size(); i++) {
            String name = currFile.getFileName() + "/" + recursiveResult.get(i);
            if (name.startsWith("//")) {
              name = name.substring(1);
            }
            recursiveResult.set(i, name);
          }
          result.addAll(recursiveResult);
        }
      }
    }
    return result;
  }

  /**
   * Recursively search for directories in currFile with the name FileName.
   * 
   * @param currFile  the file that is being searched
   * @param fileName  the name of the files being searched for
   * @return          a list with the relative paths of the directories with
   *                  the names matching fileName
   */
  private ArrayList<String> recursiveDirectory(File currFile, String fileName) {

    ArrayList<String> result = new ArrayList<>();

    if (currFile instanceof Directory) {
      // if the file is a directory and has the given file name,add it to result
      if (currFile.getFileName().equals(fileName)) {
        result.add(currFile.getFileName());
      }
      /*
       * otherwise if its a directory then see if there are other directories
       * with the given file name as children of the directory
       */
      ArrayList<File> fileChildren = ((Directory) currFile).getContents();
      for (File child: fileChildren) {
        ArrayList<String> recursiveResult = recursiveDirectory(child, fileName);
        if (!recursiveResult.isEmpty()) {
          for (int i = 0; i < recursiveResult.size(); i++) {
            String name = currFile.getFileName() + "/" + recursiveResult.get(i);
            if (name.startsWith("//")) {
              name = name.substring(1);
            }
            recursiveResult.set(i, name);
          }
          result.addAll(recursiveResult);
        }
      }
    }
    return result;
  }

  /**
   * Check if the arguments are valid for the find command. A valid argument is
   * as follows: path ... -type [f|d] -name expression.
   * 
   * @param arguments  the list of words and strings passed to the command
   * @return           true if the arguments are valid, false if they are not
   */
  private boolean validSyntax(ArrayList<String> arguments) {

    boolean isValid = true;

    // split arguments into paths and parameters if arguments contains -type
    if (!arguments.contains("-type")) {
      isValid = false;
    }
    else {
      int typeIndex = arguments.indexOf("-type");
      List<String> pathsToSearch = arguments.subList(0, typeIndex);
      List<String> params = arguments.subList(typeIndex, arguments.size());
      /*
       * the syntax is invalid if there is no path given or incorrect amount of
       * parameters
       */
      if (pathsToSearch.isEmpty() || (params.size() != 4)) {
        isValid = false;
      }
      /*
       * the syntax is invalid if -type and -name aren't included in the
       * arguments or in the right order
       */
      else if ((!params.get(0).equals("-type")) || 
          (!params.get(2).equals("-name"))) {
        isValid = false;
      }
      /*
       * the syntax is invalid if the type is anything other than file or
       * directory and the name is not a string
       */
      else if ((!(params.get(1).equals("f") || params.get(1).equals("d"))) ||
          (!(params.get(3).startsWith("\"") && params.get(3).endsWith("\"")))) {
        isValid = false;
      }
    }
    return isValid;
  }
}
