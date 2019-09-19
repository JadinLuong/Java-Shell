package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import driver.Directory;
import driver.FileSystem;
import driver.History;

/**
 * Test Cases for the History command.
 * 
 * @author Shamayum Rashad
 *
 */
public class HistoryTest {

  History history;
  FileSystem<Directory> fileSystem;
  ArrayList<String> arguments = new ArrayList<>();

  /**
   * Setup method, instantiates a file system history class.
   */
  @Before
  public void SetUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    history = new History(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testNoHistoryAndNoArguments() {
    ArrayList<String> arguments = new ArrayList<>();
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testNoHistoryAndArgumentIsHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("0");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testNoHistoryAndArgumentIsGreaterThanHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("2");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testNoHistoryAndArgumentIsFloat() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("0.5");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "0.5: not a positive integer";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testNoHistoryAndArgumentIsString() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("show");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "show: not a positive integer";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testNoHistoryAndMultipleArguments() {
    ArrayList<String> arguments = new ArrayList<>();
    arguments.add("1");
    arguments.add("2");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "1, 2: more than one argument given";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testOneHistoryAndNoArguments() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory("man cd");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "1. man cd";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testOneHistoryAndArgumentIsHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory("    man     cd  ");
    arguments.add("1");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "1.     man     cd  ";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testOneHistoryAndArgumentIsGreaterThanHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    arguments.add("3");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "1.  man   ls cd  ";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testOneHistoryAndArgumentIsFloat() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    arguments.add("1.5");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "1.5: not a positive integer";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testOneHistoryAndArgumentIsString() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    arguments.add("show");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "show: not a positive integer";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testOneHistoryAndMultipleArguments() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    arguments.add("2");
    arguments.add("s");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "2, s: more than one argument given";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndNoArguments() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory("man cd");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "1. man cd\n2.      echo \"random word and \" "
        + "testfile";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndArgumentIsHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory("    man     cd  ");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    arguments.add("2");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "1.     man     cd  \n2.      echo \"random word "
        + "and \" testfile";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndArgumentIsGreaterThanHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    arguments.add("4");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "1.  man   ls cd  \n2.      echo \"random word and "
        + "\" testfile";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndArgumentIsLessThanHistorySize() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    arguments.add("1");
    history.executeCommand(arguments);
    String actualOutput = history.getPrintCommand();
    String expectedOutput = "2.      echo \"random word and \" testfile";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndArgumentIsFloat() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    arguments.add("0.5");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "0.5: not a positive integer";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndArgumentIsString() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    arguments.add("show");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "show: not a positive integer";

    assertEquals(actualOutput, expectedOutput);
  }

  @Test
  public void testMultipleHistoryAndMultipleArguments() {
    ArrayList<String> arguments = new ArrayList<>();
    fileSystem.addToHistory(" man   ls cd  ");
    fileSystem.addToHistory("     echo \"random word and \" testfile");
    arguments.add("s");
    arguments.add("0.3");
    history.executeCommand(arguments);
    String actualOutput = history.getError();
    String expectedOutput = "s, 0.3: more than one argument given";

    assertEquals(actualOutput, expectedOutput);
  }
}
