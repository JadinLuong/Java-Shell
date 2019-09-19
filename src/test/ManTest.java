package test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import driver.Man;

/**
 * Test Cases for the Man command.
 * 
 * @author Shamayum Rashad
 *
 */
public class ManTest {

  Man man;

  /**
   * Setup method, instantiates a man class.
   */
  @Before
  public void setUp() {
    man = new Man();
  }

  /**
   * test the execute method and the output when no command is given
   */
  @Test
  public void testNoCommandGiven() {
    ArrayList<String> arguments = new ArrayList<>();
    man.executeCommand(arguments);

    assertEquals("man: no command specified", man.getError());
  }

  /**
   * test the execute method and the output when the exit command is given
   */
  @Test
  public void testExitCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("exit");
    man.executeCommand(arguments);
    String definition = "exit:\n Quit the program.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the mkdir command is given
   */
  @Test
  public void testMkdirCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("mkdir");
    man.executeCommand(arguments);
    String definition = "mkdir:\n Create directories, each of which may be "
        + "relative to the current\ndirectory or may be a full path.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the cd command is given
   */
  @Test
  public void testCdCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("cd");
    man.executeCommand(arguments);
    String definition = "cd:\n Change directory to DIR, which may be "
        + "relative to the current\ndirectory or may be a full path. .. means a"
        + " parent directory and\na . means the current directory. The "
        + "directory must be /, the\nforward slash. The foot of the file system"
        + " is a single slash: /.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the ls command is given
   */
  @Test
  public void testLsCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("ls");
    man.executeCommand(arguments);
    String definition = "ls:\n If –R is present, recursively list all "
        + "subdirectories. If no\npaths are given, print the contents "
        + "(file or directory) of the\ncurrent directory, with a new line "
        + "following each of the\ncontent (file or directory).\nOtherwise,"
        + " for each path p, the orderlisted: \n•If p specifies a file, print "
        + "p\n•If p specifies a directory, print p, a colon, then the contents"
        + " of\nthat directory, then an extra new line. \n•If p does not "
        + "exist, print a suitable message.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the pwd command is given
   */
  @Test
  public void testPwdCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("pwd");
    man.executeCommand(arguments);
    String definition = "pwd:\n Print the current working directory "
        + "(including the whole path).";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the pushd command is given
   */
  @Test
  public void testPushdCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("pushd");
    man.executeCommand(arguments);
    String definition = "pushd:\n Saves the current working directory by "
        + "pushing onto directory\nstack and then changes the new current "
        + "working directory to DIR.\nThe push must be consistent as per the "
        + "LIFO behavior of a stack.\nThe pushd command saves the old current "
        + "working directory in\ndirectory stack so that it can be returned to "
        + "at any time (via\nthe popd command). The size of the directory stack"
        + " is dynamic\nand dependent on the pushd and the popd commands.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the popd command is given
   */
  @Test
  public void testPopdCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("popd");
    man.executeCommand(arguments);
    String definition = "popd:\n Remove the top entry from the directory "
        + "stack, and cd into it.\nThe removal must be consistent as per the "
        + "LIFO behavior of a\nstack. The popd command removes the top most "
        + "directory from the\ndirectory stack and makes it the current working"
        + " directory. If there\nis no directory onto the stack, then give "
        + "appropriate error message.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the history command is given
   */
  @Test
  public void testHistoryCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("history");
    man.executeCommand(arguments);
    String definition = "history:\n This command will print out recent "
        + "commands, one command per line.\nWe can truncate the output by "
        + "specifying a number (>=0) after the command.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the cat command is given
   */
  @Test
  public void testCatCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("cat");
    man.executeCommand(arguments);
    String definition = "cat:\n Display the contents of files concatenated"
        + " in the shell.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the echo command is given
   */
  @Test
  public void testEchoCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("echo");
    man.executeCommand(arguments);
    String definition = "echo:\n If [> OUTFILE] is not provided, print "
        + "STRING on the shell.\nOtherwise, put STRING into file OUTFILE. This "
        + "creates a new file\nif OUTFILE does not exists and erases the old "
        + "contents if\nOUTFILE already exists. If >> OUTFILE is provided, "
        + "append STRING to OUTFILE.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when the man command is given
   */
  @Test
  public void testMvCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("mv");
    man.executeCommand(arguments);
    String definition = "mv:\n Move item OLDPATH to NEWPATH. Both "
        + "OLDPATH and NEWPATH maybe\nrelative to the current directory or may"
        + " be full paths. If NEWPATH\nis a directory, move the item into the"
        + " directory.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testCpCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("cp");
    man.executeCommand(arguments);
    String definition = "cp:\n Like mv, but don’t remove OLDPATH."
        + " If OLDPATH is a directory,\nrecursively copy the contents.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testGetCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("get");
    man.executeCommand(arguments);
    String definition = "get:\n URL is a web address. Retrieve the"
        + " file at that URL and add it to\nthe current working directory.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testSaveCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("save");
    man.executeCommand(arguments);
    String definition = "save:\n The above command will interact "
        + "with your real file system on\nyour computer. When the above command"
        + " is typed, you must ensure\nthat the entire state of the program is "
        + "written to the file\nFileName. The file FileName is some file that "
        + "is stored on the actual\nfilesystem of your computer.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testLoadCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("load");
    man.executeCommand(arguments);
    String definition = "load:\n When the user types in the above"
        + " command, your JShell must load\nthe contents of the FileName and"
        + " reinitialize everything that was\nsaved previously into the"
        + " FileName.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testFindCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("find");
    man.executeCommand(arguments);
    String definition = "find:\n You are now asked to implement "
        + "the find command. The syntax\nof the find command is as follows: "
        + "find path ... -type [f|d]\n-name expression.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testTreeCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("tree");
    man.executeCommand(arguments);
    String definition = "tree:\n When the user types in the tree"
        + " command you must starting, from\nthe root directory display"
        + " the entire filesystem as a tree. For every\nlevel of the tree, "
        + "you must indent by a tab character.";

    assertEquals(definition, man.getPrintCommand());
  }

  @Test
  public void testManCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("man");
    man.executeCommand(arguments);
    String definition = "man:\n Print documentation for command.";

    assertEquals(definition, man.getPrintCommand());
  }

  /**
   * test the execute method and the output when an invalid command is given
   */
  @Test
  public void testInvalidCommand() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("command");
    man.executeCommand(arguments);
    String definition = "command: not a valid command";

    assertEquals(definition, man.getError());
  }

  /**
   * test the execute method and the output when the multiple commands are
   * given
   */
  @Test
  public void testMultipleCommands() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("exit");
    arguments.add("man");
    man.executeCommand(arguments);
    String definition = "man: too many commands specified";

    assertEquals(definition, man.getError());
  }

}
