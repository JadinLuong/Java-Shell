package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.*;
import driver.*;

/**
 * Test for methods in Path class such as getPath and SearchByPath.
 * 
 * @author Ya-Tzu Wang
 */
public class PathTest {
  MockJFileSystem fileSystem;
  Path path;
  Directory dir1;
  TextFile text;

  @Before
  public void setUp() throws InvalidPathException {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    path = new Path(fileSystem);
    fileSystem.addDirectory("/", "dir1");
    fileSystem.addTextFile("/dir1", "text", "content");
    dir1 = (Directory) path.searchByPath("/dir1");
    text = (TextFile) path.searchByPath("/dir1/text");
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testGetPathWithTextFile() {
    String actual = path.getPath(text);
    String expected = "/dir1/text";
    assertEquals(actual, expected);
  }

  @Test
  public void testGetPathWithDirectory() {
    String actual = path.getPath(dir1);
    String expected = "/dir1";
    assertEquals(actual, expected);
  }

  @Test
  public void testSearchByPathWithPathOfTextFile() {
    File expected = text;
    try {
      File actual = path.searchByPath(path.getPath(text));
      assertEquals(actual, expected);
    } catch (Exception e) {
    }
  }

  @Test
  public void testSearchByPathWithPathOfDirectory() {
    File expected = dir1;
    try {
      File actual = path.searchByPath(path.getPath(dir1));
      assertEquals(actual, expected);
    } catch (Exception e) {
    }
  }
}
