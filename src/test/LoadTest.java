package test;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Load;
import driver.Directory;
import driver.FileSystem;
import driver.FileSystemSaver;

public class LoadTest {

  FileSystem<Directory> fileSystem;
  Load load;
  ArrayList<String> arguments;
  FileSystemSaver mockSaver;

  @Before
  public void SetUp() {
    this.fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    mockSaver = new MockJFileSystemSaver();
    load = new Load(fileSystem, mockSaver);
    arguments = new ArrayList<>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testNoFileGiven() {
    load.executeCommand(arguments);
    String actualOutput = load.getError() + "\n" + load.getPrintCommand();
    String expectedOutput = "load: no file given\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testLoadNotFirstCommand() {
    fileSystem.addToHistory("first command");
    fileSystem.addToHistory("load fileName");
    arguments.add("fileName");
    load.executeCommand(arguments);
    String actualOutput = load.getError() + "\n" + load.getPrintCommand();
    String expectedOutput = "load: disabled "
        + "(load command has to be first command used)\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMultipleFilesGiven() {
    fileSystem.addToHistory("load fileName");
    arguments.add("fileName");
    arguments.add("fileName2");
    load.executeCommand(arguments);
    String actualOutput = load.getError() + "\n" + load.getPrintCommand();
    String expectedOutput = "load: too many files given\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testValidFileGiven() {
    fileSystem.addToHistory("load fileName");
    arguments.add("fileName");
    load.executeCommand(arguments);
    String actualOutput = load.getError() + "\n" + load.getPrintCommand();
    String expectedOutput = "\n";

    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testFileGivenNotFound() {
    fileSystem.addToHistory("load FileNotFoundException");
    arguments.add("FileNotFoundException");
    load.executeCommand(arguments);
    String actualOutput = load.getError() + "\n" + load.getPrintCommand();
    String expectedOutput = "FileNotFoundException: file not found\n";

    assertEquals(expectedOutput, actualOutput);
  }
}
