package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.*;
import driver.*;

/**
 * Test for pwd.
 * 
 * @author Ya-Tzu Wang
 * @author Shamayum Rashad
 */
public class PwdTest {
  MockJFileSystem fileSystem;
  Pwd pwd;
  ArrayList<String> arguments;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    pwd = new Pwd(fileSystem);
    arguments = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testPwd() {
    pwd.executeCommand(arguments);
    String actual = pwd.getPrintCommand();
    String expected = "/";
    assertEquals(actual, expected);
  }

  @Test
  public void testPwdWithInput() {
    String input = "dir";
    arguments.add(input);
    pwd.executeCommand(arguments);
    String actual = pwd.getError();
    String expected = input + ": pwd does not take any inputs";
    assertEquals(actual, expected);
  }

  @Test
  public void testPwdInRootChild() {
    fileSystem.addDirectory("/", "test");
    Directory rootChild = (Directory) fileSystem.getRoot().getContents().get(0);
    fileSystem.setCurrentDirectory(rootChild);
    pwd.executeCommand(arguments);
    String actual = pwd.getPrintCommand();
    String expected = "/test";
    assertEquals(actual, expected);
  }

  @Test
  public void testPwdInNonEmptyTree() {
    fileSystem.addDirectory("/", "test");
    fileSystem.addTextFile("/", "test2", "content");
    pwd.executeCommand(arguments);
    String actual = pwd.getPrintCommand();
    String expected = "/";
    assertEquals(actual, expected);
  }
}
