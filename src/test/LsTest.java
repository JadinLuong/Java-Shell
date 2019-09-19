package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import driver.Directory;
import driver.File;
import driver.Ls;
import driver.TextFile;

public class LsTest {

  MockJFileSystem fs;
  Directory dr1, dr2, dr1Child1, dr1Child2;
  TextFile t1;
  Ls ls;
  ArrayList<String> args;

  @Before
  public void setUp() throws Exception {
    fs = MockJFileSystem.createInstanceOfJFileSystem();
    dr1 = new Directory("dr1", fs.getCurrentDirectory());
    dr2 = new Directory("dr2", fs.getCurrentDirectory());
    dr1Child1 = new Directory("dr1Child1", dr1);
    dr1Child2 = new Directory("dr1Child2", dr1);
    t1 = new TextFile("t1", fs.getCurrentDirectory(), "abcdef");
    fs.getCurrentDirectory().addContents(dr1);
    fs.getCurrentDirectory().addContents(dr2);
    fs.getCurrentDirectory().addContents(t1);
    dr1.addContents(dr1Child1);
    dr1.addContents(dr1Child2);
    ls = new Ls(fs);
    args = new ArrayList<String>();
  }

  @Test
  public void TestNoInput() {
    ls.executeCommand(args);
    assertEquals(printContentsInAList(fs.getCurrentDirectory()),
        ls.getPrintCommand());
  }

  @Test
  public void TestPathToTextFile() {
    args.add("t1");
    ls.executeCommand(args);
    assertEquals("t1\n", ls.getPrintCommand());
  }

  @Test
  public void TestRelativePathToDirectory() {
    args.add("dr2");
    ls.executeCommand(args);
    assertEquals("dr2:\n", ls.getPrintCommand());
  }

  @Test
  public void TestAbsolutePathToDirectory() {
    args.add("dr1/dr1Child1");
    ls.executeCommand(args);
    assertEquals("dr1/dr1Child1:\n", ls.getPrintCommand());

  }

  @Test
  public void TestPathToDirectoryWithContents() {
    args.add("dr1");
    ls.executeCommand(args);
    assertEquals("dr1:\n" + "dr1Child1\n" + "dr1Child2\n",
        ls.getPrintCommand());
  }

  @Test
  public void TestInvalidPath() {
    args.add("dr3");
    ls.executeCommand(args);
    assertEquals("There is no such file as:dr3\n", ls.getError());
  }

  @Test
  public void MultipleCommands() {
    args.add("t1");
    args.add("dr2");
    args.add("dr1/dr1Child1");
    args.add("dr1");
    args.add("dr3");
    ls.executeCommand(args);
    assertEquals("t1\n" + "dr2:\n" + "dr1/dr1Child1:\n" + "dr1:\n"
        + "dr1Child1\n" + "dr1Child2\n", ls.getPrintCommand());
    assertEquals("There is no such file as:dr3\n", ls.getError());
  }

  @Test
  public void TestListAllCurrentDirectoryContents() {
    args.add("-R");
    ls.executeCommand(args);
    assertEquals(fs.getCurrentDirectory().toString(), ls.getPrintCommand());
  }

  @Test
  public void TestListAllSpecificDirectoryContents() {
    args.add("-R");
    args.add("dr1");
    ls.executeCommand(args);
    System.out.println(ls.getPrintCommand());
    assertEquals("dr1:\n\tdr1Child1\n\tdr1Child2\n", ls.getPrintCommand());
  }

  @Test
  public void TestMultipleEntiesWithRecursion() {
    args.add("-R");
    args.add("dr1");
    args.add("dr1/dr1Child1");
    args.add("t1");
    ls.executeCommand(args);
    assertEquals(
        "dr1:\n\tdr1Child1\n\tdr1Child2\n" + "dr1/dr1Child1:\n" + "t1\n",
        ls.getPrintCommand());
  }

  private String printContentsInAList(Directory dr) {
    String result = "";
    ArrayList<File> contents = dr.getContents();
    for (File content : contents) {
      result += content.getFileName() + "\n";
    }
    return result;
  }

}
