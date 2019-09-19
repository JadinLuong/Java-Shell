package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.File;
import driver.JFileSystem;

public class JFileSystemTest {

  JFileSystem fileSystem;
  Directory dirA;
  Directory dirB;
  Directory dirC;
  Directory dirA1;
  Directory dirA2;
  Directory dirB1;

  @Before
  public void setUp() throws Exception {
    fileSystem = JFileSystem.createInstanceOfJFileSystem();

    dirA = new Directory("A", fileSystem.getRoot());
    fileSystem.getRoot().addContents(dirA);

    dirB = new Directory("B", fileSystem.getRoot());
    fileSystem.getRoot().addContents(dirB);

    dirC = new Directory("C", fileSystem.getRoot());
    fileSystem.getRoot().addContents(dirC);

    dirA1 = new Directory("A1", dirA);
    dirA.addContents(dirA1);

    dirA2 = new Directory("A2", dirA);
    dirA.addContents(dirA2);

    dirB1 = new Directory("B1", dirB);
    dirB.addContents(dirB1);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testGetRoot() {
    String expectedOutput = "/";
    String actualOutput = fileSystem.getRoot().getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetRootToARootWithNoChildDirectories() {
    Directory newRoot = new Directory("/", null);
    fileSystem.setRoot(newRoot);
    ArrayList<File> emptyContent = new ArrayList<File>();
    assertTrue(fileSystem.getRoot().getFileName() == "/"
        && fileSystem.getRoot().getContents().equals(emptyContent));
  }

  @Test
  public void testGetCurrentDirectory() {
    Directory expectedOutput = fileSystem.getRoot();
    Directory actualOutput = fileSystem.getCurrentDirectory();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetCurrentDirectory() {
    Directory expectedOutput = dirA;
    fileSystem.setCurrentDirectory(dirA);
    Directory actualOutput = fileSystem.getCurrentDirectory();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetEmptyCommandHistory() {
    ArrayList<String> expectedOutput = new ArrayList<String>();
    ArrayList<String> actualOutput = fileSystem.getCommandHistory();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddToCommandHistory() {
    ArrayList<String> expectedOutput = new ArrayList<String>();
    expectedOutput.add("mkdir directoryA");
    ArrayList<String> actualOutput = fileSystem.getCommandHistory();
    fileSystem.addToHistory("mkdir directoryA");
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetCommandHistory() {
    ArrayList<String> expectedOutput = new ArrayList<String>();
    expectedOutput.add("mkdir directoryA");
    expectedOutput.add("ls");
    expectedOutput.add("tree");
    fileSystem.setCommandHistory(expectedOutput);
    ArrayList<String> actualOutput = fileSystem.getCommandHistory();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testFileSystemToString() {
    String expectedOutput = "/\n\tA\n\t\tA1\n\t\tA2\n\tB\n\t\tB1\n\tC";
    String actualOutput = fileSystem.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testFileSystemNextIteratorStartingFromRoot() {
    Directory expectedOutput = dirA;
    Directory actualOutput = (Directory) fileSystem.next();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testFileSystemNextIteratorAtLastDirectory() {
    fileSystem.setCurrentDirectory(dirC);
    Directory expectedOutput = null;
    Directory actualOutput = (Directory) fileSystem.next();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testFileSystemHasNextAtRoot() {
    assertTrue(fileSystem.hasNext());
  }

  @Test
  public void testFileSystemHasNextAtLastDirectory() {
    fileSystem.setCurrentDirectory(dirC);
    assertFalse(fileSystem.hasNext());
  }
}
