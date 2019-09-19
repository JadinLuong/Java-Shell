package test;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.Command;
import driver.Directory;
import driver.FileSystem;
import driver.History;
import driver.InvalidCommandException;
import driver.Man;
import driver.Parser;

public class ParserTest {

  FileSystem<Directory> fileSystem;
  ArrayList<String> arguments;
  HashMap<String, ArrayList<String>> map;

  @Before
  public void SetUp() {
    fileSystem = MockJFileSystem.createInstanceOfJFileSystem();
    arguments = new ArrayList<>();
    map = new HashMap<>();
    
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void testParseInputOneWord() {
    String input = "man";
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputMultipleWord() {
    String input = "man cp";
    arguments.add("cp");
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputString() {
    String input = "\"string content\"";
    arguments.add( "content\"");
    map.put("\"string", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputStringAndCommand() {
    String input = "echo \"echo content\"";
    arguments.add("\"echo content\"");
    map.put("echo", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputWhiteSpaceAtEnds() {
    String input = "   man cp mv   ";
    arguments.add("cp");
    arguments.add("mv");
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputWhiteSpaceInBetween() {
    String input = "man       cp";
    arguments.add("cp");
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputWhiteSpace() {
    String input = " man    cp   mv    ";
    arguments.add("cp");
    arguments.add("mv");
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputStringAndWords() {
    String input = "echo  \"string \" file ";
    arguments.add("\"string \"");
    arguments.add("file");
    map.put("echo", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputAppendVariable() {
    String input = "man cp   >>  file";
    arguments.add("cp");
    arguments.add(">>");
    arguments.add("file");
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testParseInputOverwriteVariable() {
    String input = "man cp    > file ";
    arguments.add("cp");
    arguments.add(">");
    arguments.add("file");
    map.put("man", arguments);
    HashMap<String, ArrayList<String>> actualOutput = Parser.parseInput(input);

    assertEquals(actualOutput, map);
  }

  @Test
  public void testCommandObjectInvalidCommand() {
    String input = "invalid";
    boolean correctCommand = false;
    try {
      Parser.commandObject(input, fileSystem);
    } catch (InvalidCommandException e) {
      correctCommand = true;
    }

    assertEquals(correctCommand, true);
  }

  @Test
  public void testCommandObjectCommandWithoutAnyParameters() {
    String input = "man";
    boolean correctCommand = false;
    try {
      Command command = Parser.commandObject(input, fileSystem);
      if (command instanceof Man) {
        correctCommand = true;
      }
    } catch (InvalidCommandException e) {
    }

    assertEquals(correctCommand, true);
  }

  @Test
  public void testCommandObjectCommandWithFileSystemParameter() {
    String input = "history";
    boolean correctCommand = false;
    try {
      Command command = Parser.commandObject(input, fileSystem);
      if (command instanceof History) {
        correctCommand = true;
      }
    } catch (InvalidCommandException e) {
    }

    assertEquals(correctCommand, true);
  }

  @Test
  public void testCommandObjectCapitalCommand() {
    String input = "Man";
    boolean correctCommand = false;
    try {
      Parser.commandObject(input, fileSystem);
    } catch (InvalidCommandException e) {
      correctCommand = true;
    }

    assertEquals(correctCommand, true);
  }
}
