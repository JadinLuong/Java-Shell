package driver;

import java.util.ArrayList;

public class Save extends Command {

  /**
   * The tool to convert object into reconstructible file.
   */
  private JFileSystem fileSystem;
  private FileSystemSaver saver;

  /**
   * Construct a save object.
   * 
   * @param filename
   * @param content
   */
  public Save(FileSystem<Directory> fileSystem, FileSystemSaver saver) {
    super(false);
    this.fileSystem = (JFileSystem) fileSystem;
    this.saver = saver;
  }

  @Override
  /**
   * Execute the command will store the current fileSystem into the specified
   * file. A string representation of error message would be given for
   * appropriate exceptions.
   * 
   * @param arguments The only element in the arguments should be the absolute
   *        path to the file.
   */
  public void executeCommand(ArrayList<String> arguments) {

    if (arguments.isEmpty()) {
      this.setError("Save: no file name given");
    }

    else if (arguments.size() > 1) {
      this.setError("Save: too many file names given");
    }
    else {
      String filePath = arguments.get(0);
      this.setError(saver.saveFileSystem(filePath, this.fileSystem));
    }
  }

  public String toString() {
    return fileSystem.toString();
  }
}
