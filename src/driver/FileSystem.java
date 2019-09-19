package driver;

import java.util.ArrayList;

/**
 * Represents an interface of a FileSystem.
 * 
 * @author Jadin Luong
 */
public interface FileSystem<E> {

  /**
   * Returns the root of the file system.
   * 
   * @return the root of the file system
   */
  public E getRoot();

  /**
   * Sets the current root of the file system to a different root.
   * 
   * @param newRoot represents a new root for the file system
   */
  public void setRoot(E newRoot);

  /**
   * Sets the current working directory to a different directory.
   * 
   * @param toSet represents another directory in the file system.
   */
  public void setCurrentDirectory(E toSet);

  /**
   * Returns the current working directory.
   * 
   * @return the current working directory.
   */
  public E getCurrentDirectory();

  /**
   * Appends the most recent executed command into the history.
   * 
   * @param input represents the recent command executed as a string.
   */
  public void addToHistory(String input);

  /**
   * Returns the history of all executed commands.
   * 
   * @return the history of all executed commands.
   */
  public ArrayList<String> getCommandHistory();
}
