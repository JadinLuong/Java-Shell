package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Tree;

public class TreeTest {

  MockJFileSystem fileSystem;
  Tree tree;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    tree = new Tree(fileSystem);
    arguments = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testTreeWithRootOnly() {
    String expectedOutput = "/";
    tree.executeCommand(arguments);
    String actualOutput = tree.getPrintCommand();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTreeWithOneChild() {
    fileSystem.addDirectory("/", "FirstChild");
    String expectedOutput = "/\n\tFirstChild";
    tree.executeCommand(arguments);
    String actualOutput = tree.getPrintCommand();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTreeWithRootContainingMultipleDirectory() {
    fileSystem.addDirectory("/", "FirstChild");
    fileSystem.addDirectory("/", "SecondChild");
    fileSystem.addDirectory("/", "ThirdChild");
    String expectedOutput = "/\n\tFirstChild\n\tSecondChild\n\tThirdChild";
    tree.executeCommand(arguments);
    String actualOutput = tree.getPrintCommand();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTreeFilledWithNestedDirectories() {
    fileSystem.addDirectory("/", "A");
    fileSystem.addDirectory("/", "B");
    fileSystem.addDirectory("/", "C");
    fileSystem.addDirectory("/A", "A1");
    fileSystem.addDirectory("/A/A1", "A2");
    fileSystem.addDirectory("/C", "C1");
    fileSystem.addDirectory("/C", "C2");
    String expectedOutput =
        "/\n\tA\n\t\tA1\n\t\t\tA2\n\tB\n\tC\n\t\tC1\n\t\tC2";
    tree.executeCommand(arguments);
    String actualOutput = tree.getPrintCommand();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTreeWithDirectoriesAndTextFiles() {
    fileSystem.addTextFile("/", "TextFile", "Test Content");
    fileSystem.addDirectory("/", "A");
    fileSystem.addTextFile("/A", "ATextFile", "Tree Content");
    fileSystem.addDirectory("/", "B");
    String expectedOutput = "/\n\tTextFile\n\tA\n\t\tATextFile\n\tB";
    tree.executeCommand(arguments);
    String actualOutput = tree.getPrintCommand();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testTreeWithArguments() {
    String expectedOutput =
        "Invalid syntax for tree command: tree must not take any arguments.";
    arguments.add("TreeCommandShouldFail");
    tree.executeCommand(arguments);
    String actualOutput = tree.getError();
    assertEquals(expectedOutput, actualOutput);
  }
}
