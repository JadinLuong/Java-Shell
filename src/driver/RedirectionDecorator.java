package driver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the redirection command in the command line. If the command
 * includes >, the command output overwrites the file given, if the command
 * includes >>, the command output appends to the file given. Otherwise, it
 * returns the output.
 * 
 * @author Shamayum Rashad
 *
 */
public class RedirectionDecorator extends Command{

  
  /**
   * the file system the command will be working on
   */
  private FileSystem<Directory> fileSystem;
  /**
   * the redirection, append, overwrite, or standard
   */
  private String redirection;
  /**
   * the file the redirection should be redirected to
   */
  private List<String> redirectionPath;
  /**
   * the command object that the output will be taken from
   */
  private Command command;
  /**
   * the type of the command
   */
  private String commandType;
  

  /**
   * Constructs an object to represent the redirection decorator for the 
   * command.
   * 
   * @param command      the command object that the output will be taken from
   * @param paths        the file the output will be redirected to
   * @param commandType  the command type
   * @param fileSystem   the file system the command will be working on
   */
  public RedirectionDecorator(Command command, List<String> paths, 
      String commandType, FileSystem<Directory> fileSystem) {
    super(false);
    this.command = command;
    this.redirection = determineRedirection(paths);
    this.redirectionPath = findRedirectionPath(paths);
    this.fileSystem = fileSystem;
    this.commandType = commandType;
  }

  /**
   * Executes the redirection command, executing the command and redirecting
   * the output to the appropriate file or the JShell, depending on the
   * redirection. 
   * 
   * @param arguments  the arguments for the command
   */
  public void executeCommand(ArrayList<String> arguments) {

    String output = "";

    // if there is more than one file given, give an error
    if (this.redirectionPath.size() > 1) {
      output += this.commandType + ": too many outfiles given" + "\n";
    }

    // if there are no files given, give an error
    else if (this.redirectionPath.isEmpty() && 
        (!redirection.equals("standard"))) {
      output += this.commandType + ": no outfile given" + "\n";
    }

    // if the redirection is valid, execute the command and record the errors
    else if (validRedirection()) {
      command.executeCommand(arguments);
      if (command.getHasError()) {
        output += command.getError() + "\n";
      }
      // record the output of the redirection
      output += this.redirectOutput();

      if (output.endsWith("\n")) {
        output = output.substring(0, output.length() - 1);
      }
    }

    // give an error if the command is not redirectable
    else {
      if (this.command.isRedirectable() == false) {
        output = this.commandType + ": is not redirectable";
      } else {
        output = this.commandType + ": valid outfile not provided";
      }
    }

    this.setPrintCommand(output);
  }

  /**
   * Returns a string to determine the redirection of the command output. The
   * string append appends the output to the given file, the string overwrite
   * overwrites the output to the given file, and the string standard returns
   * the output to the shell.
   * 
   * @param paths  the command arguments
   * @return       the string to determine the redirection of the command output
   */
  private String determineRedirection(List<String> paths) {

    String redirection;

    // if the command arguments include >>, return the append string
    if (paths.contains(">>")) {
      redirection = "append";
    }

    // else if the command arguments include >, return the overwrite string
    else if (paths.contains(">")) {
      redirection = "overwrite";
    }

    // otherwise, return the standard string
    else {
      redirection = "standard";
    }

    return redirection;
  }

  /**
   * Returns the redirection paths if there are any and an empty list if there
   * is none.
   * 
   * @param paths  the input paths
   * @return       the redirection paths
   */
  private List<String> findRedirectionPath(List<String> paths) {

    List<String> path;

    // if paths has a redirection path, store it in the path variable
    if (!paths.isEmpty()) {
      path = paths.subList(1, paths.size());
    }

    // otherwise, store an empty list is the path variable
    else {
      path = paths.subList(0, paths.size());
    }

    return path;
  }

  /**
   * Returns the index of the arguments where the redirection arguments start.
   * 
   * @param arguments  the user input
   * @return           the start index of the redirection arguments
   */
  public static int redirectionStart(List<String> arguments) {

    int startIndex;

    // if the arguments contain >, give the index of >
    if (arguments.contains(">")) {
      startIndex = arguments.indexOf(">");
    }

    // if the arguments contain >>, give the index of >>
    else if (arguments.contains(">>")) {
      startIndex = arguments.indexOf(">>");
    }

    // if the arguments do no contain > or >>, the start index is the end
    else {
      startIndex = arguments.size();
    }

    return startIndex;
  }

