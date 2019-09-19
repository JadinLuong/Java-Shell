package driver;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the JFileSystem as a whole
 * 
 * @author Jadin Luong
 *
 */
public class JFileSystem
    implements FileSystem<Directory>, Iterator<File>, Serializable {


  /**
   * the serial version for serialization
   */
  private static final long serialVersionUID = 1L;

  /**
   * Represents an instance of a JFileSystem
   */
  private static JFileSystem fileSystemRef = null;

  /**
   * Represents the root directory of the file system
   */
  private Directory root;

  /**
   * Represents the current directory the user is currently working in
   */
  private Directory currentDirectory;

  /**
   * Represents the history of commands executed by the user
   */
  private ArrayList<String> history;

  /**
   * Represents the stack of directories (For the purpose of popd and pushd)
   */
  private DirectoryStack directoryStack;

  /**
   * Creates a FileSystem object.
   */
  private JFileSystem() {
    this.root = new Directory("/", null);
    this.currentDirectory = root;
    this.history = new ArrayList<String>();
    this.directoryStack = new DirectoryStack();
  }

  /**
   * Creates a single instance of a JFileSystem for singleton principle purposes
   * 
   * @return an instance of a JFileSystem
   */
  public static JFileSystem createInstanceOfJFileSystem() {
    if (fileSystemRef == null) {
      fileSystemRef = new JFileSystem();
    }
    return fileSystemRef;
  }

  /**
   * Returns the root of the JFileSystem.
   * 
   * @return the root of the JFileSystem.
   */
  public Directory getRoot() {
    return this.root;
  }

  /**
   * Sets the current root of the file system to a different root.
   * 
   * @param newRoot represents a new root for the file system
   */
  public void setRoot(Directory newRoot) {
    this.root = newRoot;
  }

  /**
   * Set the current directory to a different directory.
   * 
   * @param toSet the directory that the file system is at
   */
  public void setCurrentDirectory(Directory toSet) {
    this.currentDirectory = toSet;
  }

  /**
   * Returns the current working directory of the JFileSystem.
   * 
   * @return the current working directory of the JFilesystem.
   */
  public Directory getCurrentDirectory() {
    return this.currentDirectory;
  }

  /**
   * Save the most recent command executed into the history.
   * 
   * @param input represents a string that holds the recent command executed
   */
  public void addToHistory(String input) {
    this.getCommandHistory().add(input);
  }

  /**
   * Returns the list containing the history of all executed commands.
   * 
   * @return an Array List of strings containing the history of all executed
   *         commands.
   */
  public ArrayList<String> getCommandHistory() {
    return this.history;
  }

  /**
   * Sets the current history of commands to a new history of commands.
   * 
   * @param historyList represents the new history of commands.
   */
  public void setCommandHistory(ArrayList<String> historyList) {
    this.history = historyList;
  }

  /**
   * Sets the current directoryStack to a new stack of directories.
   * 
   * @param dirStack represents a new stack of directories
   */
  public void setDirectoryStack(DirectoryStack dirStack) {
    this.directoryStack = dirStack;
  }

  /**
   * Returns a stack of directories.
   * 
   * @return a stack of directories.
   */
  public DirectoryStack getDirectoryStack() {
    return this.directoryStack;
  }


  /**
   * For serialization of JFileSystem so that the singleton pattern is not
   * broken.
   * 
   * @return an instance of JFileSystem
   * @throws ObjectStreamException exception when there is an error with the
   *         object stream
   */
  protected Object readResolve() throws ObjectStreamException {

    return JFileSystem.createInstanceOfJFileSystem();
  }

  /**
   * Returns a string representation of the files within the file system as a
   * tree.
   * 
   * @return a string representation of the files within the file system.
   */
  public String toString() {
    return root.toString();
  }

  /**
   * Returns the next file in the file system in a pre-order traversal.
   * 
   * @return a File that is next in the iteration of the file system.
   */
  public File next() {
    // Retrieve the current file and it's parent
    File currFile = this.getCurrentDirectory();
    Directory currParentDir = currFile.getParentDir();
    // Check if the current file is last directory in the file system or the
    // file system contains just the root directory
    if ((currFile.equals(this.getRoot())
        && ((Directory) currFile).getContents().size() == 0)
        || currFile.equals(this.getLastFileInFileSystem())) {
      currFile = null;
    } else {
      // If the current directory does not have any child then traverse to the
      // next directory in pre order traversal
      if (((Directory) currFile).getContents().size() == 0) {
        while (currParentDir.getContents()
            .indexOf(currFile) == currParentDir.getContents().size() - 1) {
          currFile = currParentDir;
          currParentDir = currFile.getParentDir();
        }
        // Get the index of the next directory in the file system
        int indexOfcurrFile = currParentDir.getContents().indexOf(currFile);
        currFile = currParentDir.getContents().get(indexOfcurrFile + 1);
      } else {
        // Go to the first child if the current directory has children
        currFile = ((Directory) currFile).getContents().get(0);
      }
    }
    return currFile;
  }

  /**
   * A helper method for the next() method which returns the last file in the
   * file system in pre-order.
   * 
   * @return a File that is at the end of the file system.
   */
  private File getLastFileInFileSystem() {
    // Get the last child out of the root's contents (children)
    ArrayList<File> rootContents = this.getRoot().getContents();
    File lastFile = rootContents.get(rootContents.size() - 1);
    // Will determine whether the last file was found.
    boolean foundLastFile = false;
    // Iterate through the file System in pre order until last file is found
    while (!foundLastFile) {
      if (lastFile instanceof Directory) {
        if (((Directory) lastFile).getContents().size() > 0) {
          lastFile = ((Directory) lastFile).getContents()
              .get(((Directory) lastFile).getContents().size() - 1);
        } else {
          foundLastFile = true;
        }
      } else {
        foundLastFile = true;
      }
    }
    return lastFile;
  }

  /**
   * Returns whether there is another file after the current directory in
   * pre-order traversal.
   * 
   * @return true or false depending if there is a File in the next iteration of
   *         the file system.
   */
  public boolean hasNext() {
    boolean retVal;
    File nextFile = this.next();
    // Uses the next() method and if null is returned this means the current
    // working directory is the last directory in the file system
    if (nextFile == null) {
      retVal = false;
    } else {
      retVal = true;
    }
    return retVal;
  }

}
