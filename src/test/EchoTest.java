package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.*;
import driver.*;

/**
 * Test for command Echo.
 * 
 * @author Ya-Tzu Wang
 * @author Shamayum Rashad
 */
public class EchoTest {
  MockJFileSystem fileSystem;
  Echo echo;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    echo = new Echo();
    arguments = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testEchoString() {
    arguments.add("\"content\"");
    echo.executeCommand(arguments);
    String actual = echo.getPrintCommand();
    String expected = "content";
    assertEquals(actual, expected);
  }

  @Test
  public void testEchoMissingQuotations() {
    arguments.add("content");
    echo.executeCommand(arguments);
    String actual = echo.getError();
    String expected = "content: missing quotations";
    assertEquals(expected, actual);
  }

  @Test
  public void testEchoNoArguments() {
    echo.executeCommand(arguments);
    String actual = echo.getError();
    String expected = "echo: no string provided";
    assertEquals(expected, actual);
  }

  @Test
  public void testEchoMultipleStrings() {
    arguments.add("\"content\"");
    arguments.add("\"content2\"");
    echo.executeCommand(arguments);
    String actual = echo.getError();
    String expected = "echo: too many arguments given";
    assertEquals(expected, actual);
  }

  @Test
  public void testEchoStringWithSpace() {
    arguments.add("\"echo content\"");
    echo.executeCommand(arguments);
    String actual = echo.getPrintCommand();
    String expected = "echo content";
    assertEquals(actual, expected);
  }
}