  /**
   * Return whether or not the redirection is valid for the specific command.
   * 
   * @return  true if the redirection is valid, false if it is not
   */
  private boolean validRedirection() {

    boolean isValid;
    Path path = new Path(this.fileSystem);

    /*
     * the redirection is false if the command is not redirectable and the
     * redirection is not standard
     */
    if ((!redirection.equals("standard")) && 
        (this.command.isRedirectable() == false)) {
      isValid = false;
    }

    else {
      try {
        if ((this.redirectionPath.size() > 0) && (path.searchByPath
            (this.redirectionPath.get(0)) instanceof Directory)) {
          isValid = false;
        }
        else {
          isValid = true;
        }
      } catch (InvalidPathException e) {
        isValid = true;
      }
    }

    // otherwise, it's true
    return isValid;
  }

  /**
   * Redirect the output of the command depending on the redirection given.
   * 
   * @return  the output to the JShell.
   */
  private String redirectOutput() {

    String output = "";

    /* 
     * if the redirection is append, append the output of the command to the
     * file given
     */
    if (redirection.equals("append")) {
      append(this.command);
    }

    /*
     * if the redirection is overwrite, overwrite the output of the command to
     * the file given
     */
    else if (redirection.equals("overwrite")) {
      overwrite(this.command);
    }

    // otherwise, store it in the redirectionDecorator to return to JShell
    else {
      output = this.command.getPrintCommand();
    }

    return output;
  }

  /**
   * Overwrites the content of the file by the output of the given command.
   * 
   * @param command  the command that provides the output
   */
  private void overwrite(Command command) {

    String filePath = this.redirectionPath.get(0);
    File file = echoFile(filePath, command);

    if (file instanceof TextFile) {
      ((TextFile) file).setContents(command.getPrintCommand());
    // if file is not a text file, give an error
    } else if (file.getFileName().equals("")) {
    } else {
      command.setError(this.redirectionPath.get(0) + ": not a TextFile");
    }
  }


  /**
   * Add the string in arguments to the text file in arguments.
   * 
   * @param command the command that provides the output
   */
  private void append(Command command) {

    String filePath = this.redirectionPath.get(0);
    File file = echoFile(filePath, command);

    // if the path given is a text file, append the string to the text file
    if (file instanceof TextFile) {
      ((TextFile) file).setContents(((TextFile) file).getContents()
          + command.getPrintCommand());
    }
    // otherwise, if it is not a text file, return an error
    else if (file.getFileName().equals("")) {
    } else {
      command.setError(this.redirectionPath.get(0) + ": not a TextFile");
    }
  }

  /**
   * Finds the out file that needs to be modified by the echo command.
   * 
   * @param path     the path of the out file in the file system
   * @param command  the command that provides the output
   * @return file    the out file
   */
  private File echoFile(String path, Command command) {

    Path filePath = new Path(fileSystem);
    File file = new File();

    // find the file of the out file
    try {
      file = filePath.searchByPath(path);
    } catch (InvalidPathException e) {
      // if there is no file, and its a file name, create a text file
      if (!path.contains("/")) {
        try {
          file = new TextFile(path, fileSystem.getCurrentDirectory(), "");
          fileSystem.getCurrentDirectory().addContents((TextFile) file);
          // if the file name is invalid, give an error
        } catch (InvalidFileNameException e3) {
          command.setError(path + ": contains illegal characters");
        } catch (ExistingFileException e4) {
        }
        // if no out file given after /, give an error
      } else if (path.lastIndexOf("/") == (path.length() - 1)) {
        command.setError(path + ": no outfile given after /");
        // otherwise find the parent of the out file and create a new text file
      } else {
        String parentPath = path.substring(0, path.lastIndexOf("/"));
        String outfile = path.substring(path.lastIndexOf("/") + 1);
        try {
          // if the parent is a directory, create a text file
          File parentFile = filePath.searchByPath(parentPath);
          if (parentFile instanceof Directory) {
            file = new TextFile(outfile, (Directory) parentFile, "");
            ((Directory) parentFile).addContents((TextFile) file);
            // if parent if not a directory, give an error
          } else {
            command.setError(path + ": parent of outfile not a directory");
          }
          // if the parent doesn't exist, give an error
        } catch (InvalidPathException e2) {
          command.setError(path + ": not a valid path");
          // if the file name is invalid, give an error
        } catch (InvalidFileNameException e5) {
          command.setError(path + ": contains illegal characters");
        } catch (ExistingFileException e6) {
        }
      }
    }
    return file;
  }
}
