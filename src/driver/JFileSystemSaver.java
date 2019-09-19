package driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The JFileSystem saver.
 * 
 * @author Shamayum Rashad
 *
 */
public class JFileSystemSaver implements FileSystemSaver{

  /**
   * Default constructor for JFileSystemSaver.
   */
  public JFileSystemSaver() { 
  }

  
  /**
   * Saves the JFileSystem instance to a file names fileName.
   * 
   * @param fileName  the name of the file that will store the file system
   * @param fileSys   the file system that will be saved
   * @return          any errors that result
   */
  @Override
  public String saveFileSystem(String fileName, FileSystem<Directory> fileSys) {

    String errors = "";

    // add the file system components to a hashmap to be saved to the file
    HashMap<String, Object> savedObjects = new HashMap<String, Object>();
    savedObjects.put("root", fileSys.getRoot());
    savedObjects.put("currentDirectory",
        fileSys.getCurrentDirectory());
    savedObjects.put("history", fileSys.getCommandHistory());
    savedObjects.put("directoryStack",
        ((JFileSystem)fileSys).getDirectoryStack());
    try {
      // save the hashmap to the file
      FileOutputStream fileOut = new FileOutputStream(fileName);
      ObjectOutputStream objOutput = new ObjectOutputStream(fileOut);
      objOutput.writeObject(savedObjects);
      objOutput.close();
      fileOut.close();
      // give appropriate errors for any exceptions
    } catch (FileNotFoundException e) {
      errors = "The filePath: " + fileName
          + " exists but is a directory rather than a regular file"
          + "The filePath: " + fileName
          + "\nDoes not exist but cannot be created" + "The filePath: "
          + fileName + "\nOr cannot be opened for any other reason ";
    } catch (IOException e) {
      errors = "An I/O error occurs while writing stream header";
    } catch (SecurityException e) {
      errors = 
          "untrusted subclass illegally overrides security-sensitive methods";
    } catch (NullPointerException e) {
      errors = "Input of the ObjectOutputStream constructor is null";
    }

    return errors;
  }

  /**
   * Loads the saved JFileSystem in the file fileName to the file system.
   * 
   * @param fileName  the name of the file that stores the saved file system
   * @param fileSys   the file system that will be overloaded
   * @return          any errors that result
   */
  @SuppressWarnings("unchecked")
  @Override
  public String getSavedFileSystem(String fileName, 
      FileSystem<Directory> fileSys) {

    String errors = "";
    String loadInput = fileSys.getCommandHistory().get(0).substring(3);
    
    // get the saved file system from the file given
    try {
      FileInputStream inputFile = new FileInputStream(fileName);
      ObjectInputStream inputObject = new ObjectInputStream(inputFile);
      // change the current file system to the saved file system
      try {
        HashMap<String, Object> serializedData =
            (HashMap<String, Object>) inputObject.readObject();
        ((JFileSystem)fileSys).setCommandHistory(
            (ArrayList<String>) serializedData.get("history"));
        fileSys.setCurrentDirectory(
            (Directory) serializedData.get("currentDirectory"));
        ((JFileSystem)fileSys).setDirectoryStack(
            (DirectoryStack) serializedData.get("directoryStack"));
        fileSys.setRoot((Directory) serializedData.get("root"));
        // add the load input to the command history of the loaded file system
        fileSys.addToHistory(loadInput);
        // give appropriate errors for any exceptions
      } catch (FileNotFoundException e1) {
        errors = fileName + ": file not found";
      } catch (ClassNotFoundException e2) {
        errors = fileName + ": does not store a loadable object";
      }
      inputFile.close();
      inputObject.close();
    } catch (IOException e) {
      errors = fileName + ": not an appropriate file";
    }
    return errors;
  }
}
