package test;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Find;

public class FindTest {

  Find find;
  MockJFileSystem fileSystem;
  ArrayList<String> arguments;

  @Before
  public void SetUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    find = new Find(fileSystem);
    arguments = new ArrayList<>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testEmptyFileSystem() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    arguments.add("\"test\"");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testOnlyPathGiven() {
    arguments.add("/");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testOnlyPathAndType() {
    arguments.add("/");
    arguments.add("-type");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testOnlyPathAndTypeWithType() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("f");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testOnlyPathAndTypeAndNameWithType() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testNameWithoutQuotes() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    arguments.add("test");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTypeNotValid() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("g");
    arguments.add("-name");
    arguments.add("\"test\"");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTypeTextFile() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    arguments.add("\"test\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\n/test\n/test1/test2/test";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTypeDirectory() {
    arguments.add("/test1");
    arguments.add("-type");
    arguments.add("d");
    arguments.add("-name");
    arguments.add("\"test1\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\ntest1/test2/test1\ntest1";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testPathInvalid() {
    arguments.add("/test5");
    arguments.add("-type");
    arguments.add("d");
    arguments.add("-name");
    arguments.add("\"test\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "/test5: directory not found\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testNameNotInPath() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    arguments.add("\"test5\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testOnePath() {
    arguments.add("/test1");
    arguments.add("-type");
    arguments.add("d");
    arguments.add("-name");
    arguments.add("\"test2\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\ntest1/test2";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultiplePaths() {
    arguments.add("/test1");
    arguments.add("/test1/test2");
    arguments.add("-type");
    arguments.add("d");
    arguments.add("-name");
    arguments.add("\"test3\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1", "test3");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\ntest1/test3\ntest1/test2/test3\ntest2/test3";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultiplePathsErrorInOne() {
    arguments.add("/test1");
    arguments.add("/test");
    arguments.add("-type");
    arguments.add("d");
    arguments.add("-name");
    arguments.add("\"test3\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1", "test3");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "/test: not a directory\ntest1/test3\ntest1/"
        + "test2/test3\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultipleNames() {
    arguments.add("/");
    arguments.add("-type");
    arguments.add("d");
    arguments.add("-name");
    arguments.add("\"test\"");
    arguments.add("\"test1\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1", "test3");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "Find: invalid syntax: valid syntax as follows: "
        + "find path ... -type [f|d] -name expression\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultiplePathsNameInOne() {
    arguments.add("/test1");
    arguments.add("/test1/test3");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    arguments.add("\"test\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1", "test3");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\ntest1/test2/test\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultiplePathsNameInNone() {
    arguments.add("/test1");
    arguments.add("/test1/test3");
    arguments.add("-type");
    arguments.add("f");
    arguments.add("-name");
    arguments.add("\"test5\"");
    fileSystem.addDirectory("/", "test1");
    fileSystem.addTextFile("/", "test", "testing");
    fileSystem.addDirectory("/test1", "test2");
    fileSystem.addDirectory("/test1", "test3");
    fileSystem.addDirectory("/test1/test2", "test3");
    fileSystem.addTextFile("/test1/test2", "test", "content");
    fileSystem.addDirectory("/test1/test2", "test1");
    find.executeCommand(arguments);
    String actualOutput = find.getError() + "\n" + find.getPrintCommand();
    String expectedOutput = "\n\n";

    assertEquals(expectedOutput, actualOutput);
  }
}
