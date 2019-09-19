package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.TextFile;

public class TextFileTest {

  TextFile textfile;
  Directory parentDir;

  @Before
  public void setUp() {
    parentDir = new Directory("ParentDir", null);
    textfile = new TextFile("TextFile", parentDir, "Test Content");
  }

  @Test
  public void testGetContentsOfTextFile() {
    String expectedOutput = "Test Content";
    String actualOutput = textfile.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetContentsOfTextFile() {
    textfile.setContents("New Content");
    String expectedOutput = "New Content";
    String actualOutput = textfile.getContents();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testToStringOfTextFile() {
    String expectedOutput = "TextFile";
    String actualOutput = textfile.toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEqualsOFTextFileDifferentName() {
    TextFile newTextFile = new TextFile("Text", parentDir, "Test Content");
    assertFalse(textfile.equals(newTextFile));
  }

  @Test
  public void testEqualsOfTextFileDifferentParentDirectory() {
    Directory newParentDir = new Directory("NewParentDir", null);
    TextFile newTextFile =
        new TextFile("TextFile", newParentDir, "Test Content");
    assertFalse(textfile.equals(newTextFile));
  }

  @Test
  public void testEqualsOfTextFileDifferentContent() {
    TextFile newTextFile =
        new TextFile("TextFile", parentDir, "Different Content");
    assertFalse(textfile.equals(newTextFile));
  }

  @Test
  public void testEqualsOfTextFileSimilarTextFiles() {
    TextFile newTextFile = new TextFile("TextFile", parentDir, "Test Content");
    assertTrue(textfile.equals(newTextFile));
  }

}
