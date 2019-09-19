package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.File;

public class FileTest {

  File emptyFile;
  File file;
  Directory parentDir;

  @Before
  public void setUp() {
    emptyFile = new File();
    parentDir = new Directory("Parent", null);
    file = new File("TestFile", parentDir);
  }

  @Test
  public void testGetFileNameEmptyFile() {
    String expectedOutput = "";
    String actualOutput = emptyFile.getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetFileNameFileWithContent() {
    String expectedOutput = "TestFile";
    String actualOutput = file.getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetFileNameEmptyFile() {
    String expectedOutput = "NewName";
    emptyFile.setFileName("NewName");
    String actualOutput = emptyFile.getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetFileNameFileWithContent() {
    String expectedOutput = "OverwrittenName";
    file.setFileName("OverwrittenName");
    String actualOutput = file.getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetParentDirectoryOfEmptyFile() {
    Directory expectedOutput = null;
    Directory actualOutput = emptyFile.getParentDir();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetParentDirectoryOfFileWithContent() {
    String expectedOutput = "Parent";
    String actualOutput = file.getParentDir().getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetParentDirectoryOfEmptyFile() {
    Directory newDirectory = new Directory("NewParent", null);
    String expectedOutput = "NewParent";
    emptyFile.setParentDir(newDirectory);
    String actualOutput = emptyFile.getParentDir().getFileName();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetParentDirectoryOfFileWithContent() {
    Directory newDirectory = new Directory("NewDirectory", null);
    String expectedOutput = "NewDirectory";
    file.setParentDir(newDirectory);
    String actualOutput = file.getParentDir().getFileName();
    assertEquals(expectedOutput, actualOutput);
  }
}
