package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.*;

/**
 * Test for command move.
 * 
 * @author Ya-Tzu Wang
 */
public class MoveTest {
  MockJFileSystem fileSystem;
  Move move;
  MockPath mockPath;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    move = new Move(fileSystem);
    mockPath = new MockPath(fileSystem);
    fileSystem.addDirectory("/", "dir1");
    fileSystem.addTextFile("/dir1", "text", "Test Content");
    fileSystem.addDirectory("/", "dir2");
    fileSystem.addTextFile("/dir2", "text2", "Another Test Content");
    arguments = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testMoveSameFiles() {
    arguments.add("/dir1/text");
    arguments.add("/dir1/text");
    move.executeCommand(arguments);
    String expected = "mv: text and text are the same file";
    String actual = move.getError();
    assertEquals(expected, actual);
  }

  @Test
  public void testMoveTextFileToAnotherTextFile() throws InvalidPathException {
    arguments.add("/dir1/text");
    arguments.add("/dir2/text2");
    move.executeCommand(arguments);
    // make sure text2's content has been overwritten with text1's
    TextFile text2 = (TextFile) mockPath.searchByPath("/dir2/text2");
    String expected = "Test Content";
    String actual = text2.getContents();
    assertEquals(expected, actual);
  }

  @Test
  public void testMoveTextFileToDirectory() throws InvalidPathException {
    arguments.add("/dir1/text");
    arguments.add("/dir2");
    move.executeCommand(arguments);
    // test will fail if text is not moved to dir2
    mockPath.searchByPath("/dir2/text");
  }

  @Test
  public void testMoveDirectoryToAnotherDirectory()
      throws InvalidPathException {
    arguments.add("/dir1");
    arguments.add("/dir2");
    move.executeCommand(arguments);
    // test will fail if text is not moved to dir2
    mockPath.searchByPath("/dir2/dir1/text");
  }

  @Test
  public void testMoveDirectoryToTextFile() {
    arguments.add("/dir1");
    arguments.add("/dir2/text2");
    move.executeCommand(arguments);
    String expected =
        "mv: cannot overwrite non-directory text2 with directory dir1";
    String actual = move.getError();
    assertEquals(expected, actual);
  }
}
