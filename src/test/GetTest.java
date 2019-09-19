package test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.*;

public class GetTest {
  MockJFileSystem fileSystem;
  Get get;
  MockPath mockPath;
  ArrayList<String> arguments;
  MockRemoteDataFetcher dataFetcher;

  @Before
  public void setUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    dataFetcher = new MockRemoteDataFetcher();
    get = new Get(fileSystem, dataFetcher);
    mockPath = new MockPath(fileSystem);
    arguments = new ArrayList<String>();

  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testGetMissingURL() {
    get.executeCommand(arguments);
    String actual = get.getError();
    String expected = "get: missing URL";
    assertEquals(expected, actual);
  }

  @Test
  public void testGetProblematicURL() {
    String fakeURL = "//33thdhrddr222awgsezdhaezgWseRWAs.teFwSF";
    arguments.add(fakeURL);
    get.executeCommand(arguments);
    String actual = get.getError();
    String expected = "get: problem with URL";
    assertEquals(expected, actual);
  }

  @Test
  public void testGetTxt() throws InvalidPathException, IOException {
    String txtURL = "http://www.utsc.utoronto.ca/~nick/cscB36/189/lecture.txt";
    arguments.add(txtURL);
    get.executeCommand(arguments);
    // will fail test if fetched file not exist
    mockPath.searchByPath("/lecture");
  }

  @Test
  public void testGetHtml() throws InvalidPathException, IOException {
    String htmlURL = "https://www.utsc.utoronto.ca/~nick/cscB36/189/t1.html";
    arguments.add(htmlURL);
    get.executeCommand(arguments);
    // will fail test if fetched file not exist
    mockPath.searchByPath("/t1");
  }
}
