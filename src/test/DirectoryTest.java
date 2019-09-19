package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.ExistingFileException;
import driver.File;
import driver.TextFile;
import driver.InvalidFileNameException;

public class DirectoryTest {

  Directory root;
  Directory dirA;
  Directory dirB;
  Directory dirC;
  TextFile textA;

  @Before
  public void setUp() {
    root = new Directory("root", null);
    dirA = new Directory("dirA", root);
    dirB = new Directory("dirB", root);
    dirC = new Directory("dirC", root);
    textA = new TextFile("TextFile", root, "Test");
  }

  @Test
  public void testGetContentOfDirectoryWithNoContent() {
    ArrayList<File> expectedOutput = new ArrayList<File>();
    ArrayList<File> actualOutput = root.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetContentOfDirectoryWithContent()
      throws InvalidFileNameException, ExistingFileException {
    ArrayList<File> expectedOutput = new ArrayList<File>();
    expectedOutput.add(dirA);
    expectedOutput.add(dirB);
    expectedOutput.add(dirC);
    root.addContents(dirA);
    root.addContents(dirB);
    root.addContents(dirC);
    ArrayList<File> actualOutput = root.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddAnotherDirectory()
      throws InvalidFileNameException, ExistingFileException {
    ArrayList<File> expectedOutput = new ArrayList<File>();
    expectedOutput.add(dirA);
    root.addContents(dirA);
    ArrayList<File> actualOutput = root.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddTextFileAsRootContent()
      throws InvalidFileNameException, ExistingFileException {
    ArrayList<File> expectedOutput = new ArrayList<File>();
    expectedOutput.add(textA);
    root.addContents(textA);
    ArrayList<File> actualOutput = root.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetContents()
      throws InvalidFileNameException, ExistingFileException {
    ArrayList<File> expectedOutput = new ArrayList<File>();
    expectedOutput.add(dirA);
    expectedOutput.add(dirB);
    root.setContents(expectedOutput);
    ArrayList<File> actualOutput = root.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testToStringOfDirectoryWithJustRoot() {
    String expectedOutput = "root";
    String actualOutput = root.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testToStringOfDirectoryWithMultipleContent()
      throws InvalidFileNameException, ExistingFileException {
    String expectedOutput = "root\n\tdirA\n\tdirB\n\tdirC";
    root.addContents(dirA);
    root.addContents(dirB);
    root.addContents(dirC);
    String actualOutput = root.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEqualsDirectoryWithDifferentName() {
    assertFalse(dirA.equals(dirB));
  }

  @Test
  public void testEqualsDirectoryWithDifferentParentDirectory() {
    Directory difParent = new Directory("dirA", dirC);
    assertFalse(dirA.equals(difParent));
  }

  @Test
  public void testEqualsDirectoryWithDifferentContent()
      throws InvalidFileNameException, ExistingFileException {
    dirA.addContents(dirC);
    Directory dirAFake = new Directory("dirA", root);
    assertFalse(dirA.equals(dirAFake));
  }

  @Test
  public void testEqualsCompareRootDirectoryWithChildren() {
    assertFalse(root.equals(dirA));
  }

  @Test
  public void testEqualsDirectoryWithItself() {
    assertTrue(root.equals(root));
  }

  @Test
  public void testEqualsDirectoryWithSimilarProperties() {
    Directory newDirectory = new Directory("dirA", root);
    assertTrue(dirA.equals(newDirectory));
  }

}
