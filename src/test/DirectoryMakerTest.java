package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import driver.Directory;
import driver.DirectoryMaker;
import driver.InvalidPathException;

public class DirectoryMakerTest {

  MockJFileSystem fileSystem;
  DirectoryMaker directoryMaker;
  MockPath path;
  Directory dir1;
  Directory dir2;
  Directory dir3;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    path = new MockPath(fileSystem);
    fileSystem.addDirectory("/", "TestDir1");
    fileSystem.addDirectory("/", "TestDir2");
    fileSystem.addDirectory("/TestDir1", "TestDir3");
    try {
      dir1 = (Directory) path.searchByPath("/TestDir1");
      dir2 = (Directory) path.searchByPath("/TestDir2");
      dir3 = (Directory) path.searchByPath("/TestDir1/TestDir3");
    } catch (InvalidPathException e) {
      // Do nothing
    }
    directoryMaker = new DirectoryMaker(fileSystem);
    arguments = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testNoArgumentsGiven() {
    int expectedOutput = 2;
    directoryMaker.executeCommand(arguments);
    int actualOutput = fileSystem.getCurrentDirectory().getContents().size();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testOneArgumentUsingRelativePath() {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    arguments.add("TestFolder");
    directoryMaker.executeCommand(arguments);
    boolean correctDir =
        (currentDirectory.getContents().size() == 3) && currentDirectory
            .getContents().get(2).getFileName().equals("TestFolder");
    assertTrue(correctDir);
  }

  @Test
  public void testOneArgumentUsingFullPath() {
    fileSystem.setCurrentDirectory(dir3);
    arguments.add("/TestDir1/TestDir3/TestDir4");
    directoryMaker.executeCommand(arguments);
    boolean correctDirectory = (dir3.getContents().size() == 1)
        && dir3.getContents().get(0).getFileName().equals("TestDir4");
    assertTrue(correctDirectory);
  }

  @Test
  public void testMultipleArgsUsingRelativePath() {
    fileSystem.setCurrentDirectory(dir1);
    arguments.add("TestDir3/TestDir4");
    arguments.add("TestDir5");
    directoryMaker.executeCommand(arguments);
    boolean correctDirectory =
        (dir1.getContents().size() == 2) && (dir3.getContents().size() == 1)
            && dir3.getContents().get(0).getFileName().equals("TestDir4")
            && dir1.getContents().get(1).getFileName().equals("TestDir5");
    assertTrue(correctDirectory);
  }

  @Test
  public void testMultipleArgsUsingAbsolutePath() {
    Directory currentDir = fileSystem.getCurrentDirectory();
    arguments.add("/TestDir1/TestDir3/TestDir4");
    arguments.add("/TestDir5");
    directoryMaker.executeCommand(arguments);
    boolean correctDirectory = (currentDir.getContents().size() == 3)
        && (dir3.getContents().size() == 1)
        && dir3.getContents().get(0).getFileName().equals("TestDir4")
        && currentDir.getContents().get(2).getFileName().equals("TestDir5");
    assertTrue(correctDirectory);
  }

  @Test
  public void testDirectoryWithIllegalCharacter() {
    fileSystem.setCurrentDirectory(dir1);
    arguments.add("Illegal!@#@!@!#Folder");
    directoryMaker.executeCommand(arguments);
    boolean isDirectoryCreated = (dir1.getContents().size() == 1);
    assertTrue(isDirectoryCreated);
  }

  @Test
  public void testDirectoryAlreadyExists() {
    arguments.add("/TestDir1/TestDir3");
    directoryMaker.executeCommand(arguments);
    boolean isDirectoryCreated = (dir1.getContents().size() == 1);
    assertTrue(isDirectoryCreated);
  }

  @Test
  public void testIllegalDirectoryAndSatisfactoryDirectory() {
    arguments.add("/TestDir1/TestDir3/WorkingFolder");
    arguments.add("IllegalFolder*&()");
    directoryMaker.executeCommand(arguments);
    boolean executionsPassed = (dir3.getContents().size() == 1)
        && dir3.getContents().get(0).getFileName().equals("WorkingFolder")
        && (fileSystem.getCurrentDirectory().getContents().size() == 2);
    assertTrue(executionsPassed);
  }

  @Test
  public void testExistingDirectoryAndNonExistingDirectory() {
    arguments.add("TestDir2/NonExistingFolder");
    arguments.add("/TestDir1");
    directoryMaker.executeCommand(arguments);
    boolean executionsPassed =
        (fileSystem.getCurrentDirectory().getContents().size() == 2)
            && (dir2.getContents().size() == 1) && dir2.getContents().get(0)
                .getFileName().equals("NonExistingFolder");
    assertTrue(executionsPassed);
  }
}
