package driver;

import java.util.ArrayList;

/**
 * For command cat.
 * 
 * @author Ya-Tzu Wang
 */
public class Cat extends Command {

  /**
   * fileSystem The file system storing all the files
   */
  private FileSystem<Directory> fileSystem;

  /**
   * Construct Cat object with a fileSystem.
   * 
   * @param fs the FileSystem
   */
  public Cat(FileSystem<Directory> fs) {
    super(true);
    this.fileSystem = fs;
  }

  /**
   * Execute command cat.
   * 
   * @param arguments an ArrayList containing information related to cat command
   */
  public void executeCommand(ArrayList<String> arguments) {
    // do nothing if cat is not followed by any strings
    if (arguments.isEmpty()) {
      this.setPrintCommand("");
    } else {
      Path p = new Path(this.fileSystem);
      ArrayList<TextFile> textfiles = new ArrayList<TextFile>();
      // for each file
      for (int index = 0; index < arguments.size(); index++) {
        String path = arguments.get(index);
        try {
          File file = p.searchByPath(path);
          // if given a directory, display error messages to Jshell
          if (file instanceof Directory) {
            this.setError("cat: " + file.getFileName() + ": Is a directory");
            // if given a text file, display its content
          } else {
            textfiles.add((TextFile) file);
          }
          // if path is invalid, display error messages to Jshell
        } catch (Exception e) {
          this.setError("cat: " + path + ": No such file or directory");
        }
      }
      if (!(textfiles.isEmpty())) {
        this.showContent(textfiles);
      }
    }
  }

  /**
   * Given a list of TextFiles, show contents of the files.
   * 
   * @param textfiles a list of text files with contents that will be displayed
   */
  private void showContent(ArrayList<TextFile> textfiles) {
    String output = "";
    for (int index = 0; index < textfiles.size(); index++) {
      // add file content to output
      TextFile file = textfiles.get(index);
      output += file.getContents();
      // three line breaks between each file's content
      if (index != textfiles.size() - 1) {
        for (int subindex = 0; subindex < 4; subindex++) {
          output += System.lineSeparator();
        }
      }
    }
    // display contents to Jshell
    this.setPrintCommand(output);
  }
}
