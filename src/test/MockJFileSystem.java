package test;

import java.util.ArrayList;
import driver.Directory;
import driver.DirectoryStack;
import driver.FileSystem;
import driver.InvalidPathException;
import driver.TextFile;

/**
 * Represents a mock JFileSystem as a whole
 * 
 * @author Jadin Luong
 *
 */
public class MockJFileSystem implements FileSystem<Directory> {
  // Represents an instance of a JFileSystem
  private static MockJFileSystem fileSystemRef = null;

  // Represents the root directory of the file system
  private Directory root;

  // Represents the current directory the user is currently working in
  private Directory currentDirectory;

  // Represents the history of commands executed by the user
  private ArrayList<String> History = new ArrayList<String>();

  // Represents the stack of directories (For the purpose of popd and pushd)
  private DirectoryStack directoryStack = new DirectoryStack();

  /**
   * Creates a FileSystem object
   */
  private MockJFileSystem() {
    this.root = new Directory("/", null);
    this.currentDirectory = root;
  }

  /**
   * Creates a single instance of a JFileSystem for singleton principle purposes
   * 
   * @return an instance of a JFileSystem
   */
  public static MockJFileSystem createInstanceOfJFileSystem() {
    if (fileSystemRef == null) {
      fileSystemRef = new MockJFileSystem();
    }
    return fileSystemRef;
  }

  /** Returns the root of the file system */
  public Directory getRoot() {
    return this.root;
  }

  /**
   * Sets the current root of the file system to a different root
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

  /** Returns the current directory the file system is at. */
  public Directory getCurrentDirectory() {
    return this.currentDirectory;
  }

  /**
   * Save the most recent command executed into the history
   * 
   * @param input represents a string that holds the recent command executed
   */
  public void addToHistory(String input) {
    this.getCommandHistory().add(input);
  }

  /** Returns the list containing the history of all executed commands */
  public ArrayList<String> getCommandHistory() {
    return this.History;
  }

  /**
   * Sets the current directoryStack to a new stack of directories.
   * 
   * @param dirStack represents a new stack of directories
   */
  public void setDirectoryStack(DirectoryStack dirStack) {
    this.directoryStack = dirStack;
  }

  /** Returns a stack of directories */
  public DirectoryStack getDirectoryStack() {
    return this.directoryStack;
  }

  public void addDirectory(String path, String dirName) {
    MockPath dirPath = new MockPath(this);
    try {
      Directory parentDir = (Directory) dirPath.searchByPath(path);
      Directory newDirectory = new Directory(dirName, (Directory) parentDir);
      parentDir.getContents().add(newDirectory);
    } catch (InvalidPathException e) {
      // Do nothing
    }
  }
  
  public void addTextFile(String path, String fileName, String content) {
    MockPath dirPath = new MockPath(this);
    try {
      Directory parentDir = (Directory) dirPath.searchByPath(path);
      TextFile newTextfile = new TextFile(fileName, parentDir, content);
      parentDir.getContents().add(newTextfile);
    } catch (InvalidPathException e) {
      // Do nothing
    }
  }

  /**
   * Returns a string representation of the files within the file system as a
   * tree.
   */
  public String toString() {
    System.out.print(root);
    return "";
  }

  public static void main(String[] args) {
    MockJFileSystem system = new MockJFileSystem();
    system.addTextFile("/", "Test", "content");
    system.addDirectory("/", "Test1");
    system.addDirectory("/", "test2");
    system.addDirectory("/Test1", "test2");
    try {
      MockPath dirPath = new MockPath(system);
      System.out.println((Directory) dirPath.searchByPath("/Test1"));
    } catch (InvalidPathException e){
      System.out.println("l");
    }
    System.out.println(system);
  }
}
