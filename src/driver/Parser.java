package driver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a command line Parser. It parses the input into a hash map with
 * the command as a key and the arguments as the value of the key.
 * 
 * @author Shamayum Rashad
 *
 */
public class Parser {

  /**
   * Returns a hash map of the input with the command in the input as a key and
   * the arguments of the command as the value of the key.
   * 
   * @param input the user input for the command line
   * @return the hash map with the command and arguments as a key-value pair
   */
  public static HashMap<String, ArrayList<String>> parseInput(String input) {

    String trimmedInput = input.trim();
    int index = 0;
    String command = "";
    ArrayList<String> arguments = new ArrayList<>();
    HashMap<String, ArrayList<String>> commandAndArguments = new HashMap<>();

    // find the first word of the input which is the command
    while ((index < trimmedInput.length())
        && (trimmedInput.charAt(index) != ' ')) {
      command += String.valueOf(trimmedInput.charAt(index));
      index++;
    }

    // find the arguments in the input
    if (index < trimmedInput.length()) {
      if (command.equals("echo")) {
        arguments = echoArguments(trimmedInput.substring(index));
      } else {
        arguments = separateArguments(trimmedInput.substring(index));
      }
    }

    commandAndArguments.put(command, arguments);
    return commandAndArguments;
  }

  /**
   * Returns a list of the arguments in a string with the white spaces removed
   * and quotes intact.
   * 
   * @param input the string of words
   * @return the list of the words with the white spaces removed and quotes
   *         intact
   */
  private static ArrayList<String> separateArguments(String input) {

    int i = 0;
    int subIndex;
    ArrayList<String> arguments = new ArrayList<>();

    while (i < input.length()) {
      // if there contains a quote, add the whole quote as an argument
      if ((input.charAt(i) == '\"') && (input.length() > (i + 1))
          && (input.substring(i + 1).contains("\""))) {
        subIndex = input.substring(i + 1).indexOf('\"') + (i + 1);
        arguments.add(input.substring(i, subIndex + 1));
        i = subIndex;
      }
      // else if there is a word between spaces, add the word to the list
      else if ((input.charAt(i) != ' ') && (input.length() > (i + 1))
          && (input.substring(i + 1).contains(" "))) {
        subIndex = input.substring(i + 1).indexOf(' ') + (i + 1);
        arguments.add(input.substring(i, subIndex));
        i = subIndex;
      }
      // else if the word is the last word, add it to the list
      else if (input.charAt(i) != ' ') {
        arguments.add(input.substring(i));
        i += input.substring(i).length();
      }
      i++;
    }
    return arguments;
  }

  /**
   * Returns a list of the words in input with the white spaces removed unless
   * the spaces are in between a quote. This is used to parse the input for the
   * echo command.
   * 
   * @param input the string of words
   * @return the list of words with white spaces removed but quotes intact
   */
  private static ArrayList<String> echoArguments(String input) {

    ArrayList<String> arguments = new ArrayList<String>();

    /*
     * split the string into two by the > present in the string and add the
     * trimmed beginning string to the list
     */
    if (input.contains(">")) {
      arguments.add(input.substring(0, input.indexOf(">")).trim());
      input = input.substring(input.indexOf(">"));
      // remove the white spaces of the end string and add the words to the list
      ArrayList<String> argumentsAfterString = separateArguments(input);
      arguments.addAll(argumentsAfterString);
      return arguments;
    }

    // remove the white spaces of the end string and add the words to the list
    ArrayList<String> argumentsAfterString = separateArguments(input);
    arguments.addAll(argumentsAfterString);
    return arguments;
  }

  /**
   * Gives a new instance of the a command line command specified by command.
   * 
   * @param command a valid command line command
   * @param fileSystem the file system that the command will be working on
   * @return a new instance of a command object
   * @throws invalidCommandException exception when the command does not exist
   */
  public static Command commandObject(String command,
      FileSystem<Directory> fileSystem) throws InvalidCommandException {

    Command specificCommand;
    FileSystemSaver saver = new JFileSystemSaver();

    /*
     * if command is equal to a valid command, create a new instance of the
     * command
     */
    switch (command) {
      case "cd":
        specificCommand = new DirectoryChanger(fileSystem);
        break;
      case "mkdir":
        specificCommand = new DirectoryMaker(fileSystem);
        break;
      case "ls":
        specificCommand = new Ls(fileSystem);
        break;
      case "pwd":
        specificCommand = new Pwd(fileSystem);
        break;
      case "cat":
        specificCommand = new Cat(fileSystem);
        break;
      case "echo":
        specificCommand = new Echo();
        break;
      case "history":
        specificCommand = new History(fileSystem);
        break;
      case "pushd":
        specificCommand = new Pushd(fileSystem);
        break;
      case "popd":
        specificCommand = new Popd(fileSystem);
        break;
      case "man":
        specificCommand = new Man();
        break;
      case "tree":
        specificCommand = new Tree(fileSystem);
        break;
      case "cp":
        specificCommand = new Copy(fileSystem);
        break;
      case "mv":
        specificCommand = new Move(fileSystem);
        break;
      case "find":
        specificCommand = new Find(fileSystem);
        break;
      case "get":
        HTTPDataFetcher dataFetcher = new HTTPDataFetcher();
        specificCommand = new Get(fileSystem, dataFetcher);
        break;
      case "load":
        specificCommand = new Load(fileSystem, saver);
        break;
      case "save":
        specificCommand = new Save(fileSystem, saver);
        break;
      default:
        throw new InvalidCommandException(command + ": command not found");
    }
    return specificCommand;
  }
}
