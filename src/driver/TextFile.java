package driver;

public class TextFile extends File {

  /**
   * the serial version for serialization
   */
  private static final long serialVersionUID = 5L;

  /**
   * Represents the data in the text file.
   */
  private String contents;

  /**
   * Creates a TextFile object
   * 
   * @param name Represents the desired name of the new file.
   * @param parent Represents the parent directory of the new file.
   * @param content Represents all the contents inside of the file.
   */
  public TextFile(String name, Directory parent, String content) {
    super(name, parent);
    this.contents = content;
  }

  /**
   * Overwrites the old contents of the file with new contents.
   * 
   * @param content Represents the new contents.
   */
  public void setContents(String content) {
    this.contents = content;
  }

  /**
   * Returns the contents of the text file.
   * 
   * @return a string which represents the contents of the text file.
   */
  public String getContents() {
    return this.contents;
  }

  /**
   * Prints the file name of the text file.
   * 
   * @return a string which represents the name of the text file.
   */
  public String toString() {
    return this.getFileName();
  }

  /**
   * Checks to see if two text file objects have similar attributes.
   * 
   * @param textFile represents the textfile being compared to
   * @return whether the two textfile objects are the same or not
   */
  public boolean equals(TextFile textFile) {
    boolean isEqual = false;
    isEqual = this.getParentDir().equals(textFile.getParentDir())
        && this.getContents().equals(textFile.getContents())
        && this.getFileName().equals(textFile.getFileName());
    return isEqual;
  }
}
