package test;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.FileSystem;
import driver.RedirectionDecorator;

public class RedirectionDecoratorTest {

  FileSystem<Directory> fileSystem;
  ArrayList<String> arguments;
  MockCommand command;
  ArrayList<String> paths;

  @Before
  public void SetUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    arguments = new ArrayList<>();
    command = new MockCommand();
    paths = new ArrayList<>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testExecuteCommandMultipleOutfile() {
    arguments.add("cp");
    paths.add(">");
    paths.add("path");
    paths.add("path2");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "man: too many outfiles given\n";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testExecuteCommandNoOutfileAppend() {
    arguments.add("cp");
    paths.add(">>");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "man: no outfile given\n";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testExecuteCommandNoOutfileOverwrite() {
    arguments.add("cp");
    paths.add(">");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "man: no outfile given\n";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testExecuteCommandDirectoryOutfile() {
    ((MockJFileSystem) fileSystem).addDirectory("/", "test");
    arguments.add("cp");
    paths.add(">");
    paths.add("test");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "man: valid outfile not provided";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testExecuteCommandAppend() {
    arguments.add("cp");
    paths.add(">>");
    paths.add("path");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testExecuteCommandOverwrite() {
    arguments.add("cp");
    paths.add(">");
    paths.add("path");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testExecuteCommandStandard() {
    arguments.add("cp");
    RedirectionDecorator test = new RedirectionDecorator
        (command, paths, "man", fileSystem);
    test.executeCommand(arguments);
    String expectedOutput = "[cp]";
    String actualOutput = test.getPrintCommand();

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRedirectionStartOverwriteIndex() {
    arguments.add("man");
    arguments.add("cd");
    arguments.add(">");
    arguments.add("path");
    int actualOutput = RedirectionDecorator.redirectionStart(arguments);
    int expectedOutput = 2;

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRedirectionStartAppendIndex() {
    arguments.add("man");
    arguments.add("cd");
    arguments.add(">>");
    arguments.add("path");
    int actualOutput = RedirectionDecorator.redirectionStart(arguments);
    int expectedOutput = 2;

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRedirectionStartNoAppendOverwriteIndex() {
    arguments.add("man");
    arguments.add("cd");
    arguments.add("path");
    arguments.add("path2");
    int actualOutput = RedirectionDecorator.redirectionStart(arguments);
    int expectedOutput = 4;

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testRedirectionStartEmpty() {
    int actualOutput = RedirectionDecorator.redirectionStart(arguments);
    int expectedOutput = 0;

    assertEquals(expectedOutput, actualOutput);
  }
}
