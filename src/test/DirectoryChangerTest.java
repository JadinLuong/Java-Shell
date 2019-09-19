package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import driver.Directory;
import driver.DirectoryChanger;
import driver.ExistingFileException;
import driver.FileSystem;
import driver.InvalidFileNameException;
import driver.TextFile;

public class DirectoryChangerTest {

  MockJFileSystem fs;
  Directory dr1, dr2, dr1Child1;
  TextFile t1;
  DirectoryChanger directoryChanger;
  ArrayList<String> args;

  @Before
  public void setUp() {
    fs = MockJFileSystem.createInstanceOfJFileSystem();
    dr1 = new Directory("dr1", fs.getCurrentDirectory());
    dr2 = new Directory("dr2", fs.getCurrentDirectory());
    dr1Child1 = new Directory("dr1Child1", dr1);
    t1 = new TextFile("t1", fs.getCurrentDirectory(), "abcdef");
    try {
      fs.getCurrentDirectory().addContents(dr1);
      fs.getCurrentDirectory().addContents(dr2);
      fs.getCurrentDirectory().addContents(t1);
      dr1.addContents(dr1Child1);
    } catch (InvalidFileNameException | ExistingFileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    directoryChanger = new DirectoryChanger(fs);
    args = new ArrayList<String>();
  }

  @Test
  public void TestExecuteEmptyCommand() {
    directoryChanger.executeCommand(args);
    assertEquals("Given a Directory to change.", directoryChanger.getError());
  }

  @Test
  public void TestExecuteRelativePathCommand() {
    args.add("dr1");
    directoryChanger.executeCommand(args);
    System.out.println(fs.getCurrentDirectory());
    assertEquals(dr1.getFileName(), fs.getCurrentDirectory().getFileName());
  }

  @Test
  public void TestExecuteFullPathCommand() {
    args.add("dr1/dr1Child1");
    directoryChanger.executeCommand(args);
    assertEquals(dr1Child1.getFileName(),
        fs.getCurrentDirectory().getFileName());
  }

  @Test
  public void TestExecuteInvalidPathCommand() {
    args.add("dr1,dr1Child1");
    directoryChanger.executeCommand(args);
    assertEquals("dr1,dr1Child1 is not a path to Directory",
        directoryChanger.getError());
  }

  @Test
  public void TestExecuteUnreachablePathCommand() {
    args.add("dr1Child1");
    directoryChanger.executeCommand(args);
    assertEquals("dr1Child1 is not a path to Directory",
        directoryChanger.getError());
  }

  @Test
  public void TestExecutePathToTextFileCommand() {
    args.add("t1");
    directoryChanger.executeCommand(args);
    assertEquals("t1 is a path to TextFile",
        directoryChanger.getError());
  }

  @Test
  public void TestExecuteUpLevelCommand() {
    args.add("..");
    directoryChanger.executeCommand(args);
    assertEquals("/", fs.getCurrentDirectory().getFileName());
  }

  @Test
  public void TestExecuteNoChangeCommand() {
    args.add(".");
    directoryChanger.executeCommand(args);
    assertEquals("/", fs.getCurrentDirectory().getFileName());
  }
}
