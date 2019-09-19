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
import driver.Popd;

/**
 * Test Cases for the Popd command.
 * 
 * @author Shamayum Rashad
 *
 */
public class PopdTest {

  Popd popd;
  DirectoryStack directoryStack;
  FileSystem<Directory> fileSystem;

  @Before
  public void SetUp() {
    fileSystem = JFileSystem.createInstanceOfJFileSystem();
    popd = new Popd(fileSystem);
    directoryStack = ((JFileSystem) fileSystem).getDirectoryStack();
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
  public void testStackEmptyCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory beforePop = fileSystem.getCurrentDirectory();
    popd.executeCommand(arguments);

    assertEquals(beforePop, fileSystem.getCurrentDirectory());
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack is empty and no arguments are given
   */
  @Test
  public void testStackEmptyOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    popd.executeCommand(arguments);
    String expectedOutput = "Popd: Directory Stack is empty";
    String actualOutput = popd.getError();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has one directory and no arguments are given
   */
  @Test
  public void testOneDirectoryInStackCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    popd.executeCommand(arguments);
    Directory expectedOutput = rootChild;
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(expectedOutput, actualOutput);
  }
  
  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has one directory and no arguments are given
   */
  @Test
  public void testOneDirectoryInStackOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    popd.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = popd.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories and no arguments are given
   */
  @Test
  public void testMultipleDirectoriesInStackCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    popd.executeCommand(arguments);
    Directory expectedOutput = test1Child;
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and no arguments are given
   */
  @Test
  public void testMultipleDirectoriesInStackOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    popd.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = popd.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories and an argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackWithArgumentCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory beforePop = fileSystem.getCurrentDirectory();
    arguments.add("test1");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    popd.executeCommand(arguments);
    Directory expectedOutput = beforePop;
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and an argument is given
   */
  @Test
  public void testMultipleDirectoriesInStackWithArgumentOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    popd.executeCommand(arguments);
    String expectedOutput = "test1: arguments given for popd";
    String actualOutput = popd.getError();

    assertEquals(expectedOutput, actualOutput);
  }
  
  /**
   * test the execute method and compare the current directory when the 
   * directory stack has multiple directories and multiple arguments are given
   */
  @Test
  public void testMultipleDirectoriesWithMultipleArgumentsCurrentDirectory() {
    ArrayList<String> arguments = new ArrayList<String>();
    Directory beforePop = fileSystem.getCurrentDirectory();
    arguments.add("test1");
    arguments.add("/test1/test2");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    popd.executeCommand(arguments);
    Directory expectedOutput = beforePop;
    Directory actualOutput = fileSystem.getCurrentDirectory();
 
    assertEquals(expectedOutput, actualOutput);
  }

  /**
   * test the execute method and compare the jshell output when the 
   * directory stack has multiple directories and multiple arguments are given
   */
  @Test
  public void testMultipleDirectoriesWithMultipleArgumentsOutput() {
    ArrayList<String> arguments = new ArrayList<String>();
    arguments.add("test1");
    arguments.add("/test1/test2");
    Directory rootChild = new Directory
        ("test1", fileSystem.getCurrentDirectory());
    Directory test1Child = new Directory("test2", rootChild);
    try {
      fileSystem.getCurrentDirectory().addContents(rootChild);
      rootChild.addContents(test1Child);
    } catch (ExistingFileException e) {
    } catch (InvalidFileNameException e2) {
    }
    directoryStack.push(rootChild);
    directoryStack.push(test1Child);
    popd.executeCommand(arguments);
    String expectedOutput = "test1, /test1/test2: arguments given for popd";
    String actualOutput = popd.getError();

    assertEquals(expectedOutput, actualOutput);
  }
}
