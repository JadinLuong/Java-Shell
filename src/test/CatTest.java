package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.*;
import driver.*;

/**
 * Test for Cat class.
 * 
 * @author Ya-Tzu Wang
 */
public class CatTest {
  MockJFileSystem fileSystem;
  Cat cat;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    cat = new Cat(fileSystem);
    fileSystem.addDirectory("/", "dir1");
    fileSystem.addTextFile("/dir1", "text", "Test Content");
    fileSystem.addTextFile("/dir1", "text2", "Another Test Content");
    arguments = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testCatNoFiles() {
    cat.executeCommand(arguments);
    String actual = cat.getPrintCommand();
    String expected = "";
    assertEquals(expected, actual);
  }

  @Test
  public void testCatSingleTextFile() {
    arguments.add("/dir1/text");
    cat.executeCommand(arguments);
    String actual = cat.getPrintCommand();
    String expected = "Test Content";
    assertEquals(expected, actual);
  }

  @Test
  public void testCatMultipleFiles() {
    arguments.add("/dir1/text");
    arguments.add("/dir1/text2");
    cat.executeCommand(arguments);
    String actual = cat.getPrintCommand();
    String expected = "Test Content";
    for (int i = 0; i < 4; i++) {
      expected += System.lineSeparator();
    }
    expected += "Another Test Content";
    assertEquals(expected, actual);
  }

  @Test
  public void testCatDirectory() {
    String DirectoryPath = "/dir1";
    arguments.add(DirectoryPath);
    cat.executeCommand(arguments);
    String actual = cat.getError();
    String expected = "cat: dir1: Is a directory";
    assertEquals(expected, actual);
  }


  @Test
  public void testCatNonexistentFile() {
    String fakePath = "/dir1/2dir/22";
    arguments.add(fakePath);
    cat.executeCommand(arguments);
    String actual = cat.getError();
    String expected = "cat: " + fakePath + ": No such file or directory";
    assertEquals(actual, expected);
  }
}
