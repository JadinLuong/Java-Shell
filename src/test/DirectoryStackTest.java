package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.DirectoryStack;

public class DirectoryStackTest {

  Directory dirA;
  Directory dirB;
  Directory dirC;
  DirectoryStack dirStack;

  @Before
  public void setUp() throws Exception {
    dirA = new Directory("A", null);
    dirB = new Directory("B", null);
    dirC = new Directory("C", null);
    dirStack = new DirectoryStack();
  }

  @Test
  public void testPopEmptyStack() {
    Directory expectedOutput = null;
    Directory actualOutput = dirStack.pop();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testPushIntoEmptyStack() {
    dirStack.push(dirA);
    Directory expectedOutput = dirA;
    Directory actualOutput = dirStack.pop();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testPushMutlipleIntoEmptyStack() {
    dirStack.push(dirA);
    dirStack.push(dirA);
    dirStack.push(dirB);
    dirStack.push(dirC);
    Directory expectedOutput = dirC;
    Directory actualOutput = dirStack.pop();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testPushMultipleTimesAndCheckSecondPop() {
    dirStack.push(dirA);
    dirStack.push(dirA);
    dirStack.push(dirB);
    dirStack.push(dirC);
    dirStack.pop();
    Directory expectedOutput = dirB;
    Directory actualOutput = dirStack.pop();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetDirStackEmpty() {
    ArrayList<Directory> expectedOutput = new ArrayList<Directory>();
    ArrayList<Directory> actualOutput = dirStack.getDirStack();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testGetNonEmptyDirStack() {
    ArrayList<Directory> expectedOutput = new ArrayList<Directory>();
    expectedOutput.add(dirA);
    expectedOutput.add(dirB);
    expectedOutput.add(dirC);
    dirStack.push(dirA);
    dirStack.push(dirB);
    dirStack.push(dirC);
    ArrayList<Directory> actualOutput = dirStack.getDirStack();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSetDirStack() {
    ArrayList<Directory> expectedOutput = new ArrayList<Directory>();
    expectedOutput.add(dirA);
    expectedOutput.add(dirB);
    expectedOutput.add(dirC);
    ArrayList<Directory> newDirStack = new ArrayList<Directory>();
    newDirStack.add(dirA);
    newDirStack.add(dirB);
    newDirStack.add(dirC);
    dirStack.setDirStack(newDirStack);
    ArrayList<Directory> actualOutput = dirStack.getDirStack();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testIsEmptyOnEmptyDirectoryStack() {
    assertTrue(dirStack.isEmpty());
  }

  @Test
  public void testIsEmptyOnNonEmptyDirectoryStack() {
    dirStack.push(dirA);
    assertFalse(dirStack.isEmpty());
  }

  @Test
  public void testEqualsOnEmptyDirectoryStacks() {
    DirectoryStack newDirStack = new DirectoryStack();
    assertTrue(dirStack.equals(newDirStack));
  }

  @Test
  public void testEqualsOneEmptyStackAndOneNonEmpty() {
    DirectoryStack newDirStack = new DirectoryStack();
    dirStack.push(dirA);
    assertFalse(dirStack.equals(newDirStack));
  }

  @Test
  public void testEqualsTwoNonEmptyStackWithSimilarProperties() {
    DirectoryStack newDirStack = new DirectoryStack();
    dirStack.push(dirA);
    newDirStack.push(dirA);
    assertTrue(dirStack.equals(newDirStack));
  }

  @Test
  public void testEqualsTwoNonEmptyStackWithDifferentProperties() {
    DirectoryStack newDirStack = new DirectoryStack();
    dirStack.push(dirA);
    newDirStack.push(dirB);
    assertFalse(dirStack.equals(newDirStack));
  }
}
