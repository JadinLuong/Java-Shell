package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.*;
import driver.*;

/**
 * Test for command cat.
 * 
 * @author Ya-Tzu Wang
 */
public class CopyTest {
  MockJFileSystem fileSystem;
  Copy copy;
  MockPath mockPath;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    copy = new Copy(fileSystem);
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
  public void testCopySameFiles() {
    arguments.add("/dir1/text");
    arguments.add("/dir1/text");
    copy.executeCommand(arguments);
    String expected = "cp: text and text are the same file";
    String actual = copy.getError();
    assertEquals(expected, actual);
  }

  @Test
  public void testCopyTextFileToAnotherTextFile() throws InvalidPathException {
    arguments.add("/dir1/text");
    arguments.add("/dir2/text2");
    copy.executeCommand(arguments);
    TextFile text = (TextFile) mockPath.searchByPath("/dir1/text");
    TextFile text2 = (TextFile) mockPath.searchByPath("/dir2/text2");
    String expected = text.getContents();
    String actual = text2.getContents();
    assertEquals(expected, actual);
  }

  @Test
  public void testCopyTextFileToDirectory() throws InvalidPathException {
    arguments.add("/dir1/text");
    arguments.add("/dir2");
    copy.executeCommand(arguments);
    // test will fail if text is not copied to dir2
    mockPath.searchByPath("/dir2/text");
  }

  @Test
  public void testCopyDirectoryToAnotherDirectory()
      throws InvalidPathException {
    arguments.add("/dir1");
    arguments.add("/dir2");
    copy.executeCommand(arguments);
    // test will fail if text is not copied to dir2
    mockPath.searchByPath("/dir2/dir1/text");
  }

  @Test
  public void testCopyDirectoryToTextFile() {
    arguments.add("/dir1");
    arguments.add("/dir2/text2");
    copy.executeCommand(arguments);
    String expected =
        "cp: cannot overwrite non-directory text2 with directory dir1";
    String actual = copy.getError();
    assertEquals(expected, actual);
  }
}
