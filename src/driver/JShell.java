package driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents the command line. Users can input in commands to manipulate the
 * file system.
 * 
 * @author Shamayum Rashad
 * 
 */
public class JShell {

  public static void main(String[] args) {

    // create the File System program and objects needed to start program
    FileSystem<Directory> fileSystem =
        JFileSystem.createInstanceOfJFileSystem();
    ArrayList<String> arguments;
    Command specificCommand;

    // prompt user for input and parse through the input
    String userInput;
    Scanner in = new Scanner(System.in);
    System.out.print("/#: ");
    userInput = in.nextLine();
    HashMap<String, ArrayList<String>> inputParsed =
        Parser.parseInput(userInput);
    String command;
    List<String> redirectionPath;

    /*
     * until user inputs the exit program command, continue to prompt the user
     * for commands and execute the commands
     */
    while (!(inputParsed.containsKey("exit"))
        || !(inputParsed.get("exit").isEmpty())) {

      fileSystem.addToHistory(userInput);

      // if the input has arguments with the exit command, give an error
      if (inputParsed.containsKey("exit")) {
        System.out.println("exit: does not take any arguments");
      }

      // otherwise get the command, arguments and redirection paths from the map
      else {
        for (Map.Entry<String, ArrayList<String>> pair : inputParsed
            .entrySet()) {
          command = pair.getKey();
          arguments = new ArrayList<String>(pair.getValue().subList
              (0, RedirectionDecorator.redirectionStart(pair.getValue())));
          redirectionPath = pair.getValue().subList
              (RedirectionDecorator.redirectionStart
                  (pair.getValue()), pair.getValue().size());

          if (!command.isEmpty()) {
            // if the command isn't empty, execute the specific command inputed
            try {
              specificCommand = new RedirectionDecorator
                  (Parser.commandObject(command, fileSystem), redirectionPath, 
                      command, fileSystem);
              specificCommand.executeCommand(arguments);
              if (!specificCommand.getPrintCommand().isEmpty()) {
                System.out.println(specificCommand.getPrintCommand());
              }
              } catch (InvalidCommandException e) {
              if (command.contains("\"")) {
                System.out.println(command + ": excessive quotes");
              } else {
                System.out.println(command + ": command not found");
              }
            }
          }
        }
      }
      // prompt the user for commands and parse through the input
      System.out.print("/#: ");
      userInput = in.nextLine();
      inputParsed = Parser.parseInput(userInput);
    }
    in.close();
  }
}
