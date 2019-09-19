package driver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents the command Man in the command line.
 * 
 * @author Shamayum Rashad
 *
 */
public class Man extends Command{

  // hash map used to find definitions of commands
  private static HashMap<String, String> commandDefinitions;
  // populate the map with the definitions of valid commands
  static {
    commandDefinitions = new HashMap<String, String>();
    commandDefinitions.put("exit", "exit:\n Quit the program.");
    commandDefinitions.put("mkdir", "mkdir:\n Create directories, each of "
        + "which may be relative to the current\ndirectory or may be a full "
        + "path.");
    commandDefinitions.put("cd", "cd:\n Change directory to DIR, which may be "
        + "relative to the current\ndirectory or may be a full path. .. means a"
        + " parent directory and\na . means the current directory. The "
        + "directory must be /, the\nforward slash. The foot of the file system"
        + " is a single slash: /.");
    commandDefinitions.put("ls", "ls:\n If –R is present, recursively list all "
        + "subdirectories. If no\npaths are given, print the contents "
        + "(file or directory) of the\ncurrent directory, with a new line "
        + "following each of the\ncontent (file or directory).\nOtherwise,"
        + " for each path p, the orderlisted: \n•If p specifies a file, print "
        + "p\n•If p specifies a directory, print p, a colon, then the contents"
        + " of\nthat directory, then an extra new line. \n•If p does not "
        + "exist, print a suitable message.");
    commandDefinitions.put("pwd", "pwd:\n Print the current working directory "
        + "(including the whole path).");
    commandDefinitions.put("pushd", "pushd:\n Saves the current working "
        + "directory by pushing onto directory\nstack and then changes the new"
        + " current working directory to DIR.\nThe push must be consistent as "
        + "per the LIFO behavior of a stack.\nThe pushd command saves the old "
        + "current working directory in\ndirectory stack so that it can be "
        + "returned to at any time (via\nthe popd command). The size of the "
        + "directory stack is dynamic\nand dependent on the pushd and the popd"
        + " commands.");
    commandDefinitions.put("popd", "popd:\n Remove the top entry from the "
        + "directory stack, and cd into it.\nThe removal must be consistent as"
        + " per the LIFO behavior of a\nstack. The popd command removes the "
        + "top most directory from the\ndirectory stack and makes it the "
        + "current working directory. If there\nis no directory onto the stack,"
        + " then give appropriate error message.");
    commandDefinitions.put("history", "history:\n This command will print out"
        + " recent commands, one command per line.\nWe can truncate the output"
        + " by specifying a number (>=0) after the command.");
    commandDefinitions.put("cat", "cat:\n Display the contents of files "
        + "concatenated in the shell.");
    commandDefinitions.put("echo", "echo:\n If [> OUTFILE] is not provided, "
        + "print STRING on the shell.\nOtherwise, put STRING into file OUTFILE."
        + " This creates a new file\nif OUTFILE does not exists and erases the"
        + " old contents if\nOUTFILE already exists. If >> OUTFILE is provided,"
        + " append STRING to OUTFILE.");
    commandDefinitions.put("man", "man:\n Print documentation for command.");
    commandDefinitions.put("mv", "mv:\n Move item OLDPATH to NEWPATH. Both "
        + "OLDPATH and NEWPATH maybe\nrelative to the current directory or may"
        + " be full paths. If NEWPATH\nis a directory, move the item into the"
        + " directory.");
    commandDefinitions.put("cp", "cp:\n Like mv, but don’t remove OLDPATH."
        + " If OLDPATH is a directory,\nrecursively copy the contents.");
    commandDefinitions.put("get", "get:\n URL is a web address. Retrieve the"
        + " file at that URL and add it to\nthe current working directory.");
    commandDefinitions.put("save", "save:\n The above command will interact "
        + "with your real file system on\nyour computer. When the above command"
        + " is typed, you must ensure\nthat the entire state of the program is "
        + "written to the file\nFileName. The file FileName is some file that "
        + "is stored on the actual\nfilesystem of your computer.");
    commandDefinitions.put("load", "load:\n When the user types in the above"
        + " command, your JShell must load\nthe contents of the FileName and"
        + " reinitialize everything that was\nsaved previously into the"
        + " FileName.");
    commandDefinitions.put("find", "find:\n You are now asked to implement "
        + "the find command. The syntax\nof the find command is as follows: "
        + "find path ... -type [f|d]\n-name expression.");
    commandDefinitions.put("tree", "tree:\n When the user types in the tree"
        + " command you must starting, from\nthe root directory display"
        + " the entire filesystem as a tree. For every\nlevel of the tree, "
        + "you must indent by a tab character.");
  }
  
  /**
   * Default constructor.
   */
  public Man() {
    super(true);
  }

  
  /**
   * Executes the man command, modifying the output string to the definition of
   * the command specified by arguments.
   * 
   * @param arguments  the command the definition is wanted for
   */
  public void executeCommand(ArrayList<String> arguments) {

    /* 
     * if there is only one argument and its valid, get the definition from 
     * commandDefinition
     */
    if (arguments.size() == 1 && Man.commandDefinitions.containsKey
        (arguments.get(0))) {
      this.setPrintCommand(Man.commandDefinitions.get(arguments.get(0)));
      }
    // otherwise, specify no command was entered if arguments is empty
    else if (arguments.isEmpty()) {
      this.setError("man: no command specified");
    }
    // specify there were too many commands entered if arguments has more than 1
    else if (arguments.size() > 1) {
      this.setError("man: too many commands specified");
    }
    /*
     * and if there is only one command but its not in commandDefinition,
     * specify that its not a valid command
     */
    else {
      this.setError((arguments.get(0)) + ": not a valid command");
    }
  }

  /**
   * Returns the class name.
   *
   * @return  the class name
   */
  public String toString() {

    return "Man Class Object";
  }
}
