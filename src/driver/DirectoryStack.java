package driver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Stack of directories (Works in a LIFO fashion)
 * 
 * @author Jadin Luong
 *
 */
public class DirectoryStack implements Serializable {

  /**
   * the serial version for serialization
   */
  private static final long serialVersionUID = 3L;

  /**
   * Represents the storage of the directory stack.
   */
  private ArrayList<Directory> dirStack;

  /**
   * Creates an instance of a DirectoryStack.
   */
  public DirectoryStack() {
    this.dirStack = new ArrayList<Directory>();
  }

  /**
   * Returns the directory at the top of the stack.
   * 
   * @return the directory at the top of the stack.
   */
  public Directory pop() {
    Directory poppedElement = null;
    int lastItemIndex = this.getDirStack().size() - 1;
    // Ensure that the stack is not empty (Avoid any errors)
    if (this.getDirStack().size() > 0) {
      // Retrieves the directory being popped so it can be returned
      poppedElement = this.getDirStack().remove(lastItemIndex);
    }
    return poppedElement;
  }

  /**
   * Pushes a directory to the top of the stack.
   * 
   * @param dir The directory being pushed into the stack
   */
  public void push(Directory dir) {
    this.getDirStack().add(dir);
  }

  /**
   * Replaces the current stack of directories with a new stack of directories.
   * 
   * @param newStack represents the new stack of directories
   */
  public void setDirStack(ArrayList<Directory> newStack) {
    this.dirStack = newStack;
  }

  /**
   * Returns the stack of directories.
   * 
   * @return the stack of directories.
   */
  public ArrayList<Directory> getDirStack() {
    return this.dirStack;
  }

  /**
   * Returns whether the directory stack is empty or not.
   * 
   * @return true or false depending if the stack of directories is empty or not
   */
  public boolean isEmpty() {
    return this.getDirStack().isEmpty();
  }

  /**
   * Checks to see if two directory stack objects have similar attributes.
   * 
   * @param directoryStack represents the directory stack being compared to
   * @return whether the two directory stack objects are the same or not
   */
  public boolean equals(DirectoryStack directoryStack) {
    boolean isEqual;
    isEqual = this.getDirStack().equals(directoryStack.getDirStack());
    return isEqual;
  }
}
