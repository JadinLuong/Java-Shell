package test;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.DirectoryStack;
import driver.ExistingFileException;
import driver.FileSystem;
import driver.InvalidFileNameException;
import driver.JFileSystem;
import driver.Pushd;
import driver.TextFile;

/**
 * Test Cases for the Pushd command.
 * 
 * @author Shamayum Rashad
 *
 */
public class PushdTest {

  Pushd pushd;
  DirectoryStack directoryStack;
  FileSystem<Directory> fileSystem;

  @Before
  public void SetUp() {
    fileSystem = JFileSystem.createInstanceOfJFileSystem();
    pushd = new Pushd(fileSystem);
    directoryStack = new DirectoryStack();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack is empty and no arguments are given
   */
  @Test
  public void testStackEmptyNoArgumentsCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory beforePush = fileSystem.getCurrentDirectory();
    pushd.executeCommand(arguments);

    assertEquals(beforePush, fileSystem.getCurrentDirectory());
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack is empty and no arguments are given
   */
  @Test
  public void testStackEmptyNoArgumentsOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    pushd.executeCommand(arguments);
    String expectedOutput = "Pushd: no directory given";
    String actualOutput = pushd.getError();

    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories in it and no arguments are given
   */
  @Test
  public void testMultipleDirectoriesInStackNoArgumentsCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory beforePush = fileSystem.getCurrentDirectory();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test1Child2 = new Directory("test3", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      rootChild.addContents(test1Child2);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(beforePush, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories in it and no arguments are given
   */
  @Test
  public void testMultipleDirectoriesInStackNoArgumentsOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test1Child2 = new Directory("test3", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      rootChild.addContents(test1Child2);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "Pushd: no directory given";
    String actualOutput = pushd.getError();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack is empty and a full path argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackFullPathCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("/test1/test3");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test1Child2 = new Directory("test3", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      rootChild.addContents(test1Child2);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(test1Child2, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and a full path argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackFullPathOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1/test3");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test1Child2 = new Directory("test3", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      rootChild.addContents(test1Child2);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = pushd.getPrintCommand();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories in it and a relative path
   * argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackrelativePathCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1/test2/test3");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(test2Child1, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has directories and a relative path argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackRelativePathOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1/test2/test3");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = pushd.getPrintCommand();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories in it and a directory name
   * argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackDirectoryNameCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(rootChild, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and a directory name argument
   * is given
   */
  @Test
  public void testMultipleDirectoriesInStackDirectoryNameOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = pushd.getPrintCommand();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories in it and a text file argument
   * is given
   */
  @Test
  public void testMultipleDirectoriesInStackTextFileNameCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test4");
    Directory beforePush = fileSystem.getCurrentDirectory();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    TextFile testFile = new TextFile
        ("test4", fileSystem.getCurrentDirectory(), "");
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
      fileSystem.getCurrentDirectory().addContents(testFile);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(beforePush, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and a text file argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackTextFileNameOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test4");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    TextFile testFile = new TextFile
        ("test4", fileSystem.getCurrentDirectory(), "");
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
      fileSystem.getCurrentDirectory().addContents(testFile);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "test4: not a directory";
    String actualOutput = pushd.getError();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories and multiple arguments are given
   */
  @Test
  public void testMultipleDirectoriesInStackMultipleArgumentsCurrentDirectory()
  {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1");
    arguments.add("test1/test2");
    Directory beforePush = fileSystem.getCurrentDirectory();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(beforePush, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and multiple arguments are given
   */
  @Test
  public void testMultipleDirectoriesInStackMultipleArgumentsOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1");
    arguments.add("test1/test2");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "test1, test1/test2: too many directories given";
    String actualOutput = pushd.getError();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories and an invalid path is given
   */
  @Test
  public void testMultipleDirectoriesInStackInvalidPathCurrentDirectory()
  {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1/test5");
    Directory beforePush = fileSystem.getCurrentDirectory();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(beforePush, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and an invalid path is given
   */
  @Test
  public void testMultipleDirectoriesInStackInvalidArgumentsOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1/test5");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    Directory test2Child1 = new Directory("test3", test1Child);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
      test1Child.addContents(test2Child1);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    pushd.executeCommand(arguments);
    String expectedOutput = "test1/test5: No such file or directory";
    String actualOutput = pushd.getError();
 
    assertEquals(expectedOutput, actualOutput);
  }
}
